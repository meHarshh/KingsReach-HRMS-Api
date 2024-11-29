package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.service.AttendanceService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
@RestController
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	
	@PostMapping(value = "/attendanceCalculator")
	private ResponseEntity<ResponseStructure<Attendance>> addAttendance(@RequestBody Attendance attendance) {
		return attendanceService.addAttendance(attendance);
	}
	
}
