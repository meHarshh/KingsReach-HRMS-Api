package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.service.TerminationDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "https://hrms.kingsmenrealty.com/")
@RestController
public class TerminationDetailController {

	@Autowired
	private TerminationDetailService terminationDetailService;

	@PostMapping(value = "/terminationDetail")
	private ResponseEntity<ResponseStructure<TerminationDetail>> terminationDetail(
			@RequestBody TerminationDetail detail) {
		return terminationDetailService.terminationDetail(detail);
	}

	@PutMapping(value = "/editTerminationDetail")
	public ResponseEntity<ResponseStructure<TerminationDetail>> editTermination(@RequestParam int terminationDetailId,
			@RequestBody TerminationDetail terminationdetail) {
		return terminationDetailService.editTermination(terminationDetailId, terminationdetail);
	}

	@DeleteMapping(value = "/deleteTerminationDetail/{terminationDetailId}")
	public ResponseEntity<ResponseStructure<TerminationDetail>> deleteTermination(@PathVariable int terminationDetailId) {
		return terminationDetailService.deleteTermination(terminationDetailId);
	}

	@GetMapping(value = "/findAllTermination")
	public ResponseEntity<ResponseStructure<List<TerminationDetail>>> findAllTerminations() {
		return terminationDetailService.findAllTerminations();
	}

}
