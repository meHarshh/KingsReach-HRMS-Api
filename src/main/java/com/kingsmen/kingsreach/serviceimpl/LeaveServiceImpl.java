package com.kingsmen.kingsreach.serviceimpl;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.enums.LeaveStatus;
import com.kingsmen.kingsreach.enums.LeaveType;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.repository.PayrollRepo;
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

	// Apply Leave Logic
	public String applyLeave(Leave leave) {
		Employee employee = employeeRepository.findByEmployeeId(leave.getEmployeeId())
				.orElseThrow(() -> new RuntimeException());
		Payroll payroll = payrollRepository.findByEmployeeId(leave.getEmployeeId());

		// Calculate days of leave requested
		long leaveDays = ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()) + 1;

		// Validate leave balance
		switch (leave.getLeaveType()) {
		case LeaveType.SICK:
			if (employee.getLeave().getSickLeaveBalance() < leaveDays) {
				return "Insufficient sick leave balance";
			}
			employee.getLeave().setSickLeaveBalance(employee.getLeave().getSickLeaveBalance() - (int) leaveDays);
			break;

		case LeaveType.CASUAL:
			if (employee.getLeave().getCasualLeaveBalance() < leaveDays) {
				return "Insufficient casual leave balance";
			}
			employee.getLeave().setCasualLeaveBalance(employee.getLeave().getCasualLeaveBalance() - (int) leaveDays);
			break;

		case LeaveType.PAID:
			if (employee.getLeave().getPaidLeaveBalance() < leaveDays) {
				return "Insufficient paid leave balance";
			}
			employee.getLeave().setPaidLeaveBalance(employee.getLeave().getPaidLeaveBalance() - (int) leaveDays);
			break;

		default:
			return "Invalid leave type";
		}

//        leave.(LeaveStatus.APPROVED);
		leaveRepository.save(leave);

		// Adjust payroll if applicable
		adjustPayrollForLeave(employee, payroll, leaveDays, leave.getLeaveStatus() == LeaveStatus.NOT_APPROVED);
		return "Leave applied successfully";
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

}
