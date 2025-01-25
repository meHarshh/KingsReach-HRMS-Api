package com.kingsmen.kingsreach.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Attendance;

public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {

	Optional<Attendance> findByEmployeeId(String employeeId);


	Attendance findByEmployeeIdAndAttendanceDate(String employeeId, LocalDate attendanceDate);

	List<Attendance> findByAttendanceDate(LocalDate now);

	Attendance findByEmployeeIdAndAttendanceDateBetween(String employeeId, LocalDate fromDate, LocalDate toDate);




}
