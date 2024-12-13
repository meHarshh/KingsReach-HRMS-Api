package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface AttendanceService {

	ResponseEntity<ResponseStructure<Attendance>> addAttendance(Attendance attendance);

	ResponseEntity<ResponseStructure<Attendance>> getAttendance(String employeeId);

	ResponseEntity<ResponseStructure<List<Attendance>>> getAttendanceForMonth();



}
