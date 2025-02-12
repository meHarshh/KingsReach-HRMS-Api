package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Reimbursement;
import com.kingsmen.kingsreach.service.ReimbursementService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(origins = {"http://hrms.kingsmenrealty.com/", "http://localhost:5173/"})
@RestController
public class ReimbursementController {

	@Autowired
	private ReimbursementService reimbursementService;

	@PostMapping(value = "/reimbursement")
	private ResponseEntity<ResponseStructure<Reimbursement>> reimbursement(@RequestBody Reimbursement reimbursement) {
		return reimbursementService.reimbursement(reimbursement);
	}

	@PutMapping(value = "/changeReimbursementStatus")
	private ResponseEntity<ResponseStructure<Reimbursement>> changeReimbursementStatus(@RequestBody Reimbursement reimbursement) {
		return reimbursementService.changeReimbursementStatus(reimbursement);
	}
	
	@GetMapping(value = "/findReimbursementDetail")
	private ResponseEntity<ResponseStructure<List<Reimbursement>>> findReimbursementDetail(){
		return reimbursementService.findReimbursementDetail();
	}
	
	@GetMapping(value = "/getReimbursement/{employeeId}")
	private ResponseEntity<ResponseStructure<List<Reimbursement>>> getReimbursement(@PathVariable String employeeId) {
		return reimbursementService.getReimbursement(employeeId);
	}
	
}
