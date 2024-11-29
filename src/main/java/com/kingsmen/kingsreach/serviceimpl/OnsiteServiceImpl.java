package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;

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
	public ResponseEntity<ResponseStructure<Onsite>> onsiteEmployee(Onsite onsite) {

		onsite=onsiteRepo.save(onsite);

		String message=onsite.getEmpName() + " went to onsite to meet the client " + onsite.getClientName();

		ResponseStructure<Onsite> responseStructure=new ResponseStructure<Onsite>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage(message);
		responseStructure.setData(onsite);

		return new ResponseEntity<ResponseStructure<Onsite>>(responseStructure, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ResponseStructure<List<Onsite>>> findOnsiteEmployees() {
		List<Onsite> list = onsiteRepo.findAll();
		
		ResponseStructure<List<Onsite>> responseStructure=new ResponseStructure<List<Onsite>>();
		responseStructure.setStatusCode(HttpStatus.OK.value());
		responseStructure.setMessage("Onsite Employees Details Fetched Successfully.");
		responseStructure.setData(list);

		return new ResponseEntity<ResponseStructure<List<Onsite>>>(responseStructure, HttpStatus.OK);
		
	}

}
