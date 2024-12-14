package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.LeaveRecord;

public interface LeaveRecordRepo extends JpaRepository<LeaveRecord, Integer>{

	Optional<LeaveRecord> findByEmployeeId(String employeeId);



}
