package com.kingsmen.kingsreach.service;

import com.kingsmen.kingsreach.entity.PolicyDetail;

public interface PolicyDetailService {

	PolicyDetail policyDetail(PolicyDetail detail);


	PolicyDetail editPolicy(String policyName, PolicyDetail policyDetail);

}
