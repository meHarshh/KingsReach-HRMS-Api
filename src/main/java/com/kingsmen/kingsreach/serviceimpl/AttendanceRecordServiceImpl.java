package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.entity.AttendanceRecord;
import com.kingsmen.kingsreach.repo.AttendanceRecordRepo;
import com.kingsmen.kingsreach.repo.AttendanceRepo;
import com.kingsmen.kingsreach.service.AttendanceRecordService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService{
	
	@Autowired
	private AttendanceRecordRepo attendanceRecordRepo;
	
	@Autowired
	private AttendanceRepo attendanceRepo;

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> saveAttendanceRecord(AttendanceRecord attendanceRecord) {
		
		//saveAttendance(attendanceRecord);
		
		attendanceRecord.setAttendanceDate(LocalDate.now());
		AttendanceRecord record = attendanceRecordRepo.save(attendanceRecord);
		
		ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<AttendanceRecord>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(record);
		responseStructure.setMessage("Attendance Recorded successully");
		
		return new ResponseEntity<ResponseStructure<AttendanceRecord>>(responseStructure,HttpStatus.OK);
	}

	private void saveAttendance(AttendanceRecord attendanceRecord) {
		Attendance attendance = new Attendance();
		attendance.setEmployeeId(attendanceRecord.getEmployeeId());
		attendance.setFirstPunchIn(attendanceRecord.getFirstPunchIn());
		attendance.setLastPunchOut(attendanceRecord.getLastPunchOut());
		attendance.setLocation(attendanceRecord.getLocation());
		attendance.setWorkMode(attendanceRecord.getWorkMode());
		attendance.setTotalBreakMinutes(attendanceRecord.getTotalBreakMinutes());
		attendance.setTotalWorkMinutes(attendanceRecord.getTotalWorkMinutes());
		attendance.setAttendanceDate(attendanceRecord.getAttendanceDate());
		
		attendanceRepo.save(attendance);	
	}

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> getAttendanceDetail(String employeeId) {
		LocalDate today = LocalDate.now();
		AttendanceRecord record =attendanceRecordRepo.findByEmployeeIdAndAttendanceDate(employeeId,today);
			
		ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<AttendanceRecord>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(record);
		responseStructure.setMessage("Attendance Recorded fetched successully");
		
		return new ResponseEntity<ResponseStructure<AttendanceRecord>>(responseStructure,HttpStatus.OK);
}

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> changeRecordStatus(AttendanceRecord attendanceRecord) {
		AttendanceRecord record = attendanceRecordRepo.findById(attendanceRecord.getAttendanceRecordId())
				.orElseThrow(() -> new RuntimeException("Attendence Record not found with Id"));
		
		saveAttendance(attendanceRecord);
		record.setLastPunchOut(attendanceRecord.getLastPunchOut());
		record.setTotalBreakMinutes(attendanceRecord.getTotalBreakMinutes());
		record.setTotalWorkMinutes(attendanceRecord.getTotalWorkMinutes());
		
		AttendanceRecord record2 = attendanceRecordRepo.save(record);
		
		ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<AttendanceRecord>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(record2);
		responseStructure.setMessage("Attendance record updated successully");
		
		return new ResponseEntity<ResponseStructure<AttendanceRecord>>(responseStructure,HttpStatus.OK);
		
	}

}
