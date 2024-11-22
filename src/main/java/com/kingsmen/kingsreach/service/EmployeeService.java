package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.dto.Credentials;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface EmployeeService {

	ResponseEntity<ResponseStructure<Employee>> addEmployee(Employee employee);

	ResponseEntity<ResponseStructure<List<Employee>>> login(Credentials credentials);

}
