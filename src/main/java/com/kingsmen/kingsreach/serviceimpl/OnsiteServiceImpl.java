package com.kingsmen.kingsreach.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.repo.OnsiteRepo;
import com.kingsmen.kingsreach.service.OnsiteService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class OnsiteServiceImpl implements OnsiteService {

	@Autowired
	private OnsiteRepo onsiteRepo;
	

	@Override
<<<<<<< HEAD
	public Onsite onsiteEmployee(Onsite onsite) {
		return onsiteRepo.save(onsite);
=======
	public ResponseEntity<ResponseStructure<Onsite>> onsiteEmployee(Onsite onsite) {

		onsite=onsiteRepo.save(onsite);

		String message=onsite.getEmpName() + " went to onsite to meet the client " + onsite.getClientName();

		ResponseStructure<Onsite> responseStructure=new ResponseStructure<Onsite>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(message);
		responseStructure.setData(onsite);

		return new ResponseEntity<ResponseStructure<Onsite>>(responseStructure, HttpStatus.OK);
>>>>>>> 973128086ad1b476817bc77ac0f37efecc6aabe4
	}

}
