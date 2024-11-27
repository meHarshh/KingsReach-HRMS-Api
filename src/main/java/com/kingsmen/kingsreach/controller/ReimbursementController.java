package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Reimbursement;
import com.kingsmen.kingsreach.service.ReimbursementService;

@RestController
public class ReimbursementController {

	@Autowired
	private ReimbursementService reimbursementService;
	
	
	@PostMapping(value = "/reimbursement")
	private Reimbursement reimbursement(@RequestBody Reimbursement reimbursement) {
		return reimbursementService.reimbursement(reimbursement);
	}
	
	@PutMapping(value = "/changeReimbursementStatus")
	private Reimbursement changeReimbursementStatus(@RequestBody Reimbursement reimbursement) {
		return reimbursementService.changeReimbursementStatus(reimbursement);
		
	}
	
	
}
