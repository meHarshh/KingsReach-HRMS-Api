package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.service.PayrollService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
@RestController
public class PayrollController {
	
	@Autowired
	private PayrollService payrollService;
	
	@PostMapping(value= "/paysalary")
	public ResponseEntity<ResponseStructure<Payroll>> paySalary(@RequestBody Payroll payroll){
		return payrollService.paySalary(payroll);
		
	}
	

}
