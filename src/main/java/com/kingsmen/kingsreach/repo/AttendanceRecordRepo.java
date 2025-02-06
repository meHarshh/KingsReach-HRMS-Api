package com.kingsmen.kingsreach.repo;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.AttendanceRecord;

public interface AttendanceRecordRepo extends JpaRepository<AttendanceRecord, Integer>{

	AttendanceRecord findByEmployeeIdAndAttendanceDate(String employeeId, LocalDate today);

}
