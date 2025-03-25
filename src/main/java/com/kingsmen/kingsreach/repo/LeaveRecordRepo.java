package com.kingsmen.kingsreach.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.LeaveRecord;

public interface LeaveRecordRepo extends JpaRepository<LeaveRecord, Integer>{

	List<LeaveRecord> findByEmployeeId(String employeeId);
	
}
