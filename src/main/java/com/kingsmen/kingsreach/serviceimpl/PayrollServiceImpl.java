package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.PayrollRepo;
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
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("The payroll of " + employee.getFirstName() + " from " + payroll.getDepartment()
				+ " department has been updated");

		responseStructure.setData(payrollRepo.save(payroll));

		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<List<Payroll>>> getEmployeesSalary() {
		List<Payroll> list = payrollRepo.findAll();

		ResponseStructure<List<Payroll>> responseStructure = new ResponseStructure<List<Payroll>>();
		responseStructure.setData(list);
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("The employees payroll data is fetched");

		return new ResponseEntity<ResponseStructure<List<Payroll>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> getEmployeeSalary(String employeeId) {

		Optional<Employee> optionalEmployee = employeeRepo.findByEmployeeId(employeeId);

		if (optionalEmployee.isEmpty()) {
			ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
			responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("Employee not found with ID: " + employeeId);
			responseStructure.setData(null);

			return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
		}

		Employee employee = optionalEmployee.get();

		Payroll payroll = employee.getPayroll();

		if (payroll == null) {
			ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
			responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
			responseStructure.setMessage("No payroll record found for employee ID: " + employeeId);
			responseStructure.setData(null);

			return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
		}

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure
				.setMessage("Salary details of employee " + payroll.getEmployeeId() + " fetched successfully.");
		responseStructure.setData(payroll);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> editEmployeeSalary(Payroll payroll) {
		// Optional<Employee> optionalEmployee =
		// employeeRepo.findByEmployeeId(employeeId);
		//
		// if (optionalEmployee.isEmpty()) {
		// ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		// responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
		// responseStructure.setMessage("Employee not found with ID: " + employeeId);
		// responseStructure.setData(null);
		//
		// return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
		// }
		//
		// Employee employee = optionalEmployee.get();
		//
		// Payroll existingPayroll = employee.getPayroll();
		//
		// if (existingPayroll == null) {
		// ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		// responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
		// responseStructure.setMessage("No payroll record found for employee ID: " +
		// employeeId);
		// responseStructure.setData(null);
		//
		// return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
		// }

		Payroll existingPayroll = payrollRepo.findById(payroll.getPayrollId())
				.orElseThrow(() -> new RuntimeException());

		existingPayroll.setSalary(payroll.getSalary());
		existingPayroll.setTaxDeduction(payroll.getTaxDeduction());
		existingPayroll.setSpecialAllowance(payroll.getSpecialAllowance());
		existingPayroll.setEmployeeStateInsurance(payroll.getEmployeeStateInsurance());
		existingPayroll.setHouseRentAllowance(payroll.getHouseRentAllowance());
		existingPayroll.setProvidentFund(payroll.getProvidentFund());
		existingPayroll.setGrossSalary(payroll.getGrossSalary());
		existingPayroll.setOtherAllowance(payroll.getOtherAllowance());
		existingPayroll.setEmployeeProvidentFund(payroll.getEmployeeProvidentFund());

		Payroll updatedPayroll = payrollRepo.save(existingPayroll);

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee salary updated successfully");
		responseStructure.setData(updatedPayroll);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> deleteEmployeeSalary(int payrollId) {

		Payroll payroll = payrollRepo.findById(payrollId).orElseThrow(() -> new RuntimeException());

		payrollRepo.deleteById(payrollId);

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee salary detail deleted successfully.");
		responseStructure.setData(payroll);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);

	}

	public Payroll generateMonthlyPayroll(String employeeId) {
		Payroll payroll = payrollRepo.findByEmployeeId(employeeId);

		double grossSalary = payroll.getSalary();
		double basicPay = payroll.getBasicPay();
		double tax = calculateTax(grossSalary);
		double pf = calculateProvidentFund(basicPay);

		payroll.setTaxDeduction(tax);
		payroll.setEmployeeProvidentFund(pf);
		payroll.setGrossSalary(grossSalary - tax - pf);

		payrollRepo.save(payroll);
		return payroll;
	}

	private double calculateTax(double grossSalary) {
		return 0.0; // 10% tax deduction
	}

	private double calculateProvidentFund(double basePay) {
		if (basePay >= 15000) {
			return 15000 * 0.12;
		} else
			return basePay * 0.12; // 12% PF deduction
	}

}
