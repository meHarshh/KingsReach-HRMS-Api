package com.kingsmen.kingsreach.serviceimpl;

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
		String approvedBy = leave.getApprovedBy();
		String[] split = approvedBy.split(" ");

		Optional<Employee> byEmployeeName = employeeRepo.findByEmployeeFirstName(split[0]);

		leave.setApproved(byEmployeeName.get());

		leave=leaveRepo.save(leave);

		String message="Employee ID: " + leave.getEmployeeId() + " Applied for " + leave.getLeaveType() + " leave.";
		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(leave);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure,HttpStatus.CREATED);

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
