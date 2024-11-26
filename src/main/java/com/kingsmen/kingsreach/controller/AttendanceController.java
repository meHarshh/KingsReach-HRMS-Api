package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.service.AttendanceService;

@RestController
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	
	@PostMapping(value = "/attendanceCalculator")
	private Attendance addAttendance(@RequestBody Attendance attendance) {
		return attendanceService.addAttendance(attendance);
	}
	
}
