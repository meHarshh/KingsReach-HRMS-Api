package com.kingsmen.kingsreach.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Payroll;

public interface PayrollRepo extends JpaRepository<Payroll, Integer>{

	Payroll findByEmployeeId(String employeeId);

	List<Payroll> findByDate(LocalDate date);

}
