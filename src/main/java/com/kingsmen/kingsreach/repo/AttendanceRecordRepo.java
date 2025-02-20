package com.kingsmen.kingsreach.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.AttendanceRecord;

public interface AttendanceRecordRepo extends JpaRepository<AttendanceRecord, Integer>{

	AttendanceRecord findByEmployeeIdAndAttendanceDate(String employeeId, LocalDate today);

	List<AttendanceRecord> findByAttendanceDate(LocalDate now);

}
