package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.service.OnsiteService;

@RestController
public class OnsiteController {

	@Autowired
	private OnsiteService onsiteService;
	
	@PostMapping(value = "/onsiteEmployee")
	private Onsite onsiteEmployee(@RequestBody Onsite onsite) {
		return onsiteService.onsiteEmployee(onsite);
	}
}
