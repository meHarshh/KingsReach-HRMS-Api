package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.LeaveRecord;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.repo.LeaveRecordRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.service.LeaveRecordService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class LeaveRecordServiceImpl implements LeaveRecordService {
	@Autowired
	private LeaveRecordRepo leaveRecordRepo;

	@Autowired
	private LeaveRepo leaveRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<LeaveRecord>> saveEmployeeLeaveRecord(LeaveRecord leaveRecord) {
		Optional<Leave> byEmployeeId = leaveRepo.findByEmployeeId(leaveRecord.getEmployeeId());

		Employee employee = byEmployeeId.get().getEmployee();

		LocalDate fromDate = leaveRecord.getFromDate();
		LocalDate toDate = leaveRecord.getToDate();

		int between = (int) ChronoUnit.DAYS.between(fromDate, toDate);
		leaveRecord.setNoOfDays(between);
		leaveRecord.setEmployee(employee);

		leaveRecord = leaveRecordRepo.save(leaveRecord);

		ResponseStructure<LeaveRecord> responseStructure = new ResponseStructure<LeaveRecord>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(leaveRecord.getEmployeeName() + " leave records are added");
		responseStructure.setData(leaveRecord);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(leaveRecord.getEmployeeId());
		notify.setMessage(leaveRecord.getEmployeeName() + " leave records are added");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<LeaveRecord>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<LeaveRecord>>> getEmployeesLeaveRecords() {
		List<LeaveRecord> list = leaveRecordRepo.findAll();

		ResponseStructure<List<LeaveRecord>> responseStructure = new ResponseStructure<List<LeaveRecord>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("EmployeeLeave Record Fetched Successfully.");
		responseStructure.setData(list);

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("EmployeeLeave Record Fetched Successfully.");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<List<LeaveRecord>>>(responseStructure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<List<LeaveRecord>>> getEmployeeLeaveRecord(String employeeId) {
		List<LeaveRecord> optional = leaveRecordRepo.findByEmployeeId(employeeId);
		List<LeaveRecord> list = new ArrayList<LeaveRecord>();
		for(LeaveRecord lists : optional) {
			list.add(lists);
		}

		ResponseStructure<List<LeaveRecord>> responseStructure = new ResponseStructure<>();
		responseStructure.setMessage("Leave record retrieved successfully");
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setData(list);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(employeeId);
		notify.setMessage("Leave record retrieved successfully");
		notificationRepo.save(notify);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

}
