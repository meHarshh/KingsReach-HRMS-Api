package com.kingsmen.kingsreach.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Attendance;

public interface AttendanceRepo extends JpaRepository<Attendance, Integer> {

}
