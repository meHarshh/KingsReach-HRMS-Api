package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.repo.AttendanceRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public Attendance addAttendance(Attendance attendance) {
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(attendance.getEmpId());
		Employee employee = byEmployeeId.get();
		attendance.setEmployee(employee);
		return attendanceRepo.save(attendance);
	}

}
