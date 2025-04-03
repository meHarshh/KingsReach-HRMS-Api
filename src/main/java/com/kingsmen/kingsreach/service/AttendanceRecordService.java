package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.AttendanceRecord;
import com.kingsmen.kingsreach.helper.AttendanceRecordHelper;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface AttendanceRecordService {

	ResponseEntity<ResponseStructure<AttendanceRecord>> saveAttendanceRecord(AttendanceRecord attendanceRecord);

	ResponseEntity<ResponseStructure<AttendanceRecord>> getAttendanceDetail(String employeeId);

	ResponseEntity<ResponseStructure<AttendanceRecord>> changeRecordStatus(AttendanceRecord attendanceRecord);

	ResponseEntity<ResponseStructure<List<AttendanceRecordHelper>>> getPunchInRecords();

}
