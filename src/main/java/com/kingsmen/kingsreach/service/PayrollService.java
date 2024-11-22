package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface PayrollService {
	
	ResponseEntity<ResponseStructure<Payroll>> paySalary(Payroll payroll);

}
