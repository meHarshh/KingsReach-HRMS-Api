package com.kingsmen.kingsreach.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;

import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.repo.TerminationDetailRepo;
import com.kingsmen.kingsreach.service.TerminationDetailService;

public class TerminationDetailServiceImpl implements TerminationDetailService {

	@Autowired
	private TerminationDetailRepo detailRepo;

	@Override
	public TerminationDetail terminationDetail(TerminationDetail detail) {

		return detailRepo.save(detail);
	}

}
