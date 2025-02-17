package com.kingsmen.kingsreach.serviceimpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.entity.AttendanceRecord;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.exceptions.EmployeeIdNotExistsException;
import com.kingsmen.kingsreach.repo.AttendanceRecordRepo;
import com.kingsmen.kingsreach.repo.AttendanceRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.AttendanceRecordService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

	@Autowired
	private AttendanceRecordRepo attendanceRecordRepo;

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> saveAttendanceRecord(AttendanceRecord attendanceRecord) {

		attendanceRecord.setAttendanceDate(LocalDate.now());
		AttendanceRecord record = attendanceRecordRepo.save(attendanceRecord);

		ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<AttendanceRecord>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(record);
		responseStructure.setMessage("Attendance Recorded successully");

		return new ResponseEntity<ResponseStructure<AttendanceRecord>>(responseStructure, HttpStatus.OK);
	}

	private void saveAttendance(AttendanceRecord attendanceRecord) {
		Optional<Employee> byEmployeeId = Optional.of(employeeRepo.findByEmployeeId(attendanceRecord.getEmployeeId())
				.orElseThrow(() -> new EmployeeIdNotExistsException("No value present with the ID.")));

		Employee employee = byEmployeeId.get();

		Attendance attendance = new Attendance();
		attendance.setEmployeeId(attendanceRecord.getEmployeeId());
		attendance.setFirstPunchIn(attendanceRecord.getFirstPunchIn());
		attendance.setLastPunchOut(attendanceRecord.getLastPunchOut());
		attendance.setLocation(attendanceRecord.getLocation());
		attendance.setWorkMode(attendanceRecord.getWorkMode());
		attendance.setTotalBreakMinutes(attendanceRecord.getTotalBreakMinutes());
		attendance.setTotalWorkMinutes(attendanceRecord.getTotalWorkMinutes());
		attendance.setAttendanceDate(LocalDate.now());
		attendance.setEmployeeName(employee.getName());

		attendanceRepo.save(attendance);
	}

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> getAttendanceDetail(String employeeId) {
		LocalDate today = LocalDate.now();
		AttendanceRecord record = attendanceRecordRepo.findByEmployeeIdAndAttendanceDate(employeeId, today);

		ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<AttendanceRecord>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(record);
		responseStructure.setMessage("Attendance Recorded fetched successully");

		return new ResponseEntity<ResponseStructure<AttendanceRecord>>(responseStructure, HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	public ResponseEntity<ResponseStructure<AttendanceRecord>> changeRecordStatus(AttendanceRecord attendanceRecord) {
	    AttendanceRecord record = attendanceRecordRepo.findById(attendanceRecord.getAttendanceRecordId())
	            .orElseThrow(() -> new RuntimeException("Attendance Record not found with Id"));

	    LocalDateTime punchInTime = record.getFirstPunchIn(); // Get the first punch-in from the record
	    LocalDateTime now = LocalDateTime.now();
	    
	    if (punchInTime != null) {
	        Duration workedDuration = Duration.between(punchInTime, now).minus(Duration.ofMinutes(attendanceRecord.getTotalBreakMinutes()));
	        
	        // Auto punch-out logic after 12 hours if not already punched out
	        if (record.getLastPunchOut() == null && now.isAfter(punchInTime.plusHours(12))) {
	            record.setLastPunchOut(punchInTime.plusHours(12));
	        } else if (attendanceRecord.getLastPunchOut() != null) {
	            record.setLastPunchOut(attendanceRecord.getLastPunchOut());
	        }
	    }

	    record.setTotalBreakMinutes(attendanceRecord.getTotalBreakMinutes());
	    record.setTotalWorkMinutes(attendanceRecord.getTotalWorkMinutes());

	    AttendanceRecord updatedRecord = attendanceRecordRepo.save(record);

	    if (record.getLastPunchOut() != null) {
	        saveAttendance(record);
	    }

	    ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatusCode(HttpStatus.OK.value());
	    responseStructure.setData(updatedRecord);
	    responseStructure.setMessage("Attendance record updated successfully");

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	
}
