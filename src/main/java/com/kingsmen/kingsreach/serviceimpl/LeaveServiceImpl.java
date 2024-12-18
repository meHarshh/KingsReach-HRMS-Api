package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.LeaveRecord;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.enums.Department;
import com.kingsmen.kingsreach.enums.LeaveStatus;
import com.kingsmen.kingsreach.enums.LeaveType;
import com.kingsmen.kingsreach.exceptions.EmployeeIdNotExistsException;
import com.kingsmen.kingsreach.exceptions.LeaveIdNotFoundException;
import com.kingsmen.kingsreach.exceptions.PayrollDetailsNotFoundException;
import com.kingsmen.kingsreach.repo.LeaveRecordRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.PayrollRepo;
import com.kingsmen.kingsreach.service.LeaveService;
import com.kingsmen.kingsreach.util.ResponseStructure;

import jakarta.transaction.Transactional;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private LeaveRepo leaveRepository;

	@Autowired
	private PayrollRepo payrollRepository;

	@Autowired
	private LeaveRecordRepo leaveRecordRepo;

	@Autowired
	private NotificationRepo notificationRepo;

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

		Payroll payroll = payrollRepository.findByEmployeeId(leave.getEmployeeId());
		if (payroll == null) {
			throw new PayrollDetailsNotFoundException("Payroll record not found for employee ID: " + leave.getEmployeeId() + " Enter valid Employee ID");
		}
		// Fetch the employee's leave record

		Leave existingLeave = leaveRepository.findByEmployeeId(leave.getEmployeeId())
				.orElse(new Leave());

		saveLeaveRecord(leave);
		existingLeave.setEmployeeName(leave.getEmployeeName());
		existingLeave.setEmployee(leave.getEmployee());
		existingLeave.setEmployeeId(leave.getEmployeeId());
		existingLeave.setFromDate(leave.getFromDate());
		existingLeave.setToDate(leave.getToDate());
		existingLeave.setLeaveType(leave.getLeaveType());
		existingLeave.setApprovedBy(leave.getApprovedBy());
		existingLeave.setReason(leave.getReason());
		existingLeave.setLeaveStatus(LeaveStatus.PENDING);

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
			if (existingLeave.getSickLeaveBalance() <= leaveDays) {
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
			if (existingLeave.getCasualLeaveBalance() <= leaveDays) {
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

		case LeaveType.EMERGENCY:
			if (existingLeave.getEmergencyLeaveBalance() <= leaveDays) {
				responseStructure.setMessage("Insufficient Emergency leave balance");
				return ResponseEntity.badRequest().body(responseStructure);
			}
			if (leaveDays > existingLeave.getEmergencyLeaveBalance()) {
				int lopDays = (int) leaveDays - existingLeave.getEmergencyLeaveBalance();
				payroll.setLopDays(payroll.getLopDays() + lopDays);
			}
			existingLeave.setEmergencyLeaveBalance(existingLeave.getEmergencyLeaveBalance() - (int) leaveDays);
			break;

		default:
			responseStructure.setMessage("Invalid leave type");
			return ResponseEntity.badRequest().body(responseStructure);
		}

		leaveRepository.save(existingLeave);
		payrollRepository.save(payroll);

		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(existingLeave);
		responseStructure.setMessage("Leave applied successfully" + leave.getEmployeeName());

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(leave.getEmployeeId());
		notify.setMessage("Leave applied successfully" + leave.getEmployeeName());
		notificationRepo.save(notify);

		return ResponseEntity.ok(responseStructure);
	}

	private void saveLeaveRecord(Leave leave) {
		LeaveRecord leaveRecord = new LeaveRecord();
		leaveRecord.setEmployee(leave.getEmployee());
		leaveRecord.setEmployeeId(leave.getEmployeeId());
		leaveRecord.setEmployeeName(leave.getEmployeeName());
		leaveRecord.setApprovedBy(leave.getApprovedBy());
		leaveRecord.setFromDate(leave.getFromDate());
		leaveRecord.setToDate(leave.getToDate());

		leaveRecordRepo.save(leaveRecord);
	}

	@Override
	@Transactional
	public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {
		Leave leave2 = leaveRepository.findById(leave.getLeaveId()).orElseThrow(() -> new LeaveIdNotFoundException("Invalid Leave ID"));

		leave2.setLeaveStatus(leave.getLeaveStatus());
		leave2 = leaveRepository.findById(leave2.getLeaveId()).orElseThrow(() -> new LeaveIdNotFoundException("Invalid Leave ID"));

		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setData(leave2);
		responseStructure.setMessage("The leave status changed to " + leave2.getLeaveStatus());
		responseStructure.setStatusCode(HttpStatus.OK.value());

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(leave.getEmployeeId());
		notify.setMessage("The leave status changed to " + leave2.getLeaveStatus());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Leave>>> getLeave() {
		List<Leave> list = leaveRepository.findAll();

		ResponseStructure<List<Leave>> responseStructure = new ResponseStructure<List<Leave>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(list);
		responseStructure.setMessage("Leave details fetched Successfully.");

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("Leave details fetched Successfully.");
		notificationRepo.save(notify);

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

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(employeeId);
		notify.setMessage("The Employees Leave Deatils Fetched Successfully.");
		notificationRepo.save(notify);

		return ResponseEntity.ok(responseStructure);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Leave>>> findAbsentEmployees() {
		List<Leave> list = leaveRepository.findByFromDate(LocalDate.now());

		ResponseStructure<List<Leave>> responseStructure = new ResponseStructure<List<Leave>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Absent Employees Leave Deatils Fetched Successfully.");
		responseStructure.setData(list);

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("Absent Employees Leave Deatils Fetched Successfully.");
		notificationRepo.save(notify);

		return ResponseEntity.ok(responseStructure);
	}

	@Override
	public ResponseEntity<ResponseStructure<Map<String, Integer>>> getRemainingLeave(String employeeId) {
		// TODO Auto-generated method stub

		Leave orElseThrow = leaveRepository.findByEmployeeId(employeeId)
				.orElseThrow(() -> new EmployeeIdNotExistsException("Invalid Employee ID"));

		int casualLeaveBalance = orElseThrow.getCasualLeaveBalance();
		int paidLeaveBalance = orElseThrow.getPaidLeaveBalance();
		int sickLeaveBalance = orElseThrow.getSickLeaveBalance();
		int emergencyLeaveBalance = orElseThrow.getEmergencyLeaveBalance();

		//int[] pending = { casualLeaveBalance, paidLeaveBalance, sickLeaveBalance };
		Map<String,Integer> pending = new HashMap<>();
		pending.put("CasualLeaveBalance",casualLeaveBalance);
		pending.put("PaidLeaveBalance",paidLeaveBalance);
		pending.put("SickLeaveBalance",sickLeaveBalance);
		pending.put("EmergencyLeaveBalance",emergencyLeaveBalance);

		ResponseStructure<Map<String, Integer>> responseStructure = new ResponseStructure<Map<String, Integer>>();
		responseStructure.setData(pending);
		responseStructure.setMessage("The response is having the causal , paid, emergency and sick leave in the array");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(employeeId);
		notify.setMessage("The response is having the causal , paid, emergency and sick leave in the array");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Map<String, Integer>>>(responseStructure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<List<Leave>>> findAbsentEmployees(Department department) {
		// TODO Auto-generated method stub
		List<Leave> all = leaveRepository.findAll();

		ArrayList<Leave> leaves = new ArrayList<Leave>();
		for (Leave leave : all) {
			if (leave.getEmployee().getDepartment() == department)
				leaves.add(leave);
		}
		ResponseStructure<List<Leave>> responseStructure = new ResponseStructure<List<Leave>>();
		responseStructure.setData(leaves);
		responseStructure.setMessage("The people on leave based on department are below");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("The people on leave based on department are below");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<List<Leave>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Leave>>> fetchLeaveBasedOnManagerEmployee(String employeeId) {
		List<Leave> all = leaveRepository.findAll();

		ArrayList<Leave> leaves = new ArrayList<Leave>();
		//		for (Leave leave : all) {
		//			if (leave.getEmployee().getManager().getEmployeeId().equals(employeeId)) {
		//				leaves.add(leave);
		//			}
		//		}

		for (Leave leave : all) {
			if (leave.getEmployee() != null // Check if leave has an associated employee
					&& leave.getEmployee().getManager() != null // Check if the employee has a manager
					&& employeeId != null // Ensure the provided employeeId is not null
					&& employeeId.equals(leave.getEmployee().getManager().getEmployeeId())) {  	// Match manager's ID
				leaves.add(leave); // Add the leave to the result list
			}
		}
		ResponseStructure<List<Leave>> responseStructure = new ResponseStructure<List<Leave>>();
		responseStructure.setData(leaves);
		responseStructure.setMessage("The people on leave based on manager are below");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(employeeId);
		notify.setMessage("The people on leave based on manager are below");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<List<Leave>>>(responseStructure, HttpStatus.OK);
	}


	//	@Override
	//	public ResponseEntity<ResponseStructure<List<List<Leave>>>> fetchLeaveBasedOnManagerEmployee(String employeeId) {
	//		System.out.println("Input employeeId: " + employeeId);
	//
	//	    List<Leave> allLeaves = leaveRepository.findAll();
	//	    System.out.println("All leaves from the database: " + allLeaves);
	//
	//	    List<Leave> managerLeaves = new ArrayList<>();
	//	    List<Leave> employeeLeaves = new ArrayList<>();
	//
	////	    for (Leave leave : allLeaves) {
	////	        if (leave.getEmployee() != null) {
	////	            Employee employee = leave.getEmployee();
	////	            System.out.println("Processing leave: " + leave);
	////
	////	            if (employeeId.equals(employee.getEmployeeId())) {
	////	                // Manager's leave
	////	                System.out.println("Manager leave added: " + leave);
	////	                managerLeaves.add(leave);
	////	            } else if (employee.getManager() != null 
	////	                    && employeeId.equals(employee.getManager().getEmployeeId())) {
	////	                // Employee under manager
	////	                System.out.println("Employee leave added: " + leave);
	////	                employeeLeaves.add(leave);
	////	            }
	////	        }
	////	    }
	//	    
	//	    for (Leave leave : allLeaves) {
	//	        if (leave.getEmployee() != null) {
	//	            Employee employee = leave.getEmployee();
	//	            System.out.println("Processing leave for employee: " + employee.getEmployeeId());
	//	            System.out.println("Manager ID: " + (employee.getManager() != null ? employee.getManager().getEmployeeId() : "null"));
	//	            if (employeeId.equals(employee.getEmployeeId())) {
	//	                System.out.println("Adding manager's leave: " + leave);
	//	            } else if (employee.getManager() != null && employeeId.equals(employee.getManager().getEmployeeId())) {
	//	                System.out.println("Adding employee's leave under manager: " + leave);
	//	            }
	//	        } else {
	//	            System.out.println("Leave has no associated employee: " + leave);
	//	        }
	//	    }
	//
	//	    List<List<Leave>> responseData = new ArrayList<>();
	//	    responseData.add(managerLeaves);
	//	    responseData.add(employeeLeaves);
	//
	//	    ResponseStructure<List<List<Leave>>> responseStructure = new ResponseStructure<>();
	//	    responseStructure.setData(responseData);
	//	    responseStructure.setMessage("The people on leave based on manager are below");
	//	    responseStructure.setStatusCode(HttpStatus.OK.value());
	//
	//	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	//	}

}

