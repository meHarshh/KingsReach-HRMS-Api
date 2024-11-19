package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.TerminationDetail;
import com.kingsmen.kingsreach.repo.TerminationDetailRepo;
import com.kingsmen.kingsreach.service.TerminationDetailService;

@Service
public class TerminationDetailServiceImpl implements TerminationDetailService {

	@Autowired
	private TerminationDetailRepo detailRepo;

	@Override
	public TerminationDetail terminationDetail(TerminationDetail detail) {

		return detailRepo.save(detail);
	}

	@Override
	public TerminationDetail editTermination(TerminationDetail terminationDetail) {

		Optional<TerminationDetail> byEmployeeName = detailRepo.findByEmployeeName(terminationDetail.getEmployeeName());
		TerminationDetail employee = byEmployeeName.get();

		employee.setTerminationDate(terminationDetail.getTerminationDate());
		employee.setNoticeDate(terminationDetail.getNoticeDate());
		employee.setTerminationReason(terminationDetail.getTerminationReason());
		employee.setTerminationType(terminationDetail.getTerminationType());

		return detailRepo.save(employee);
	}

}
