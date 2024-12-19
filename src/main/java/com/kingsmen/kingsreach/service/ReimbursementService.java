package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Reimbursement;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface ReimbursementService {

	ResponseEntity<ResponseStructure<Reimbursement>> reimbursement(Reimbursement reimbursement);

	ResponseEntity<ResponseStructure<Reimbursement>> changeReimbursementStatus(Reimbursement reimbursement);

	ResponseEntity<ResponseStructure<List<Reimbursement>>> findReimbursementDetail();

	ResponseEntity<ResponseStructure<List<Reimbursement>>> getReimbursement(String employeeId);

}
