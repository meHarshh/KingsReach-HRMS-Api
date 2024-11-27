package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Reimbursement;
import com.kingsmen.kingsreach.service.ReimbursementService;

@RestController
public class ReimbursementController {

	@Autowired
	private ReimbursementService reimbursementService;
	
	
	@PostMapping(value = "/reimbursement")
	private Reimbursement reimbursement(@PathVariable Reimbursement reimbursement) {
		return reimbursementService.reimbursement(reimbursement);
	}
	
}
