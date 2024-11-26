package com.kingsmen.kingsreach.serviceimpl;

import java.time.Month;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.service.LeaveService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private LeaveRepo leaveRepo;


	@Override
	public ResponseEntity<ResponseStructure<Leave>> applyLeave(Leave leave) {
		String employeeId=leave.getEmployeeId();

		Optional<Employee> employeeOpt=employeeRepo.findByEmployeeId(employeeId);
		if(!employeeOpt.isPresent()) {
			return buildErrorResponse("Invalid Employee ID");
		}

		Employee employee = employeeOpt.get();
		String approvedBy = leave.getApprovedBy();

		ResponseEntity<ResponseStructure<Leave>> validationResponse = validateAndApplyLeave(employee , leave);
		if(validationResponse != null) {
			return  validationResponse;
		}

		leave.setApprovedBy(approvedBy);
		leave = leaveRepo.save(leave);

		double lopDeduction = calculateLopDeduction(employee , leave);
		if(lopDeduction > 0) {
			if(employee.getPayroll() != null) {
				double newSalary=employee.getPayroll().getGrossSalary() - lopDeduction;
				employee.getPayroll().setGrossSalary(newSalary);
			}else {
				return buildErrorResponse("Employee Payroll information is missing");
			}
		}

		employeeRepo.save(employee);

		String message= "Employee Id:" + leave.getEmployeeId() + " Applied for " + leave.getLeaveType() + " leave";
		ResponseStructure<Leave> responseStructure=new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(leave);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure, HttpStatus.CREATED);	

	}


	private ResponseEntity<ResponseStructure<Leave>> validateAndApplyLeave(Employee employee, Leave leave) {
		int month = leave.getFromDate().getMonthValue();

		switch (leave.getLeaveType()){
		case CASUAL:
			return validateCasualLeave(employee , leave , month);

		case SICK:
			return validateSickLeave(employee , leave);

		case PAID:
			return validatePaidLeave(employee , leave);

		default:
			return buildErrorResponse("Invalid leave type");
		}
	}

	private ResponseEntity<ResponseStructure<Leave>> validateCasualLeave(Employee employee, Leave leave, int month) {
		if(month == Month.MARCH.getValue() || month == Month.APRIL.getValue() ) {
			return buildErrorResponse("Casual leave is not allowed in march and April. ");	
		}
		if(employee.getClBalance() + leave.getNumberOfDays() > 10 ) {
			return buildErrorResponse("Cannot exceeds 10 days of casual Leave for the year. ");	
		}
		if(employee.getClBalance() < leave.getNumberOfDays()) {
			return buildErrorResponse("Insufficient casual leave balance. ");	
		}

		employee.setClBalance(employee.getClBalance() - leave.getNumberOfDays());
		return null;
	}

	private ResponseEntity<ResponseStructure<Leave>> validateSickLeave(Employee employee, Leave leave) {
		if(employee.getSlBalance() + leave.getNumberOfDays() > 12 ) {
			return buildErrorResponse("Cannot exceeds 12 days of sick Leave for the year. ");	
		}
		if(employee.getSlBalance() < leave.getNumberOfDays()) {
			return buildErrorResponse("Insufficient sick leave balance. ");	
		}

		employee.setSlBalance(employee.getSlBalance() - leave.getNumberOfDays());
		return null;
	}

	private ResponseEntity<ResponseStructure<Leave>> validatePaidLeave(Employee employee, Leave leave) {
		if(employee.getPlBalance() + leave.getNumberOfDays() > 12 ) {
			return buildErrorResponse("Cannot exceeds 12 days of Paid Leave for the year. ");	
		}
		if(employee.getPlBalance() < leave.getNumberOfDays()) {
			return buildErrorResponse("Insufficient paid leave balance. ");	
		}

		employee.setPlBalance(employee.getPlBalance() - leave.getNumberOfDays());
		return null;
	}


	private double calculateLopDeduction(Employee employee, Leave leave) {
		int maxAllowedLeaves = (leave.getFromDate().getMonthValue() == Month.MARCH.getValue() || leave.getFromDate().getMonthValue() == Month.APRIL.getValue()) ? 2 : 0;

		if(leave.getNumberOfDays() > maxAllowedLeaves) {
			int excessLeaves = leave.getNumberOfDays() - maxAllowedLeaves;
			return employee.getPayroll().getGrossSalary() * (excessLeaves/30.0);
		}
		return 0.0;
	}

	private ResponseEntity<ResponseStructure<Leave>> buildErrorResponse(String message) {
		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		responseStructure.setMessage(message);
		responseStructure.setData(null);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure, HttpStatus.BAD_REQUEST);
	}


	@Override
	public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {

		String employeeId = leave.getEmployeeId();

		Optional<Leave> optional = leaveRepo.findByEmployeeId(employeeId);

		Leave leave2 = optional.get();

		leave2.setLeaveStatus(leave.getLeaveStatus());
		Leave leave3=leaveRepo.save(leave2);

		String message="Employee ID: " + leave.getEmployeeId() + " leave is " + leave.getLeaveStatus();

		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(leave3);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure,HttpStatus.ACCEPTED);

	}

}
