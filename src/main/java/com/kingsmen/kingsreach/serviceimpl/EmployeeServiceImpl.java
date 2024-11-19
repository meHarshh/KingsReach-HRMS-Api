package com.kingsmen.kingsreach.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.dto.Credentials;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.enums.EmployeeRole;
import com.kingsmen.kingsreach.exceptions.InvalidEmailException;
import com.kingsmen.kingsreach.exceptions.PasswordMismatchException;
import com.kingsmen.kingsreach.exceptions.UserIdOrEmailAlreadyExistException;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public Employee addEmployee(Employee employee) {
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
		return employee;
	}

	@Override
	public List<Employee> login(Credentials credentials) {
		Optional<Employee> employee = employeeRepo.findByEmailAndPassword(credentials.getEmail(),
				credentials.getPassword());

		if (employee.isPresent()) {
			Employee employee2 = employee.get();

			if (employee2.getRole() == EmployeeRole.ADMIN) {
				return employeeRepo.findAll();
			} else {
				List<Employee> employees = new ArrayList<>();
				employees.add(employee2);
				return employees;
			}
		} else {
			return new ArrayList<>();
		}
	}

}
