package com.kingsmen.kingsreach.controller;

import java.util.List;
import java.util.Map;

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
import com.kingsmen.kingsreach.enums.Department;
import com.kingsmen.kingsreach.service.LeaveService;
import com.kingsmen.kingsreach.util.ResponseStructure;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(allowCredentials = "true", origins = "https://peppy-kitsune-9771a0.netlify.app/")
@RestController
public class LeaveController {

	@Autowired
	private LeaveService leaveservice;

	@PostMapping(value = "/applyLeave")
	private ResponseEntity<ResponseStructure<Leave>> applyLeave(@RequestBody Leave leave) {
		return leaveservice.applyLeave(leave);
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

	@GetMapping("/employeePendingLeave")
	public ResponseEntity<ResponseStructure<Map<String, Integer>>> getRemainingLeave(@RequestParam String employeeId) {
		return leaveservice.getRemainingLeave(employeeId);
	}

	@GetMapping(value = "/findAbsentEmployees")
	private ResponseEntity<ResponseStructure<List<Leave>>> findAbsentEmployees() {
		return leaveservice.findAbsentEmployees();
	}

	@GetMapping(value = "/fetchLeaveBasedOnDepartment")
	private ResponseEntity<ResponseStructure<List<Leave>>> fetchLeaveBasedOnDepartment(@RequestParam Department department) {
		return leaveservice.findAbsentEmployees(department);
	}
	
	@GetMapping(value = "/fetchLeaveBasedOnManagerEmployee")
	private ResponseEntity<ResponseStructure<List<Leave>>> fetchLeaveBasedOnManagerEmployee(@RequestParam String employeeId){
		return leaveservice.fetchLeaveBasedOnManagerEmployee(employeeId);
	}
	
}
