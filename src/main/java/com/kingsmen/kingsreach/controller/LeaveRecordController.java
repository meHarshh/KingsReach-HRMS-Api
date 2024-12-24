package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.LeaveRecord;
import com.kingsmen.kingsreach.service.LeaveRecordService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "https://hrms.kingsmenrealty.com/")
@RestController
public class LeaveRecordController {
	@Autowired
	private LeaveRecordService leaveRecordService;
	
	@PostMapping(value = "/saveEmployeeLeaveRecord")
	private ResponseEntity<ResponseStructure<LeaveRecord>> saveEmployeeLeaveRecord(@RequestBody LeaveRecord leaveRecord){
		return leaveRecordService.saveEmployeeLeaveRecord(leaveRecord);	
	}
	
	@GetMapping(value = "/fetchAllLeaveRecords")
	private ResponseEntity<ResponseStructure<List<LeaveRecord>>>  getEmployeesLeaveRecords(){
		return leaveRecordService.getEmployeesLeaveRecords();
	}
	
	@GetMapping(value = "/getEmployeeLeaveRecord")
	private ResponseEntity<ResponseStructure<List<LeaveRecord>>>  getEmployeeLeaveRecord(@RequestParam String employeeId){
		return leaveRecordService.getEmployeeLeaveRecord(employeeId);
	}

}
