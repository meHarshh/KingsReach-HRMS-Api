package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.Reimbursement;
import com.kingsmen.kingsreach.enums.ReimbursementStatus;
import com.kingsmen.kingsreach.exceptions.ReimbursementNotFoundException;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.ReimbursementRepo;
import com.kingsmen.kingsreach.service.ReimbursementService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private ReimbursementRepo reimbursementRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<Reimbursement>> reimbursement(Reimbursement reimbursement) {

		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(reimbursement.getEmployeeId());
		reimbursement.setEmployee(byEmployeeId.get());

		int[] projects = reimbursement.getProjects();
		if (reimbursement.getPurpose() == "Petrol Allowance") {
			int amount = 0;
			int totalKms = 0;
			for (int i = 0; i < projects.length; i++) {
				totalKms += projects[i];
			}

			totalKms = totalKms * 2;
			amount = totalKms / projects.length * 5;
			reimbursement.setAmount(amount);
		}

		reimbursement.setReimbursementStatus(ReimbursementStatus.PENDING);

		reimbursementRepo.save(reimbursement);

		ResponseStructure<Reimbursement> responseStructure = new ResponseStructure<Reimbursement>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(reimbursement.getEmployeeName() + " added successfully.");
		responseStructure.setData(reimbursement);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(reimbursement.getEmployeeId());
		notify.setMessage(reimbursement.getEmployeeName() + " added successfully.");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Reimbursement>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<Reimbursement>> changeReimbursementStatus(Reimbursement reimbursement) {
		//		String employeeId = reimbursement.getEmployeeId();
		//		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employeeId);
		//		Employee employee = byEmployeeId.get();
		//		System.out.println(employee.getFirstName());
		//
		//		Reimbursement reimbursement2 = employee.getReimbursement();
		//		reimbursement2 = reimbursementRepo.findById(reimbursement2.getReimbursementId()).orElseThrow(()->new RuntimeException("No employees found"));
		//		reimbursement2.setReimbursementStatus(reimbursement.getReimbursementStatus());
		//
		//		Reimbursement reimbursement3 = reimbursementRepo.save(reimbursement2);

		int reimbursementId = reimbursement.getReimbursementId();
		Reimbursement reimbursement2 = reimbursementRepo.findById(reimbursementId)
				.orElseThrow(() -> new ReimbursementNotFoundException("Reimbursement with ID " + reimbursementId + " not found"));

		reimbursement2.setReimbursementStatus(reimbursement.getReimbursementStatus());

		Reimbursement reimbursement3 = reimbursementRepo.save(reimbursement2);

		ResponseStructure<Reimbursement> responseStructure = new ResponseStructure<Reimbursement>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(reimbursement.getEmployeeName() + " updated successfully.");
		responseStructure.setData(reimbursement3);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(reimbursement.getEmployeeId());
		notify.setMessage(reimbursement.getEmployeeName() + " updated successfully.");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<Reimbursement>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Reimbursement>>> findReimbursementDetail() {
		List<Reimbursement> list = reimbursementRepo.findAll();

		ResponseStructure<List<Reimbursement>> responseStructure = new ResponseStructure<List<Reimbursement>>();
		responseStructure.setData(list);
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("The employees Reimbursement data is fetched");

		//Notification code 
		Notification notify = new Notification();
		notify.setMessage("The employees Reimbursement data is fetched");
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<List<Reimbursement>>>(responseStructure, HttpStatus.OK);
	}
}

