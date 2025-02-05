package com.kingsmen.kingsreach.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.AttendanceRecord;

public interface AttendanceRecordRepo extends JpaRepository<AttendanceRecord, Integer>{

	List<AttendanceRecord> findByEmployeeId(String employeeId);

}
