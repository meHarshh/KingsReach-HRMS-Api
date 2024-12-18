package com.kingsmen.kingsreach.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.ResignationDetail;
import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.exceptions.ResignationIdNotFoundException;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.ResignationDetailRepo;
import com.kingsmen.kingsreach.repo.TerminationDetailRepo;
import com.kingsmen.kingsreach.service.ResignationDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class ResignationDetailServiceImpl implements ResignationDetailService {

	@Autowired
	private ResignationDetailRepo resignationDetailRepo;

	@Autowired
	private TerminationDetailRepo terminationDetailRepo;

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<ResignationDetail>> resignationDetail(ResignationDetail resignationDetail) {

		resignationDetail=resignationDetailRepo.save(resignationDetail);

		ResponseStructure<ResignationDetail> responseStructure=new ResponseStructure<ResignationDetail>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage("Employee ID :" + resignationDetail.getEmployeeId() + " " + resignationDetail.getName() +" resigned.");
		responseStructure.setData(resignationDetail);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(resignationDetail.getEmployeeId());
		notify.setMessage("Employee ID :" + resignationDetail.getEmployeeId() + " " + resignationDetail.getName() +" resigned.");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<ResignationDetail>>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ResignationDetail>>> getResignationDetails() {
		List<ResignationDetail> list = resignationDetailRepo.findAll();

		ResponseStructure<List<ResignationDetail>> responseStructure=new ResponseStructure<List<ResignationDetail>>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage("Resignation Details fetched successfully.");
		responseStructure.setData(list);

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("Resignation Details fetched successfully.");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<List<ResignationDetail>>>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<ResignationDetail>> changeResignationStatus(int resignationId,ResignationDetail resignationDetail) {


		ResignationDetail existingResignation = resignationDetailRepo.findById(resignationId)
				.orElseThrow(() -> new ResignationIdNotFoundException("Resignation with ID " + resignationId + " not found"));

		existingResignation.setResignationStatus(resignationDetail.getResignationStatus());
		ResignationDetail updatedDetail = resignationDetailRepo.save(existingResignation);

		ResponseStructure<ResignationDetail> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Resignation status updated successfully.");
		responseStructure.setData(updatedDetail);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(resignationDetail.getEmployeeId());
		notify.setMessage("Resignation status updated successfully.");
		notificationRepo.save(notify);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<Map<String, Object>>> getAllDetails() {
		List<Employee> employees = employeeRepo.findAll();

		List<TerminationDetail> terminationDetails = terminationDetailRepo.findAll();

		List<ResignationDetail> resignationDetails = resignationDetailRepo.findAll();

		Map<String, Object> map = new HashMap<>();
		map.put("employees", employees);
		map.put("terminationDetails", terminationDetails);
		map.put("resignationDetails", resignationDetails);

		map.put("employeesSize", employees.size());
		map.put("terminationDetailsSize", terminationDetails.size());
		map.put("resignationDetailsSize", resignationDetails.size());

		ResponseStructure<Map<String, Object>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Data retrieved successfully");
		responseStructure.setData(map);

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("Data retrieved successfully");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Map<String, Object>>>(responseStructure, HttpStatus.OK);
	}

}


