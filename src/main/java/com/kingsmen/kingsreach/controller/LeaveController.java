package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.service.LeaveService;

@RestController
public class LeaveController {

	@Autowired
	private LeaveService leaveservice;

	@PostMapping(value = "/applyLeave")
	private Leave applyLeave(@RequestBody Leave leave) {
		return leaveservice.applyLeave(leave);
	}
	
	@PutMapping(value = "/leaveStatus")
	private Leave changeLeaveStatus(@RequestBody Leave leave) {
		return leaveservice.changeLeaveStatus(leave);
	}
}
