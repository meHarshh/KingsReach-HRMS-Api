package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.ResignationDetail;
import com.kingsmen.kingsreach.repo.ResignationDetailRepo;
import com.kingsmen.kingsreach.service.ResignationDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class ResignationDetailServiceImpl implements ResignationDetailService {

	@Autowired
	private ResignationDetailRepo resignationDetailRepo;

	@Override
	public ResponseEntity<ResponseStructure<ResignationDetail>> resignationDetail(ResignationDetail resignationDetail) {

		resignationDetail=resignationDetailRepo.save(resignationDetail);

		String message="Employee ID :" + resignationDetail.getEmployeeId() + " " + resignationDetail.getName() +" resigned.";

		ResponseStructure<ResignationDetail> responseStructure=new ResponseStructure<ResignationDetail>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(resignationDetail);

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
		            .orElseThrow(() -> new RuntimeException("Resignation with ID " + resignationId + " not found"));

		   existingResignation.setResignationStatus(resignationDetail.getResignationStatus());

		    ResponseStructure<ResignationDetail> responseStructure = new ResponseStructure<>();
		    responseStructure.setStatusCode(HttpStatus.OK.value());
		    responseStructure.setMessage("Resignation status updated successfully.");
		    responseStructure.setData(existingResignation);

		    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
		}

		
	}

