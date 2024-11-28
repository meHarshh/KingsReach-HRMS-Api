package com.kingsmen.kingsreach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kingsmen.kingsreach.entity.Payroll;

public interface PayrollRepo extends JpaRepository<Payroll, Integer>{

	//Payroll findPayrollByEmployeeId(String employeeId);

	@Query("SELECT p FROM Payroll p WHERE p.employeeId = :employeeId")
	Payroll findPayrollByEmployeeId(@Param("employeeId") String employeeId);

	Payroll findByEmployeeId(String employeeId);


}
