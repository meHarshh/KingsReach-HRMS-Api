package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.service.PayrollService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RestController
public class PayrollController {
	
	@Autowired
	private PayrollService payrollService;
	
	@PostMapping(value= "/paysalary")
	public ResponseEntity<ResponseStructure<Payroll>> paySalary(@RequestBody Payroll payroll){
		return payrollService.paySalary(payroll);
		
	}
	
	@GetMapping(value = "/getEmployeesSalary")
	public ResponseEntity<ResponseStructure<List<Payroll>>> getEmployeesSalary(){
		return payrollService.getEmployeesSalary();
	}
	
	@GetMapping(value = "/getSalary/{employeeId}")
	public ResponseEntity<ResponseStructure<Payroll>> getEmployeeSalary(@PathVariable String employeeId){
		return payrollService.getEmployeeSalary(employeeId);
	}
	
	@PutMapping(value= "/editEmployeeSalary")
	public ResponseEntity<ResponseStructure<Payroll>> editEmployeeSalary(@RequestBody Payroll payroll){
		return payrollService.editEmployeeSalary(payroll);
	}
	
	@DeleteMapping(value= "/deleteEmployeeSalary/{payrollId}")
	public ResponseEntity<ResponseStructure<Payroll>> deleteEmployeeSalary(@PathVariable  int payrollId){
		return payrollService.deleteEmployeeSalary(payrollId);
	}

}
