package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.entity.Reimbursement;
import com.kingsmen.kingsreach.enums.ReimbursementStatus;
import com.kingsmen.kingsreach.exceptions.PayrollNotFoundException;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.PayrollRepo;
import com.kingsmen.kingsreach.repo.ReimbursementRepo;
import com.kingsmen.kingsreach.service.PayrollService;
import com.kingsmen.kingsreach.util.ResponseStructure;

import jakarta.transaction.Transactional;

@Service
public class PayrollServiceImpl implements PayrollService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private PayrollRepo payrollRepo;

	@Autowired
	private LeaveRepo leaveRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	private ReimbursementRepo reimbursementRepository;

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> paySalary(Payroll payroll) {

		String employeeId = payroll.getEmployeeId();
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employeeId);
		Employee employee = byEmployeeId.get();

		payroll.setEmployee(employee);

		double lopDays = payroll.getLopDays();

		double salary = payroll.getSalary();

		int finalSalary = calculateLopDeduction(salary, lopDays);
		// int lopDeduction = calculateLop(salary, lopDays);

		double pfDeduction = finalSalary * 0.12;
		finalSalary = (int) (finalSalary - pfDeduction);

		payroll.setSalary(finalSalary);

		// payroll.setLopDeduction(lopDeduction);

		payroll.setProvidentFund(pfDeduction);

		payroll.setDate(LocalDate.now());
		System.out.println(LocalDate.now());

		String message = "The payroll of " + employee.getName() + " from " + payroll.getDepartment()
				+ " department has been updated";

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<Payroll>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(message);
		responseStructure.setData(payrollRepo.save(payroll));

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(payroll.getEmployeeId());
		notify.setMessage(message);
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);

	}

	private double calculateTotalReimbursement(String employeeId) {
		YearMonth currentMonth = YearMonth.from(LocalDate.now());

		LocalDate startDate = currentMonth.atDay(1);
		LocalDate endDate = currentMonth.atEndOfMonth();

		List<Reimbursement> reimbursements = reimbursementRepository.findByEmployeeIdAndDateBetween(employeeId,
				startDate, endDate);

		for (Reimbursement reimbursement : reimbursements) {
			System.out.println(reimbursement.getAmount());
		}
		
		// Calculate total approved reimbursement amount
		return reimbursements.stream().filter(r -> r.getReimbursementStatus() == ReimbursementStatus.APPROVED)
				.mapToDouble(Reimbursement::getAmount).sum();
	}

	private int calculateLop(double salary, double lopDays) {
		double lopPerDay = salary / 30;
		int totalLopDeduction = (int) (lopPerDay * lopDays);

		return totalLopDeduction;
	}

	public int calculateLopDeduction(double salary, double lopDays) {

		double lopPerDay = salary / 30;
		double totalLopDeduction = lopPerDay * lopDays;

		int finalSalary = (int) (salary - totalLopDeduction);

		return finalSalary;
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
				.setMessage("Salary details of employee " + payroll.getEmployeeName() + " fetched successfully.");
		responseStructure.setData(payroll);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> editEmployeeSalary(Payroll payroll) {

		Payroll existingPayroll = payrollRepo.findById(payroll.getPayrollId())
				.orElseThrow(() -> new RuntimeException());

		double lopDays = payroll.getLopDays();
		double salary = payroll.getSalary();
		double reimbursment = calculateTotalReimbursement(payroll.getEmployeeId());
		int lopDeduction = calculateLop(salary, lopDays);
		int basicPay = calculateBasicPay(salary);

		existingPayroll.setReimbursementAmount(reimbursment);
		existingPayroll.setSalary(payroll.getSalary());
		existingPayroll.setTaxDeduction(payroll.getTaxDeduction());
		existingPayroll.setSpecialAllowance(payroll.getSpecialAllowance());
		existingPayroll.setEmployeeStateInsurance(payroll.getEmployeeStateInsurance());
		existingPayroll.setHouseRentAllowance(payroll.getHouseRentAllowance());
		existingPayroll.setProvidentFund(payroll.getProvidentFund());
		existingPayroll.setGrossSalary(payroll.getGrossSalary());
		existingPayroll.setOtherAllowance(payroll.getOtherAllowance());
		existingPayroll.setEmployeeProvidentFund(payroll.getEmployeeProvidentFund());
		existingPayroll.setLopDays(payroll.getLopDays());
		existingPayroll.setLopDeduction(lopDeduction);
		existingPayroll.setBasicPay(basicPay);

		Payroll updatedPayroll = payrollRepo.save(existingPayroll);

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(payroll.getEmployeeName() + " salary updated successfully");
		responseStructure.setData(updatedPayroll);

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(payroll.getEmployeeId());
		notify.setMessage(payroll.getEmployeeName() + " salary updated successfully");
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	private int calculateBasicPay(double salary) {
		int basicPay = (int) (salary - (salary * 0.40));
		return basicPay;
	}

	@Transactional
	@Override
	public ResponseEntity<ResponseStructure<Payroll>> deleteEmployeeSalary(int payrollId) {

		Payroll payroll = payrollRepo.findById(payrollId)
				.orElseThrow(() -> new PayrollNotFoundException("Payroll Assosiated with given ID not found"));

		payrollRepo.deleteById(payrollId);

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(payroll.getEmployeeName() + " salary detail deleted successfully.");
		responseStructure.setData(payroll);

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(payroll.getEmployeeId());
		notify.setMessage(payroll.getEmployeeName() + " salary detail deleted successfully.");
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

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

	@Override
	public ResponseEntity<ResponseStructure<Payroll>> approvedSalarySlip(Payroll payroll) {

		Payroll payroll2 = payrollRepo.findById(payroll.getPayrollId())
				.orElseThrow(() -> new PayrollNotFoundException("Payroll with the ID is not found."));

		payroll2.setPayrollStatus(payroll.getPayrollStatus());

		payrollRepo.save(payroll2);

		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(payroll.getEmployeeName() + " Payroll status changed");
		responseStructure.setData(payroll2);

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(payroll.getEmployeeId());
		notify.setMessage(payroll.getEmployeeName() + " Payroll status changed");
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Object>>> getPayrollDetails(String employeeId) {
		Optional<Employee> employees = employeeRepo.findByEmployeeId(employeeId);
		Payroll payrolls = payrollRepo.findByEmployeeId(employeeId);
		Optional<Leave> leaves = leaveRepo.findByEmployeeId(employeeId);

		List<Object> list = new ArrayList<Object>();
		list.add(leaves);
		list.add(employees);
		list.add(payrolls);

		ResponseStructure<List<Object>> responseStructure = new ResponseStructure<List<Object>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("All Payroll Details fetched Successfully.");
		responseStructure.setData(list);

		return new ResponseEntity<ResponseStructure<List<Object>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Payroll>>> getPayrollOfMonth(LocalDate date) {

		int month = date.getMonthValue();
		int year = date.getYear();

		LocalDate startDate = LocalDate.of(year, month, 1);
		LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

		List<Payroll> payroll = payrollRepo.findByDateBetween(startDate, endDate);

		ResponseStructure<List<Payroll>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Salary details of employee for the month " + date + " fetched successfully.");
		responseStructure.setData(payroll);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);

	}

}
