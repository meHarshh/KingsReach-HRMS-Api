package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.repo.TerminationDetailRepo;
import com.kingsmen.kingsreach.service.TerminationDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class TerminationDetailServiceImpl implements TerminationDetailService {

	@Autowired
	private TerminationDetailRepo detailRepo;

	@Override
	public ResponseEntity<ResponseStructure<TerminationDetail>> terminationDetail(TerminationDetail detail) {

		detail = detailRepo.save(detail);

		String message = "Termination details of " + detail.getEmployeeName() + " added.";

		ResponseStructure<TerminationDetail> responseStructure = new ResponseStructure<TerminationDetail>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(detail);

		return new ResponseEntity<ResponseStructure<TerminationDetail>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<TerminationDetail>> editTermination(String employeeId,
			TerminationDetail terminationDetail) {

		int terminationDetailId = terminationDetail.getTerminationDetailId();

		TerminationDetail employee = detailRepo.findById(terminationDetailId).orElseThrow(() -> new RuntimeException());

		employee.setEmployeeId(terminationDetail.getEmployeeId());
		employee.setNoticeDate(terminationDetail.getNoticeDate());
		employee.setTerminationReason(terminationDetail.getTerminationReason());
		employee.setTerminationReason(terminationDetail.getTerminationReason());

		// employee.setDepartment(terminationDetail.getDepartment());

		TerminationDetail updatedDetail = detailRepo.save(employee);

		String message = "Termination ID: " + terminationDetail.getTerminationDetailId() + " of "
				+ terminationDetail.getEmployeeName() + " updated successfully!!";

		ResponseStructure<TerminationDetail> responseStructure = new ResponseStructure<TerminationDetail>();
		responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(updatedDetail);

		return new ResponseEntity<ResponseStructure<TerminationDetail>>(responseStructure, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<ResponseStructure<TerminationDetail>> deleteTermination(String employeeId) {
		Optional<TerminationDetail> byEmployeeName = detailRepo.findByEmployeeId(employeeId);

		TerminationDetail employee = byEmployeeName.get();

		detailRepo.delete(employee);

		ResponseStructure<TerminationDetail> responseStructure = new ResponseStructure<TerminationDetail>();

		String message = "Termination details for Employee ID: " + employeeId + " ( " + employee.getEmployeeName()
				+ " ) deleted successfully.";

		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(message);
		responseStructure.setData(employee);

		return new ResponseEntity<ResponseStructure<TerminationDetail>>(responseStructure, HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ResponseStructure<List<TerminationDetail>>> findAllTerminations() {
		List<TerminationDetail> terminationDetails = detailRepo.findAll();

		ResponseStructure<List<TerminationDetail>> responseStructure = new ResponseStructure<>();

		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Termination details retrieved successfully.");
		responseStructure.setData(terminationDetails);

		return new ResponseEntity<ResponseStructure<List<TerminationDetail>>>(responseStructure, HttpStatus.OK);

	}

}
