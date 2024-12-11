package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface LeaveService {

	ResponseEntity<ResponseStructure<Leave>> applyLeave(int leaveId);

	ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave);

	ResponseEntity<ResponseStructure<List<Leave>>> getLeave();

	ResponseEntity<ResponseStructure<List<Leave>>> getEmployeeLeave(String employeeId);

	ResponseEntity<ResponseStructure<Leave>> getLeavesTaken(String employeeId);

}
