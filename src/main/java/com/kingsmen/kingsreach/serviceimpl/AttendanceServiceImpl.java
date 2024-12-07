package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.repo.AttendanceRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.service.AttendanceService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Override
	public ResponseEntity<ResponseStructure<Attendance>> addAttendance(Attendance attendance) {

		Optional<Employee> byEmployeeId = Optional.of(employeeRepo.findByEmployeeId(attendance.getEmployeeId())
				.orElseThrow(() -> new RuntimeException("No value present.")));

		Employee employee = byEmployeeId.get();
		//attendance.setEmployee(employee);
		attendance.setFirstPunchIn(attendance.getFirstPunchIn());

		attendance.setEmployee(employee);
		attendanceRepo.save(attendance);

		ResponseStructure<Attendance> responseStructure = new ResponseStructure<Attendance>();

		String message = "Attendence Of " + attendance.getEmployee() + " is recorded.";
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(attendance);

		return new ResponseEntity<ResponseStructure<Attendance>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public Attendance getAttendance(String employeeId) {
		return attendanceRepo.findByEmployeeId(employeeId).orElseThrow(()-> new RuntimeException());
	}

	
}
