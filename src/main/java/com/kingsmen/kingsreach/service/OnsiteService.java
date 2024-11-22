package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface OnsiteService {

	ResponseEntity<ResponseStructure<Onsite>> onsiteEmployee(Onsite onsite);

}
