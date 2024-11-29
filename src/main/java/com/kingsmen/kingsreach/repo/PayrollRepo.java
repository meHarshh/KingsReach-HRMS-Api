package com.kingsmen.kingsreach.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Payroll;

public interface PayrollRepo extends JpaRepository<Payroll, Integer>{

	Payroll findByEmployeeId(String employeeId);

}
