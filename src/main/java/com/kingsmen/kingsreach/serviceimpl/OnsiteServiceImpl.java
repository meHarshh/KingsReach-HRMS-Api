package com.kingsmen.kingsreach.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.repo.OnsiteRepo;
import com.kingsmen.kingsreach.service.OnsiteService;

@Service
public class OnsiteServiceImpl implements OnsiteService {

	@Autowired
	private OnsiteRepo onsiteRepo;
	

	@Override
	public Onsite onsiteEmployee(Onsite onsite) {
		return onsiteRepo.save(onsite);
	}

}
