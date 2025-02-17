package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Admin;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.HR;
import com.kingsmen.kingsreach.entity.Manager;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.entity.SuperAdmin;
import com.kingsmen.kingsreach.enums.Department;
import com.kingsmen.kingsreach.enums.EmployeeRole;
import com.kingsmen.kingsreach.exception.InvalidRoleException;
import com.kingsmen.kingsreach.exceptions.EmployeeIdNotExistsException;
import com.kingsmen.kingsreach.exceptions.InvalidEmailException;
import com.kingsmen.kingsreach.exceptions.PasswordMismatchException;
import com.kingsmen.kingsreach.exceptions.UserIdOrEmailAlreadyExistException;
import com.kingsmen.kingsreach.helper.EmployeeHelper;
import com.kingsmen.kingsreach.repo.AdminRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.HrRepo;
import com.kingsmen.kingsreach.repo.ManagerRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.OnsiteRepo;
import com.kingsmen.kingsreach.repo.SuperAdminRepo;
import com.kingsmen.kingsreach.service.EmployeeService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private ManagerRepo managerRepo;

	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private OnsiteRepo onsiteRepo;

	@Autowired
	private HrRepo hrRepo;

	@Autowired
	private SuperAdminRepo superAdminRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<Employee>> addEmployee(Employee employee) {
		String email = employee.getOfficialEmail();
		String userName = employee.getUserName();
		String employeeId = employee.getEmployeeId();

		if (employeeRepo.existsByEmployeeId(employeeId)) {
			throw new EmployeeIdNotExistsException("The Employee with given Id is Already Exist, Enter valid ID");
		}

		if (employeeRepo.existsByofficialEmailAndUserName(email, userName)) {
			throw new UserIdOrEmailAlreadyExistException(
					"The user-Id or Email already exists please enter a new email and retry");
		}
		if (!employee.getPassword().equals(employee.getConfirmPassword())) {
			throw new PasswordMismatchException(
					"The password doesnt match, the password and the confirm passwrd should match");
		}

		if (!employee.getOfficialEmail().contains("@") && !employee.getOfficialEmail().contains(".com")) {
			throw new InvalidEmailException("Please enter the valid email-id");
		}
		employee.setCreatedAt(LocalDateTime.now());
		employee.setDate(LocalDate.now());
		employee.setName(employee.getFirstName() + " " + employee.getLastName());

		// Setting the manager for the employee by down-casting the Manager entity
		if (employee.getRole() != EmployeeRole.ADMIN && employee.getRole() != EmployeeRole.MANAGER
				&& employee.getRole() != EmployeeRole.HR && employee.getRole() != EmployeeRole.SUPER_ADMIN) {
			String managerId = employee.getManagerId();
			Employee orElseThrow = employeeRepo.findByEmployeeId(managerId).orElseThrow(
					() -> new EmployeeIdNotExistsException("The Employee with given Id is not Exist, Enter valid ID"));
			employee.setManager((Manager) orElseThrow);
		}
		switch (employee.getRole()) {
		case MANAGER:
			Manager manager = new Manager();
			BeanUtils.copyProperties(employee, manager);
			manager = managerRepo.save(manager);
			employee = manager;
			break;

		case ADMIN:
			Admin admin = new Admin();
			BeanUtils.copyProperties(employee, admin);
			admin = adminRepo.save(admin);
			employee = admin;
			break;

		case EMPLOYEE:
			employee = employeeRepo.save(employee);
			break;

		case HR:
			HR hr = new HR();
			BeanUtils.copyProperties(employee, hr);
			hr = hrRepo.save(hr);
			employee = hr;
			break;

		case SUPER_ADMIN:
			SuperAdmin superAdmin = new SuperAdmin();
			BeanUtils.copyProperties(employee, superAdmin);
			superAdmin = superAdminRepo.save(superAdmin);
			employee = superAdmin;
			break;

		default:
			throw new InvalidRoleException("Invalid role specified for the employee");
		}

		String message = "Employee ID :" + employee.getEmployeeId() + " " + employee.getName()
				+ " Added Successfully!!";

		ResponseStructure<Employee> responseStructure = new ResponseStructure<Employee>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(employee);

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(employee.getEmployeeId());
		notify.setMessage(message);
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Employee>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Employee>>> login(String officialEmail, String password) {
		Optional<Employee> employee = employeeRepo.findByofficialEmailAndPassword(officialEmail, password);

		if (employee.isPresent()) {
			Employee employee2 = employee.get();

			List<Employee> employees = new ArrayList<Employee>();
			employees.add(employee2);

			String message = employee2.getName() + " LoggedIn Successfully!!";

			ResponseStructure<List<Employee>> responseStructure = new ResponseStructure<List<Employee>>();
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage(message);
			responseStructure.setData(employees);

			return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure, HttpStatus.OK);
		} else {
			String errorMessage = "Invalid credentials: Employee not found.";

			ResponseStructure<List<Employee>> errorResponse = new ResponseStructure<List<Employee>>();
			errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			errorResponse.setMessage(errorMessage);
			errorResponse.setData(Collections.emptyList());

			return new ResponseEntity<ResponseStructure<List<Employee>>>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Employee>>> getEmployees() {
		List<Employee> list = employeeRepo.findAll();

		ResponseStructure<List<Employee>> responseStructure = new ResponseStructure<List<Employee>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee Details Fetched Successfully.");
		responseStructure.setData(list);

		return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Employee>> editEmployee(Employee employee) {
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employee.getEmployeeId());
		Employee employee2 = byEmployeeId.get();

		employee2.setFirstName(employee.getFirstName());
		employee2.setLastName(employee.getLastName());
		employee2.setOfficialEmail(employee.getOfficialEmail());
		employee2.setUserName(employee.getUserName());
		employee2.setPassword(employee.getPassword());
		employee2.setConfirmPassword(employee.getConfirmPassword());
		employee2.setPhoneNumber(employee.getPhoneNumber());
		employee2.setJoiningDate(employee.getJoiningDate());
		employee2.setAadharCardNumber(employee.getAadharCardNumber());
		employee2.setPanCardNumber(employee.getPanCardNumber());
		employee2.setDob(employee.getDob());
		employee2.setFatherName(employee.getFatherName());
		employee2.setMotherName(employee.getMotherName());
		employee2.setBloodGroup(employee.getBloodGroup());
		employee2.setPermanentAddress(employee.getPermanentAddress());
		employee2.setAsset(employee.getAsset());
		employee2.setDepartment(employee.getDepartment());
		employee2.setRole(employee.getRole());
		employee2.setAttendance(employee.getAttendance());
		employee2.setGender(employee.getGender());
		employee2.setMotherNumber(employee.getMotherNumber());
		employee2.setEmail(employee.getEmail());
		employee2.setPhoneNumber(employee.getPhoneNumber());
		employee2.setOfficialNumber(employee.getOfficialNumber());
		employee2.setEmergencyContact(employee.getEmergencyContact());
		employee2.setEmergencyContactName(employee.getEmergencyContactName());
		employee2.setEmergencyContactRelation(employee.getEmergencyContactRelation());
		employee2.setLocalAddress(employee.getLocalAddress());
		employee2.setBankName(employee.getBankName());
		employee2.setBranchName(employee.getBranchName());
		employee2.setAccountNumber(employee.getAccountNumber());
		employee2.setIfscCode(employee.getIfscCode());
		employee2.setFatherContactNumber(employee.getFatherContactNumber());
		employee2.setExperience(employee.getExperience());
		employee2.setCreatedAt(employee.getCreatedAt());
		employee2.setUpdatedAt(LocalDateTime.now());
		employee2.setUanNumber(employee.getUanNumber());
		employee2.setName(employee.getFirstName() + " " + employee.getLastName());
		employee2.setManagerId(employee.getManagerId());

		if (employee.getRole() == EmployeeRole.EMPLOYEE) {
			Manager orElseThrow = managerRepo.findByEmployeeId(employee.getManagerId()).orElseThrow();
			employee2.setManager(orElseThrow);
		}

		Employee employee3 = employeeRepo.save(employee2);

		ResponseStructure<Employee> responseStructure = new ResponseStructure<Employee>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(employee.getName() + " Data updated Successfully.");
		responseStructure.setData(employee3);

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(employee.getEmployeeId());
		notify.setMessage(employee.getName() + " Data updated Successfully.");
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Employee>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Employee>>> getManager() {
		List<Employee> all = employeeRepo.findAll();

		ArrayList<Employee> employees = new ArrayList<Employee>();
		for (Employee employee : all) {
			if (employee.getRole() == EmployeeRole.MANAGER) {
				employees.add(employee);
			}
		}

		ResponseStructure<List<Employee>> responseStructure = new ResponseStructure<List<Employee>>();
		responseStructure.setData(employees);
		responseStructure.setMessage("The list of the manager is here in the below list");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Object>> employeesStrength() {

		List<Onsite> onsiteList = fetchAllTheOnsiteEmployee(LocalDate.now());
		ArrayList<Onsite> arrayList = new ArrayList<Onsite>();

		for (Onsite onsite : onsiteList) {
			if (onsite.getDate() == LocalDate.now()) {
				arrayList.add(onsite);
			}
		}

		List<Employee> totalEmployees = fetchAllEmployees(LocalDate.now());

		int inOffice = totalEmployees.size() - arrayList.size();

		int[] count = { inOffice, onsiteList.size() };

		ResponseStructure<Object> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee strength details fetched successfully");
		responseStructure.setMessage("inOffice , onsite ");
		responseStructure.setData(count);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);

	}

	private List<Employee> fetchAllEmployees(LocalDate date) {
		return employeeRepo.findEmployeesByDate(LocalDate.now());

	}

	private List<Onsite> fetchAllTheOnsiteEmployee(LocalDate date) {
		return onsiteRepo.findByDate(LocalDate.now());

	}

	public ResponseEntity<ResponseStructure<List<Employee>>> getManagerEmployee(Department department) {

		List<Employee> all = employeeRepo.findAll();
		ArrayList<Employee> employees = new ArrayList<Employee>();

		for (Employee employee : all) {

			if (employee.getDepartment() == department && employee.getRole() != EmployeeRole.MANAGER
					&& employee.getRole() != EmployeeRole.ADMIN) {
				employees.add(employee);
			}
		}

		ResponseStructure<List<Employee>> responseStructure = new ResponseStructure<List<Employee>>();
		responseStructure.setData(employees);
		responseStructure.setMessage("The list of the manager is here in the below list");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure, HttpStatus.OK);

	}

	public ResponseEntity<ResponseStructure<List<EmployeeHelper>>> getEmployeeNameAndDepartment() {
		List<Employee> all = employeeRepo.findAll();

		List<EmployeeHelper> employeeHelpers = new ArrayList<>();

		for (Employee employee : all) {
			EmployeeHelper employeeHelper = new EmployeeHelper();
			employeeHelper.setEmployeeId(employee.getEmployeeId());
			employeeHelper.setDepartment(employee.getDepartment());
			employeeHelper.setEmployeeName(employee.getName());

			employeeHelpers.add(employeeHelper);
		}

		ResponseStructure<List<EmployeeHelper>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee details fetched successfully");
		responseStructure.setData(employeeHelpers);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

}
