package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.PolicyDetail;

public interface PolicyDetailRepo extends JpaRepository<PolicyDetail, Integer>{

	Optional<PolicyDetail> findByPolicyName(String policyName);

}
