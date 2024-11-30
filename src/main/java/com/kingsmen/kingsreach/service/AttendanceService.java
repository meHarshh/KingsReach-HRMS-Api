package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface AttendanceService {

	ResponseEntity<ResponseStructure<Attendance>> addAttendance(Attendance attendance);

	Attendance getAttendance(String employeeId);


//	Attendance getAttendance(String employeeId, LocalDate date);

}
