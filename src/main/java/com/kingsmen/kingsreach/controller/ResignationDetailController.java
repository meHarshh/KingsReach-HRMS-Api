package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.ResignationDetail;
import com.kingsmen.kingsreach.service.ResignationDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173")
@RestController
public class ResignationDetailController {
	
	@Autowired
	private ResignationDetailService resignationDetailService; 

	@PostMapping(value = "/resignationDetail")
	private ResponseEntity<ResponseStructure<ResignationDetail>> resignationDetail(@RequestBody ResignationDetail resignationDetail) {
		return resignationDetailService.resignationDetail(resignationDetail);
	}
}
