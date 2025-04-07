package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.ResignationDetail;
import com.kingsmen.kingsreach.entity.ResignedEmployee;
import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.exceptions.ResignationIdNotFoundException;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.ResignationDetailRepo;
import com.kingsmen.kingsreach.repo.ResignedEmployeeRepo;
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
	
	@Autowired
	private ResignedEmployeeRepo resignedEmployeeRepo;

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
		notify.setCreatedAt(LocalDateTime.now());
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

		return new ResponseEntity<ResponseStructure<List<ResignationDetail>>>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<ResignationDetail>> changeResignationStatus(int resignationId,ResignationDetail resignationDetail) {

		ResignationDetail existingResignation = resignationDetailRepo.findById(resignationId)
				.orElseThrow(() -> new ResignationIdNotFoundException("Resignation with ID " + resignationId + " not found"));
		
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(existingResignation.getEmployeeId());
		Employee employee = byEmployeeId.get();

		saveResignedEmployeeDetail(existingResignation);
		
		existingResignation.setResignationStatus(resignationDetail.getResignationStatus());
		ResignationDetail updatedDetail = resignationDetailRepo.save(existingResignation);
		
		employeeRepo.delete(employee);
		
		ResponseStructure<ResignationDetail> responseStructure = new ResponseStructure<>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(existingResignation.getName() + " Resignation status updated successfully.");
		responseStructure.setData(updatedDetail);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(resignationDetail.getEmployeeId());
		notify.setMessage(existingResignation.getName() + " Resignation status updated successfully.");
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}

	private void saveResignedEmployeeDetail(ResignationDetail existingResignation) {
		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(existingResignation.getEmployeeId());
		Employee employee = byEmployeeId.get();
		
		ResignedEmployee resignedEmployee = new ResignedEmployee();
		
		resignedEmployee.setEmployeeId(existingResignation.getEmployeeId());
		resignedEmployee.setFirstName(employee.getFirstName());
		resignedEmployee.setLastName(employee.getLastName());
		resignedEmployee.setUserName(employee.getUserName());
		resignedEmployee.setName(existingResignation.getName());
		resignedEmployee.setRole(employee.getRole());
		resignedEmployee.setOfficialEmail(employee.getOfficialEmail());
		resignedEmployee.setPassword(employee.getPassword());
		resignedEmployee.setConfirmPassword(employee.getConfirmPassword());
		resignedEmployee.setJoiningDate(employee.getJoiningDate());
		resignedEmployee.setPhoneNumber(employee.getPhoneNumber());
		resignedEmployee.setDepartment(employee.getDepartment());
		resignedEmployee.setAadharCardNumber(employee.getAadharCardNumber());
		resignedEmployee.setPanCardNumber(employee.getPanCardNumber());
		resignedEmployee.setDob(employee.getDob());
		resignedEmployee.setBloodGroup(employee.getBloodGroup());
		resignedEmployee.setFatherName(employee.getFatherName());
		resignedEmployee.setMotherName(employee.getMotherName());
		resignedEmployee.setFatherContactNumber(employee.getFatherContactNumber());
		resignedEmployee.setMotherNumber(employee.getMotherNumber());
		resignedEmployee.setPermanentAddress(employee.getPermanentAddress());
		resignedEmployee.setEmergencyContact(employee.getEmergencyContact());
		resignedEmployee.setOfficialNumber(employee.getOfficialNumber());
		resignedEmployee.setEmergencyContactName(employee.getEmergencyContactName());
		resignedEmployee.setEmergencyContactRelation(employee.getEmergencyContactRelation());
		resignedEmployee.setLocalAddress(employee.getLocalAddress());
		resignedEmployee.setBankName(employee.getBankName());
		resignedEmployee.setBranchName(employee.getBranchName());
		resignedEmployee.setAccountNumber(employee.getAccountNumber());
		resignedEmployee.setIfscCode(employee.getIfscCode());
		resignedEmployee.setEmail(employee.getEmail());
		resignedEmployee.setGender(employee.getGender());
		resignedEmployee.setDate(employee.getDate());
		resignedEmployee.setUanNumber(employee.getUanNumber());
		resignedEmployee.setCasualLeaveBalance(employee.getCasualLeaveBalance());
		resignedEmployee.setSickLeaveBalance(employee.getSickLeaveBalance());
		resignedEmployee.setPaidLeaveBalance(employee.getPaidLeaveBalance());
		resignedEmployee.setEmergencyDeathLeave(employee.getEmergencyDeathLeave());
		resignedEmployee.setExperience(employee.getExperience());
		resignedEmployee.setUpdatedAt(employee.getUpdatedAt());
		resignedEmployee.setCreatedAt(employee.getCreatedAt());
		
		resignedEmployeeRepo.save(resignedEmployee);	
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

		return new ResponseEntity<ResponseStructure<Map<String, Object>>>(responseStructure, HttpStatus.OK);
	}

}


