package com.kingsmen.kingsreach.serviceimpl;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.enums.LeaveStatus;
import com.kingsmen.kingsreach.enums.LeaveType;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.repo.PayrollRepo;
import com.kingsmen.kingsreach.service.LeaveService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private LeaveRepo leaveRepository;

	@Autowired
	private PayrollRepo payrollRepository;

	@Autowired
	private EmployeeRepo employeeRepository;

	@Override
	// Apply Leave Logic
	public ResponseEntity<ResponseStructure<Leave>> applyLeave(int leaveId) {
		 Leave leave = leaveRepository.findById(leaveId)
		            .orElseThrow(() -> new RuntimeException("Leave not found for ID: " + leaveId));	
		
		Employee employee = employeeRepository.findByEmployeeId(leave.getEmployeeId())
				.orElseThrow(() -> new RuntimeException());

		Payroll payroll = payrollRepository.findByEmployeeId(leave.getEmployeeId());

		// Calculate days of leave requested
		long leaveDays = ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()) + 1;

		// Validate leave balance
		switch (leave.getLeaveType()) {
		case LeaveType.SICK:
			if (employee.getSickLeaveBalance() < leaveDays) {
				ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
				responseStructure.setData(leave);
				responseStructure.setMessage("Insufficient Sick leaves");

				return ResponseEntity.ok(responseStructure);
			}
			employee.setSickLeaveBalance(employee.getSickLeaveBalance() - (int) leaveDays);
			break;

		case LeaveType.CASUAL:
			if (employee.getCasualLeaveBalance() < leaveDays) {

				ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
				responseStructure.setData(leave);
				responseStructure.setMessage("Insufficient Casual leaves");

				return ResponseEntity.ok(responseStructure);
			}
			employee.setCasualLeaveBalance(employee.getCasualLeaveBalance() - (int) leaveDays);
			break;

		case LeaveType.PAID:
			if (employee.getPaidLeaveBalance() < leaveDays) {
				ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
				responseStructure.setData(leave);
				responseStructure.setMessage("Insufficient Paid leaves");

				return ResponseEntity.ok(responseStructure);
			}
			employee.setPaidLeaveBalance(employee.getPaidLeaveBalance() - (int) leaveDays);
			break;

		default:
			ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
			responseStructure.setMessage("Invalid Leave");

			return ResponseEntity.ok(responseStructure);
		}

		// leave.(LeaveStatus.APPROVED);
		Leave leaves = leaveRepository.save(leave);

		// Adjust payroll if applicable
		if (employee.getPayroll() != null) {
			adjustPayrollForLeave(employee, payroll, leaveDays, leave.getLeaveStatus() == LeaveStatus.NOT_APPROVED);
		}

		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setData(leaves);
		responseStructure.setMessage("Leaves Applied Successfully.");
		responseStructure.setStatusCode(HttpStatus.CREATED.value());

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure, HttpStatus.CREATED);
	}

	// Adjust Payroll Logic
	private void adjustPayrollForLeave(Employee employee, Payroll payroll, long leaveDays, boolean isUnapprovedLeave) {
		int lopDays = 0;

		// LOP for excess leaves
		if (leaveDays > 3) {
			lopDays += (int) leaveDays - 3;
		}

		// Additional LOP for unapproved leaves
		if (isUnapprovedLeave) {
			lopDays += (int) leaveDays;
		}

		// Calculate daily salary
		double dailySalary = payroll.getSalary() / 30; // Assuming 30 days in a month
		double lopAmount = lopDays * dailySalary;

		// Adjust salary
		payroll.setGrossSalary(payroll.getGrossSalary() - lopAmount);
		payrollRepository.save(payroll);
	}

	@Override
	public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {
		// TODO Auto-generated method stub
		Leave leave2 = leaveRepository.findById(leave.getLeaveId()).orElseThrow(() -> new RuntimeException());

		leave2.setLeaveStatus(LeaveStatus.APPROVED);

		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setData(leave2);
		responseStructure.setMessage("The leave status changed to " + leave2.getLeaveStatus());

		return ResponseEntity.ok(responseStructure);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Leave>>> getLeave() {
		List<Leave> list = leaveRepository.findAll();

		ResponseStructure<List<Leave>> responseStructure = new ResponseStructure<List<Leave>>();
		responseStructure.setData(list);
		responseStructure.setMessage("Leave details fetched Successfully.");

		return ResponseEntity.ok(responseStructure);  

	}

	@Override
	public ResponseEntity<ResponseStructure<List<Leave>>> getEmployeeLeave(String employeeId) {
		List<Leave> all = leaveRepository.findAll();
		ArrayList<Leave> leaves = new ArrayList<Leave>();
		for (Leave leave : all) {
			if (leave.getEmployeeId().equals(employeeId)) {
				leaves.add(leave);
			}
		}
		
		ResponseStructure<List<Leave>> responseStructure = new ResponseStructure<List<Leave>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("The Employees Leave Deatils Fetched Successfully.");
		responseStructure.setData(leaves);
		
		return ResponseEntity.ok(responseStructure);
	}

}
