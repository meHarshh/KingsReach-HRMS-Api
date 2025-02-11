package com.kingsmen.kingsreach.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.service.AttendanceService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://hrms.kingsmenrealty.com/")
@RestController
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	@PostMapping(value = "/attendanceCalculator")
	private ResponseEntity<ResponseStructure<Attendance>> addAttendance(@RequestBody Attendance attendance) {
		return attendanceService.addAttendance(attendance);
	}

	@GetMapping(value = "/getEmployeeAttendance/{employeeId}")
	private ResponseEntity<ResponseStructure<Attendance>> getAttendance(@PathVariable String employeeId) {
		return attendanceService.getAttendance(employeeId);
	}
	
	@GetMapping(value = "/getAttendanceForMonth")
	public ResponseEntity<ResponseStructure<List<Attendance>>> getAttendanceForMonth() {
		return attendanceService.getAttendanceForMonth();
	}
	
	@GetMapping(value = "/getAttendenceOfSpecificDate")
	private ResponseEntity<ResponseStructure<Attendance>> getAttendenceForDate(@RequestParam  String employeeId, @RequestParam LocalDate attendanceDate){
		return attendanceService.getAttendenceForDate(employeeId, attendanceDate);
	}
	
	@GetMapping(value = "/getAttendenceDetails")
	private ResponseEntity<ResponseStructure<Object>> getAttendenceDetails(){
		return attendanceService.getAttendanceDetails();
	}
	
	@GetMapping(value = "/getAttedanceBetween")
	private ResponseEntity<ResponseStructure<List<Attendance>>> getAttendanceBetween
	(@RequestParam String employeeId, @RequestParam LocalDate fromDate,@RequestParam LocalDate toDate){
		return attendanceService.getAttendanceBetween(employeeId,fromDate,toDate);
	}
	
	@GetMapping(value = "/daysOfAttendance")
	private ResponseEntity<ResponseStructure<Map<String, List<Attendance>>>> getAttendanceForDays(){
		return attendanceService.getAttendanceForDays();
	}
	
}
