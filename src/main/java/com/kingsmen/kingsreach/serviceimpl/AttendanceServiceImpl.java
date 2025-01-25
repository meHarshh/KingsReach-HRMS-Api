package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Attendance;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.exceptions.EmployeeIdNotExistsException;
import com.kingsmen.kingsreach.repo.AttendanceRepo;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
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

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<Attendance>> addAttendance(Attendance attendance) {

		Optional<Employee> byEmployeeId = Optional.of(employeeRepo.findByEmployeeId(attendance.getEmployeeId())
				.orElseThrow(() -> new EmployeeIdNotExistsException("No value present with the ID.")));

		Employee employee = byEmployeeId.get();
		// attendance.setEmployee(employee);
		attendance.setFirstPunchIn(attendance.getFirstPunchIn());
		attendance.setLastPunchOut(attendance.getLastPunchOut());
		attendance.setAttendanceDate(LocalDate.now());
		attendance.setEmployee(employee);
		attendance.setWorkmode(attendance.getWorkmode());
		attendance.setLocation(attendance.getLocation());
		attendanceRepo.save(attendance);

		ResponseStructure<Attendance> responseStructure = new ResponseStructure<Attendance>();

		String message = "Attendence Of " + attendance.getEmployeeName() + " is recorded.";
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(attendance);

		// Notification code
		Notification notify = new Notification();
		notify.setEmployeeId(attendance.getEmployeeId());
		notify.setMessage(message);
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Attendance>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<Attendance>> getAttendance(String employeeId) {
		Attendance attendance = attendanceRepo.findByEmployeeId(employeeId)
				.orElseThrow(() -> new EmployeeIdNotExistsException(
						"No value Present with the assosiated ID.Enter the valid Employee ID"));

		ResponseStructure<Attendance> responseStructure = new ResponseStructure<Attendance>();

		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Attendence detail fetched successfully.");
		responseStructure.setData(attendance);

		return new ResponseEntity<ResponseStructure<Attendance>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Attendance>>> getAttendanceForMonth() {
		ResponseStructure<List<Attendance>> responseStructure = new ResponseStructure<List<Attendance>>();
		responseStructure.setData(attendanceRepo.findAll());
		responseStructure.setMessage("All attendance fetched");
		responseStructure.setStatusCode(HttpStatus.OK.value());

		return new ResponseEntity<ResponseStructure<List<Attendance>>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Attendance>> getAttendenceForDate(String employeeId, LocalDate date) {
		Attendance attendanceOptional = attendanceRepo.findByEmployeeIdAndAttendanceDate(employeeId, date);

		ResponseStructure<Attendance> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Attendence of " + date + " fetched successfully.");
		responseStructure.setData(attendanceOptional);

		return new ResponseEntity<ResponseStructure<Attendance>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Object>> getAttendanceDetails() {
		List<Onsite> onsites = onsiteRepo.findByDate(LocalDate.now());
		ArrayList<Onsite> arrayList = new ArrayList<Onsite>();

		for (Onsite onsite : onsites) {
			if (onsite.getDate() == LocalDate.now()) {
				arrayList.add(onsite);
			}
		}

		List<Attendance> attendances = attendanceRepo.findByDate(LocalDate.now());

		int totalEmployees = attendances.size() + onsites.size();

		int inOffice = attendances.size() - arrayList.size();

		int[] count = { inOffice, onsites.size(), totalEmployees };

		ResponseStructure<Object> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Employee strength details fetched successfully");
		responseStructure.setMessage("inOffice ,onsite ,totalEmployees");
		responseStructure.setData(count);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

}
