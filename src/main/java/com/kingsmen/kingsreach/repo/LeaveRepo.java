package com.kingsmen.kingsreach.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;

public interface LeaveRepo extends JpaRepository<Leave, Integer>{

	Optional<Leave> findByEmployeeId(String employeeId);

	List<Leave> findByEmployee(Employee employee);

}
