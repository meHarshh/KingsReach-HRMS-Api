package com.kingsmen.kingsreach.serviceimpl;

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
import com.kingsmen.kingsreach.entity.Manager;
import com.kingsmen.kingsreach.enums.EmployeeRole;
import com.kingsmen.kingsreach.exception.InvalidRoleException;
import com.kingsmen.kingsreach.exceptions.InvalidEmailException;
import com.kingsmen.kingsreach.exceptions.PasswordMismatchException;
import com.kingsmen.kingsreach.exceptions.UserIdOrEmailAlreadyExistException;
import com.kingsmen.kingsreach.repo.AdminRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.ManagerRepo;
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

	@Override
	public ResponseEntity<ResponseStructure<Employee>> addEmployee(Employee employee) {
		String email = employee.getOfficialEmail();
		String userName = employee.getUserName();
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

		employee.setName(employee.getFirstName() + " " + employee.getLastName());
		switch (employee.getRole()) {
		case MANAGER:
			// Manager manager = (Manager) employee;
			Manager manager = new Manager();
			BeanUtils.copyProperties(employee, manager);
			manager = managerRepo.save(manager);
			employee = manager;
			break;

		case ADMIN:
			// Admin admin = (Admin) employee;
			Admin admin = new Admin();
			BeanUtils.copyProperties(employee, admin);
			admin = adminRepo.save(admin);
			employee = admin;
			break;

		case EMPLOYEE:
			employee = employeeRepo.save(employee);
			break;

		default:
			throw new InvalidRoleException("Invalid role specified for the employee");
		}

		String message = "Employee ID :" + employee.getEmployeeId() + " Added Successfully!!";

		ResponseStructure<Employee> responseStructure = new ResponseStructure<Employee>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(employee);
		return new ResponseEntity<ResponseStructure<Employee>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Employee>>> login(String officialEmail, String password) {
		Optional<Employee> employee = employeeRepo.findByofficialEmailAndPassword(officialEmail, password);

		if (employee.isPresent()) {
			Employee employee2 = employee.get();

			List<Employee> employees = new ArrayList<Employee>();
			employees.add(employee2);

			String message = "Employee ID :" + employee2.getEmployeeId() + " LoggedIn Successfully!!";

			ResponseStructure<List<Employee>> responseStructure = new ResponseStructure<List<Employee>>();
			responseStructure.setStatusCode(HttpStatus.OK.value());
			responseStructure.setMessage(message);
			responseStructure.setData(employees);

			return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure, HttpStatus.OK);
		}
		// if (employee2.getRole() == EmployeeRole.ADMIN) {
		// List<Employee> updatedEmployee = employeeRepo.findAll();
		//
		// String message = "Employee ID :" + employee2.getEmployeeId() + " LoggedIn
		// Successfully!!";
		//
		// ResponseStructure<List<Employee>> responseStructure = new
		// ResponseStructure<List<Employee>>();
		// responseStructure.setStatusCode(HttpStatus.OK.value());
		// responseStructure.setMessage(message);
		// responseStructure.setData(updatedEmployee);
		//
		// return new
		// ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure,
		// HttpStatus.OK);
		//
		// } else {
		// List<Employee> employees = new ArrayList<>();
		// employees.add(employee2);
		//
		// String message = "Employee ID :" + employee2.getEmployeeId() + " LoggedIn
		// Successfully!!";
		//
		// ResponseStructure<List<Employee>> responseStructure = new
		// ResponseStructure<List<Employee>>();
		// responseStructure.setStatusCode(HttpStatus.OK.value());
		// responseStructure.setMessage(message);
		// responseStructure.setData(employees);
		//
		// return new
		// ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure,
		// HttpStatus.OK);
		// }
		// }
		else {
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
		responseStructure.setMessage("Emloyee Details Fetched Successfully.");
		responseStructure.setData(list);

		return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Employee>> editEmployee(Employee employee) {
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employee.getEmployeeId());
		Employee employee2 = byEmployeeId.get();

		employee2.setEmployeeId(employee.getEmployeeId());
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

		employee2.setFatherContactNumber(employee.getFatherContactNumber());

		Employee employee3 = employeeRepo.save(employee2);

		ResponseStructure<Employee> responseStructure = new ResponseStructure<Employee>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee Data updated Successfully.");
		responseStructure.setData(employee3);
		return new ResponseEntity<ResponseStructure<Employee>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Employee>>> getManager() {
		// TODO Auto-generated method stub
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

}
