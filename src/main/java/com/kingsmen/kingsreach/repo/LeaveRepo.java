package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Leave;

public interface LeaveRepo extends JpaRepository<Leave, Integer>{

	Optional<Leave> findByEmployeeId(String employeeId);

}
