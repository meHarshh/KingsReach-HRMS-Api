package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.exceptions.EmployeeIdNotExistsException;
import com.kingsmen.kingsreach.repo.AttendanceRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.OnsiteRepo;
import com.kingsmen.kingsreach.service.AttendanceService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class AttendanceServiceImpl implements AttendanceService {

	@Autowired
	private AttendanceRepo attendanceRepo;

	@Autowired
	private EmployeeRepo employeeRepo;
	
	@Autowired
	private OnsiteRepo onsiteRepo;

	@Override
	public ResponseEntity<ResponseStructure<Attendance>> addAttendance(Attendance attendance) {

		Optional<Employee> byEmployeeId = Optional.of(employeeRepo.findByEmployeeId(attendance.getEmployeeId())
				.orElseThrow(() -> new EmployeeIdNotExistsException("No value present with the ID.")));

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
	public ResponseEntity<ResponseStructure<Attendance>> getAttendance(String employeeId) {
		Attendance attendance = attendanceRepo.findByEmployeeId(employeeId).orElseThrow(()-> new EmployeeIdNotExistsException(
				"No value Present with the assosiated ID.Enter the valid Employee ID"));

		ResponseStructure<Attendance> responseStructure = new ResponseStructure<Attendance>();

		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Attendence detail fetched successfully.");
		responseStructure.setData(attendance);

		return new ResponseEntity<ResponseStructure<Attendance>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Attendance>>> getAttendanceForMonth() {
		ResponseStructure<List<Attendance>> responseStructure= new ResponseStructure<List<Attendance>>();
		responseStructure.setData(attendanceRepo.findAll());
		responseStructure.setMessage("All attendance fetched");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<List<Attendance>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Attendance>> getAttendenceForDate(String employeeId, LocalDate date) {
		Attendance attendanceOptional = attendanceRepo.findByEmployeeIdAndDate(employeeId, date);
		
		ResponseStructure<Attendance> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Attendence of employee " + date + " fetched successfully.");
		responseStructure.setData(attendanceOptional);

		return new ResponseEntity<ResponseStructure<Attendance>>(responseStructure,HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<ResponseStructure<List<Attendance>>> getAttendanceDetails() {
	    List<Employee> employeesInOffice = findEmployeeInOffice(LocalDate.now());

	    List<Onsite> onsiteEmployees = findEmployeeOnsite(LocalDate.now());

	    List<Attendance> attendanceDetails = new ArrayList<>();

	    for (Employee employee : employeesInOffice) {
	        Attendance attendance = getAttendanceForEmployee(employee.getEmployeeId(), LocalDate.now());
	        if (attendance != null) {
	            attendanceDetails.add(attendance);
	        }
	    }

	    for (Onsite onsite : onsiteEmployees) {
	        Attendance attendance = getAttendanceForEmployee(onsite.getEmployeeId(), LocalDate.now());
	        if (attendance != null) {
	            attendanceDetails.add(attendance);
	        }
	    }

	    ResponseStructure<List<Attendance>> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatusCode(HttpStatus.OK.value());
	    responseStructure.setMessage("Attendance details fetched successfully.");
	    responseStructure.setData(attendanceDetails);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	private List<Onsite> findEmployeeOnsite(LocalDate date) {
	    return onsiteRepo.findByDate(date); 
	}

	private List<Employee> findEmployeeInOffice(LocalDate date) {
	    return employeeRepo.findByDate(date); 
	}

	private Attendance getAttendanceForEmployee(String employeeId, LocalDate date) {
	    return attendanceRepo.findByEmployeeIdAndDate(employeeId, date);
	}

	
	
}
