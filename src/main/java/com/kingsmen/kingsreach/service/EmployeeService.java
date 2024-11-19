package com.kingsmen.kingsreach.service;

import java.util.List;

import com.kingsmen.kingsreach.dto.Credentials;
import com.kingsmen.kingsreach.entity.Employee;

public interface EmployeeService {

	Employee addEmployee(Employee employee);

	List<Employee> login(Credentials credentials);

}
