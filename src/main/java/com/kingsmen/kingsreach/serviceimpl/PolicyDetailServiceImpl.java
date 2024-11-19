package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.PolicyDetail;
import com.kingsmen.kingsreach.repo.PolicyDetailRepo;
import com.kingsmen.kingsreach.service.PolicyDetailService;

@Service
public class PolicyDetailServiceImpl implements PolicyDetailService {

	@Autowired
	private PolicyDetailRepo detailRepo;

	@Override
	public PolicyDetail policyDetail(PolicyDetail detail) {
		return detailRepo.save(detail);
	}

	@Override
	public PolicyDetail editPolicy(String policyName, PolicyDetail policyDetail) {
		Optional<PolicyDetail> name = detailRepo.findByPolicyName(policyName);
		PolicyDetail policyDetail2 = name.get();

		policyDetail2.setPolicyName(policyDetail.getPolicyName());
		policyDetail2.setDescription(policyDetail.getDescription());
		policyDetail2.setDepartment(policyDetail.getDepartment());

		return detailRepo.save(policyDetail2);

	}

}
