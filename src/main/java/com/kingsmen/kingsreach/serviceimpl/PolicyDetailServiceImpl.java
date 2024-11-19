package com.kingsmen.kingsreach.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.kingsmen.kingsreach.entity.PolicyDetail;
import com.kingsmen.kingsreach.repo.PolicyDetailRepo;
import com.kingsmen.kingsreach.service.PolicyDetailService;

public class PolicyDetailServiceImpl implements PolicyDetailService {

	@Autowired
	private PolicyDetailRepo detailRepo;
	
	@Override
	public PolicyDetail policyDetail(PolicyDetail detail) {
		 return detailRepo.save(detail);
	}

}
