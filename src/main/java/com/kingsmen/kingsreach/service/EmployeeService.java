package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.enums.Department;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface EmployeeService {

	ResponseEntity<ResponseStructure<Employee>> addEmployee(Employee employee);

	ResponseEntity<ResponseStructure<List<Employee>>> login(String officialEmail, String password);

	ResponseEntity<ResponseStructure<List<Employee>>> getEmployees();

	ResponseEntity<ResponseStructure<Employee>> editEmployee(Employee employee);

	ResponseEntity<ResponseStructure<List<Employee>>> getManager();

	ResponseEntity<ResponseStructure<List<Employee>>> getManagerEmployee(Department department);

	

}
