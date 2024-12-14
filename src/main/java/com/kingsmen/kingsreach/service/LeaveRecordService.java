package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.LeaveRecord;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface LeaveRecordService {

	ResponseEntity<ResponseStructure<LeaveRecord>> saveEmployeeLeaveRecord(LeaveRecord leaveRecord);

	ResponseEntity<ResponseStructure<List<LeaveRecord>>> getEmployeesLeaveRecords();

	ResponseEntity<ResponseStructure<List<LeaveRecord>>> getEmployeeLeaveRecord(String employeeId);
	

}
