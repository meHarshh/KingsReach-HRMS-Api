package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.service.LeaveService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RestController
public class LeaveController {

	@Autowired
	private LeaveService leaveservice;

	@PostMapping(value = "/applyLeave")
	private ResponseEntity<ResponseStructure<Leave>> applyLeave(@PathVariable   int leaveId) {
		return leaveservice.applyLeave(leaveId);
	}

	@PutMapping(value = "/leaveStatus")
	private ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(@RequestBody Leave leave) {
		return leaveservice.changeLeaveStatus(leave);
	}

	@GetMapping(value = "/findAllLeave")
	private ResponseEntity<ResponseStructure<List<Leave>>> getLeaves() {
		return leaveservice.getLeave();
	}

	@GetMapping(value = "/getEmployeeLeave/{employeeId}")
	private ResponseEntity<ResponseStructure<List<Leave>>> getEmployeeLeave(@PathVariable String employeeId) {
		return leaveservice.getEmployeeLeave(employeeId);
	}

}
