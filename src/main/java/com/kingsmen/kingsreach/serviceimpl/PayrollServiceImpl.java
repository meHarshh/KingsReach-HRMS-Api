package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repository.PayrollRepo;
import com.kingsmen.kingsreach.service.PayrollService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private PayrollRepo payrollRepo;

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> paySalary(Payroll payroll) {

		String employeeId = payroll.getEmployeeId();
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employeeId);
		Employee employee = byEmployeeId.get();

		payroll.setEmployee(employee);

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<Payroll>();
		responseStructure.setData(payrollRepo.save(payroll));
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("The payroll for the employee " + employee.getFirstName() + " has been updated");

		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);

	}

}
