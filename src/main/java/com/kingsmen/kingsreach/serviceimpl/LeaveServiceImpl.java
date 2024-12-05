package com.kingsmen.kingsreach.serviceimpl;

import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.enums.LeaveStatus;
import com.kingsmen.kingsreach.enums.LeaveType;
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


	@Override
	// Apply Leave Logic
	public ResponseEntity<ResponseStructure<Leave>> applyLeave(int leaveId) {
		 Leave leave = leaveRepository.findById(leaveId)
		            .orElseThrow(() -> new RuntimeException("Leave not found for ID: " + leaveId));
		 
		 ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		 responseStructure.setStatusCode(HttpStatus.OK.value());
		 responseStructure.setData(leave);
		 
		 return ResponseEntity.ok(responseStructure);
	}
	
//		Employee employee = employeeRepo.findByEmployeeId(leave.getEmployeeId())
//				.orElseThrow(() -> new RuntimeException("Employee not found for ID:"));

	// Reset LOP Days and Carry-Forward Leave Balances on the 1st of the Month
	@Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of each month
	public void resetLopAndCarryForwardLeaves() {
		List<Leave> leaves = leaveRepository.findAll();

		for (Leave leave : leaves) {
			// Carry forward unused leaves
			if (leave.getCasualLeaveBalance() < 11) {
				leave.setCasualLeaveBalance(Math.min(11, leave.getCasualLeaveBalance() + 1));
			}

			if (leave.getSickLeaveBalance() < 11) {
				leave.setSickLeaveBalance(Math.min(11, leave.getSickLeaveBalance() + 1));
			}

			if (leave.getPaidLeaveBalance() < 8) {
				leave.setPaidLeaveBalance(Math.min(8, leave.getPaidLeaveBalance() + 1));
			}

			leaveRepository.save(leave);
		}

		List<Payroll> payrolls = payrollRepository.findAll();
		for (Payroll payroll : payrolls) {
			// Reset LOP Days
			payroll.setLopDays(0);
			payrollRepository.save(payroll);
		}
	}
	

	public ResponseEntity<ResponseStructure<Leave>> applyLeave(Leave leave) {
		ResponseStructure<Leave> responseStructure = new ResponseStructure<>();

		// Fetch the employee's leave record
		Leave existingLeave = leaveRepository.findByEmployeeId(leave.getEmployeeId()).orElseThrow(
				() -> new RuntimeException("Leave record not found for employee ID: " + leave.getEmployeeId()));
		Payroll payroll = payrollRepository.findByEmployeeId(leave.getEmployeeId());
		if (payroll == null) {
			throw new RuntimeException("Payroll record not found for employee ID: " + leave.getEmployeeId());
		}

		// Calculate leave days
		long leaveDays = ChronoUnit.DAYS.between(leave.getFromDate(), leave.getToDate()) + 1;

		// Get the current month
		Month currentMonth = leave.getFromDate().getMonth();

		// Define max leave limits for March and April
		int maxCasualLeave = 1; // 1/month for all months
		int maxSickLeave = 1; // 1/month for all months
		int maxPaidLeave = (currentMonth == Month.MARCH || currentMonth == Month.APRIL) ? 0 : 1;

		// Check leave type and update balances
		switch (leave.getLeaveType()) {
		case LeaveType.SICK:
			if (existingLeave.getSickLeaveBalance() < leaveDays) {
				responseStructure.setMessage("Insufficient sick leave balance");
				return ResponseEntity.badRequest().body(responseStructure);
			}
			if (leaveDays > maxSickLeave) {
				int lopDays = (int) leaveDays - maxSickLeave;
				payroll.setLopDays(payroll.getLopDays() + lopDays);
			}
			existingLeave.setSickLeaveBalance(existingLeave.getSickLeaveBalance() - (int) leaveDays);
			break;

		case LeaveType.CASUAL:
			if (existingLeave.getCasualLeaveBalance() < leaveDays) {
				responseStructure.setMessage("Insufficient casual leave balance");
				return ResponseEntity.badRequest().body(responseStructure);
			}
			if (leaveDays > maxCasualLeave) {
				int lopDays = (int) leaveDays - maxCasualLeave;
				payroll.setLopDays(payroll.getLopDays() + lopDays);
			}
			existingLeave.setCasualLeaveBalance(existingLeave.getCasualLeaveBalance() - (int) leaveDays);
			break;

		case LeaveType.PAID:
			if (currentMonth == Month.MARCH || currentMonth == Month.APRIL) {
				responseStructure.setMessage("Paid leave is not available in March and April");
				return ResponseEntity.badRequest().body(responseStructure);
			}
			if (existingLeave.getPaidLeaveBalance() < leaveDays) {
				responseStructure.setMessage("Insufficient paid leave balance");
				return ResponseEntity.badRequest().body(responseStructure);
			}
			if (leaveDays > maxPaidLeave) {
				int lopDays = (int) leaveDays - maxPaidLeave;
				payroll.setLopDays(payroll.getLopDays() + lopDays);
			}
			existingLeave.setPaidLeaveBalance(existingLeave.getPaidLeaveBalance() - (int) leaveDays);
			break;

		default:
			responseStructure.setMessage("Invalid leave type");
			return ResponseEntity.badRequest().body(responseStructure);
		}

		leaveRepository.save(existingLeave);
		payrollRepository.save(payroll);

		responseStructure.setData(existingLeave);
		responseStructure.setMessage("Leave applied successfully");
		return ResponseEntity.ok(responseStructure);
	}
 

	@Override
	public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {
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
