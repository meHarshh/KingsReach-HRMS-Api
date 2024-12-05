package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface PayrollService {
	
	ResponseEntity<ResponseStructure<Payroll>> paySalary(Payroll payroll);

	ResponseEntity<ResponseStructure<List<Payroll>>> getEmployeesSalary();

	ResponseEntity<ResponseStructure<Payroll>> deleteEmployeeSalary(int payrollId);

	ResponseEntity<ResponseStructure<Payroll>> getEmployeeSalary(String employeeId);

	ResponseEntity<ResponseStructure<Payroll>> editEmployeeSalary(Payroll payroll);

	ResponseEntity<ResponseStructure<Payroll>> approvedSalarySlip(Payroll payroll);

}
