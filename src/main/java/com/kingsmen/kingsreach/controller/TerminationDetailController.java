package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.service.TerminationDetailService;

@RestController
public class TerminationDetailController {

	@Autowired
	private TerminationDetailService terminationDetailService;

	@PostMapping(value = "/terminationDetail")
	private TerminationDetail terminationDetail(@RequestBody TerminationDetail detail) {
		return terminationDetailService.terminationDetail(detail);
	}

	@PutMapping(value = "/editTerminationDetail")
	public TerminationDetail editTermination(@RequestBody TerminationDetail terminationDetail) {
		return terminationDetailService.editTermination(terminationDetail);
	}
}
