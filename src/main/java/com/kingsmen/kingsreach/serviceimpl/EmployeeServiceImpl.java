package com.kingsmen.kingsreach.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.dto.Credentials;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.enums.EmployeeRole;
import com.kingsmen.kingsreach.exceptions.InvalidEmailException;
import com.kingsmen.kingsreach.exceptions.PasswordMismatchException;
import com.kingsmen.kingsreach.exceptions.UserIdOrEmailAlreadyExistException;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.EmployeeService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;


	@Override
	public ResponseEntity<ResponseStructure<Employee>> addEmployee(Employee employee) {
		String email = employee.getEmail();
		String userName = employee.getUserName();
		if (employeeRepo.existsByEmailAndUserName(email, userName)) {
			throw new UserIdOrEmailAlreadyExistException(
					"The user-Id or Email already exists please enter a new email and retry");
		}
		if (!employee.getPassword().equals(employee.getConfirmPassword())) {
			throw new PasswordMismatchException(
					"The password doesnt match, the password and the confirm passwrd should match");
		}

		if (!employee.getEmail().contains("@") && !employee.getEmail().contains(".com")) {
			throw new InvalidEmailException("Please enter the valid email-id");
		}
		employee = employeeRepo.save(employee);

		String message="Employee ID :" + employee.getEmployeeId() +" Added Successfully!!";

		ResponseStructure<Employee> responseStructure=new ResponseStructure<Employee>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(employee);
		return new ResponseEntity<ResponseStructure<Employee>>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Employee>>> login(Credentials credentials) {
		Optional<Employee> employee = employeeRepo.findByEmailAndPassword(credentials.getEmail(),
				credentials.getPassword());

		if (employee.isPresent()) {
			Employee employee2 = employee.get();

			if (employee2.getRole() == EmployeeRole.ADMIN) {
				List<Employee> updatedEmployee = employeeRepo.findAll();

				String message="Employee ID :" + employee2.getEmployeeId() +" LoggedIn Successfully!!";

				ResponseStructure<List<Employee>> responseStructure=new ResponseStructure<List<Employee>>();
				responseStructure.setStatusCode(HttpStatus.FOUND.value());
				responseStructure.setMessage(message);
				responseStructure.setData(updatedEmployee);

				return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure,HttpStatus.FOUND);

			} else {
				List<Employee> employees = new ArrayList<>();
				employees.add(employee2);

				String message="Employee ID :" + employee2.getEmployeeId() +" Added Successfully!!";

				ResponseStructure<List<Employee>> responseStructure=new ResponseStructure<List<Employee>>();
				responseStructure.setStatusCode(HttpStatus.FOUND.value());
				responseStructure.setMessage(message);
				responseStructure.setData(employees);

				return new ResponseEntity<ResponseStructure<List<Employee>>>(responseStructure,HttpStatus.FOUND);
			}
		} else {
			String errorMessage = "Invalid credentials: Employee not found.";

			ResponseStructure<List<Employee>> errorResponse = new ResponseStructure<>();
			errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());  
			errorResponse.setMessage(errorMessage);
			errorResponse.setData(Collections.emptyList());  

			return new ResponseEntity<ResponseStructure<List<Employee>>>(errorResponse, HttpStatus.UNAUTHORIZED);
		}
	}

}
