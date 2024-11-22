package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.PolicyDetail;
import com.kingsmen.kingsreach.repo.PolicyDetailRepo;
import com.kingsmen.kingsreach.service.PolicyDetailService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class PolicyDetailServiceImpl implements PolicyDetailService {

	@Autowired
	private PolicyDetailRepo detailRepo;

	@Override
	public ResponseEntity<ResponseStructure<PolicyDetail>> policyDetail(PolicyDetail detail) {
		detail=detailRepo.save(detail);

		String message="Policy details of " + detail.getDepartment() + " department";

		ResponseStructure<PolicyDetail> responseStructure = new ResponseStructure<PolicyDetail>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(detail);

		return new ResponseEntity<ResponseStructure<PolicyDetail>>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<PolicyDetail>> editPolicy(String policyName, PolicyDetail policyDetail) {
		Optional<PolicyDetail> name = detailRepo.findByPolicyName(policyName);
		PolicyDetail policyDetail2 = name.get();

		policyDetail2.setPolicyName(policyDetail.getPolicyName());
		policyDetail2.setDescription(policyDetail.getDescription());
		policyDetail2.setDepartment(policyDetail.getDepartment());

		PolicyDetail policyDetail3=detailRepo.save(policyDetail2);

		String message="Policy details of " + policyDetail.getDepartment() + " department updated successfully!!";

		ResponseStructure<PolicyDetail> responseStructure = new ResponseStructure<PolicyDetail>();
		responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(policyDetail3);

		return new ResponseEntity<ResponseStructure<PolicyDetail>>(responseStructure,HttpStatus.ACCEPTED);

	}
}
