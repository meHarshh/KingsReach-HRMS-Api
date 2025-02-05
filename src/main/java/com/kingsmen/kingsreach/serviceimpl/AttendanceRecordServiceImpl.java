package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.AttendanceRecord;
import com.kingsmen.kingsreach.repo.AttendanceRecordRepo;
import com.kingsmen.kingsreach.service.AttendanceRecordService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService{
	
	@Autowired
	private AttendanceRecordRepo attendanceRecordRepo;

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> saveAttendanceRecord(AttendanceRecord attendanceRecord) {
		attendanceRecord.setAttendanceDate(LocalDate.now());
		AttendanceRecord record = attendanceRecordRepo.save(attendanceRecord);
		
		ResponseStructure<AttendanceRecord> responseStructure = new ResponseStructure<AttendanceRecord>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(record);
		responseStructure.setMessage("Attendance Recorded successully");
		
		return new ResponseEntity<ResponseStructure<AttendanceRecord>>(responseStructure,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<AttendanceRecord>> getAttendanceDetail(String employeeId) {
		
		AttendanceRecord record = attendanceRecordRepo.findByEmployeeId(employeeId);
		
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
