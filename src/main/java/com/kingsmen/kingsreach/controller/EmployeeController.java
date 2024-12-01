package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.dto.Credentials;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.service.EmployeeService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/addEmployee")
	public ResponseEntity<ResponseStructure<Employee>> addEmployee(@RequestBody Employee employee) {
		return employeeService.addEmployee(employee);
	}

	@GetMapping("/login")
	public ResponseEntity<ResponseStructure<List<Employee>>> login(@RequestBody Credentials credentials) {
		return employeeService.login(credentials);
	}
	
	@GetMapping("/fetchAllEmployees")
	public List<Employee> getEmployees(){
		return employeeService.getEmployees();
	}
	
	@PutMapping("/editEmployee")
	public Employee editEmployee(@RequestBody Employee employee) {
		return employeeService.editEmployee(employee);
	}
}
