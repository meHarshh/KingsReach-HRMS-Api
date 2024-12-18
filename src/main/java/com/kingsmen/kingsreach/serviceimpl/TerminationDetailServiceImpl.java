package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.exceptions.TerminationDetailNotFoundException;
import com.kingsmen.kingsreach.repo.NotificationRepo;
import com.kingsmen.kingsreach.repo.TerminationDetailRepo;
import com.kingsmen.kingsreach.service.TerminationDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class TerminationDetailServiceImpl implements TerminationDetailService {

	@Autowired
	private TerminationDetailRepo detailRepo;

	@Autowired
	private NotificationRepo notificationRepo;

	@Override
	public ResponseEntity<ResponseStructure<TerminationDetail>> terminationDetail(TerminationDetail detail) {

		detail = detailRepo.save(detail);

		String message = "Termination details of " + detail.getEmployeeName() + " added.";

		ResponseStructure<TerminationDetail> responseStructure = new ResponseStructure<TerminationDetail>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(detail);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(detail.getEmployeeId());
		notify.setMessage(message);
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<TerminationDetail>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<TerminationDetail>> editTermination(int terminationDetailId, TerminationDetail terminationDetail) {

		TerminationDetail employee = detailRepo.findById(terminationDetailId)
				.orElseThrow(() -> new TerminationDetailNotFoundException("No details found for TerminationDetail ID: " + terminationDetailId));

		employee.setEmployeeId(terminationDetail.getEmployeeId());
		employee.setNoticeDate(terminationDetail.getNoticeDate());
		employee.setTerminationReason(terminationDetail.getTerminationReason());
		//	employee.setTerminationReason(terminationDetail.getTerminationReason());

		// employee.setDepartment(terminationDetail.getDepartment());

		TerminationDetail updatedDetail = detailRepo.save(employee);

		String message = "Termination ID: " + terminationDetail.getTerminationDetailId() + " of "
				+ terminationDetail.getEmployeeName() + " updated successfully!!";

		ResponseStructure<TerminationDetail> responseStructure = new ResponseStructure<TerminationDetail>();
		responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(updatedDetail);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(terminationDetail.getEmployeeId());
		notify.setMessage(message);
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

		return new ResponseEntity<ResponseStructure<TerminationDetail>>(responseStructure, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<ResponseStructure<TerminationDetail>> deleteTermination(int terminationDetailId) {

		TerminationDetail employee = detailRepo.findById(terminationDetailId)
				.orElseThrow(() -> new TerminationDetailNotFoundException("No details found for TerminationDetail ID: " + terminationDetailId));

		detailRepo.delete(employee);

		ResponseStructure<TerminationDetail> responseStructure = new ResponseStructure<TerminationDetail>();

		String message = "Termination details for  ID: " + terminationDetailId + " deleted successfully.";

		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(message);
		responseStructure.setData(employee);

		//Notification code 
		Notification notify = new Notification();
		notify.setEmployeeId(employee.getEmployeeId());
		notify.setMessage(message);
		notify.setCreatedAt(LocalDateTime.now());
		notificationRepo.save(notify);

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
