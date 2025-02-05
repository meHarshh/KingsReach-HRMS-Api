package com.kingsmen.kingsreach.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.AttendanceRecord;

public interface AttendanceRecordRepo extends JpaRepository<AttendanceRecord, Integer>{

	AttendanceRecord findByEmployeeId(String employeeId);

}
