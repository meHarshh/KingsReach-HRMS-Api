package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Attendance;

public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {

	Optional<Attendance> findByEmployeeId(String employeeId);


//	void findByEmployeeIdAndDate(String employeeId, LocalDate date);




}
