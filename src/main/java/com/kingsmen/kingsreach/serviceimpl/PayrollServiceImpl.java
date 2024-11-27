//package com.kingsmen.kingsreach.serviceimpl;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import com.kingsmen.kingsreach.entity.Employee;
//import com.kingsmen.kingsreach.entity.Payroll;
//import com.kingsmen.kingsreach.repo.EmployeeRepo;
//import com.kingsmen.kingsreach.repository.PayrollRepo;
//import com.kingsmen.kingsreach.service.PayrollService;
//import com.kingsmen.kingsreach.util.ResponseStructure;
//
//@Service
//public class PayrollServiceImpl implements PayrollService {
//
//	@Autowired
//	private EmployeeRepo employeeRepo;
//
//	@Autowired
//	private PayrollRepo payrollRepo;
//
//	@Override
//	public ResponseEntity<ResponseStructure<Payroll>> paySalary(Payroll payroll) {
//
//		String employeeId = payroll.getEmployeeId();
//		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employeeId);
//		Employee employee = byEmployeeId.get();
//
//		payroll.setEmployee(employee);
//
//		ResponseStructure<Payroll> responseStructure = new ResponseStructure<Payroll>();
//		responseStructure.setStatusCode(HttpStatus.OK.value());
//		responseStructure.setMessage("The payroll of " + employee.getFirstName() + " from " + payroll.getDepartment()
//				+ " department has been updated");
//		responseStructure.setData(payrollRepo.save(payroll));
//
//		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);
//
//	}
//
//	@Override
//	public ResponseEntity<ResponseStructure<List<Payroll>>> getEmployeesSalary() {
//		List<Payroll> list = payrollRepo.findAll();
//
//		ResponseStructure<List<Payroll>> responseStructure = new ResponseStructure<List<Payroll>>();
//		responseStructure.setData(list);
//		responseStructure.setStatusCode(HttpStatus.OK.value());
//		responseStructure.setMessage("The employees payroll data is fetched");
//
//		return new ResponseEntity<ResponseStructure<List<Payroll>>>(responseStructure, HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<ResponseStructure<Payroll>> getEmployeeSalary(String employeeId) {
//		Payroll payroll = payrollRepo.findPayrollByEmployeeId(employeeId);
//
//		ResponseStructure<Payroll> responseStructure = new ResponseStructure<Payroll>();
//		responseStructure.setStatusCode(HttpStatus.OK.value());
//		responseStructure.setMessage(" salary detail of " + payroll.getEmployeeId() + " fetched.");
//		responseStructure.setData(payroll);
//
//		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);
//	}
//
//	@Override
//	public ResponseEntity<ResponseStructure<Payroll>> editEmployeeSalary(String employeeId, Payroll payroll) {
//		Payroll existingPayroll = payrollRepo.findPayrollByEmployeeId(employeeId);
//
//		existingPayroll.setSalary(payroll.getSalary());
//		existingPayroll.setTaxDeduction(payroll.getTaxDeduction());
//		existingPayroll.setSpecialAllowance(payroll.getSpecialAllowance());
//		existingPayroll.setEmployeeStateInsurance(payroll.getEmployeeStateInsurance());
//		existingPayroll.setHouseRentAllowance(payroll.getHouseRentAllowance());
//		existingPayroll.setProvidentFund(payroll.getProvidentFund());
//		existingPayroll.setGrossSalary(payroll.getGrossSalary());
//		existingPayroll.setOtherAllowance(payroll.getOtherAllowance());
//		existingPayroll.setEmployeeProvidentFund(payroll.getEmployeeProvidentFund());
//
//		Payroll updatedPayroll = payrollRepo.save(existingPayroll);
//
//		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
//
//		responseStructure.setStatusCode(HttpStatus.OK.value());
//		responseStructure.setMessage("Employee salary updated successfully");
//		responseStructure.setData(updatedPayroll);
//
//		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);
//	}
//
////	@Override
////	public ResponseEntity<ResponseStructure<Payroll>> deleteEmployeeSalary(String employeeId) {
////		Payroll payroll = payrollRepo.findPayrollByEmployeeId(employeeId);
////
////		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);
////		payroll.setEmployee(employee);
////
////		payrollRepo.delete(payroll);
////
////		ResponseStructure<Payroll> responseStructure = new ResponseStructure<Payroll>();
////		responseStructure.setStatusCode(HttpStatus.OK.value());
////		responseStructure.setMessage(" Employee salary Detail deleted successfully. ");
////		responseStructure.setData(payroll);
////
////		return new ResponseEntity<ResponseStructure<Payroll>>(responseStructure, HttpStatus.OK);
////	}
//
//	@Override
//	public ResponseEntity<ResponseStructure<Payroll>> deleteEmployeeSalary(String employeeId) {
//		// Find payroll by employee ID
//		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employeeId);
//		Employee employee2 = byEmployeeId.get();
//		Payroll payroll = employee2.getPayroll();
//
//		if (payroll == null) {
//			// If payroll is not found, return NOT_FOUND response
//			ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
//			responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
//			responseStructure.setMessage("Payroll not found for employee ID: " + employeeId);
//			responseStructure.setData(null);
//			return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
//		}
//
//		// Validate if the employee exists
//		Optional<Employee> employee = employeeRepo.findByEmployeeId(employeeId);
//		if (employee.isEmpty()) {
//			ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
//			responseStructure.setStatusCode(HttpStatus.NOT_FOUND.value());
//			responseStructure.setMessage("Employee not found for employee ID: " + employeeId);
//			responseStructure.setData(null);
//			return new ResponseEntity<>(responseStructure, HttpStatus.NOT_FOUND);
//		}
//
//		payroll.setEmployee(null);
//
//		// Delete the payroll entry
//		payrollRepo.delete(payroll);
//
//		// Prepare response
//		ResponseStructure<Payroll> responseStructure = new ResponseStructure<>();
//		responseStructure.setStatusCode(HttpStatus.OK.value());
//		responseStructure.setMessage("Employee salary details deleted successfully.");
//		responseStructure.setData(payroll); // Include the deleted payroll details for reference
//
//		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
//	}
//
//}
