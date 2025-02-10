package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Onsite;
import com.kingsmen.kingsreach.service.OnsiteService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(origins = {"http://localhost:5173/","http://server.ovf.bgg.mybluehostin.me:8080/"})
@RestController
public class OnsiteController {

	@Autowired
	private OnsiteService onsiteService;
	
	@PostMapping(value = "/onsiteEmployee")
	private ResponseEntity<ResponseStructure<Onsite>> onsiteEmployee(@RequestBody Onsite onsite) {
		return onsiteService.onsiteEmployee(onsite);
	}
	
	@GetMapping(value = "/findAllOnsiteEmployees")
	private ResponseEntity<ResponseStructure<List<Onsite>>> findOnsiteEmployees(){
		return onsiteService.findOnsiteEmployees();
	}
	
	@GetMapping(value = "/getOnsiteEmployee/{employeeId}")
	private ResponseEntity<ResponseStructure<Onsite>> getOnsiteEmployee(@PathVariable String employeeId){
		return  onsiteService.getOnsiteEmployee(employeeId);
	}
}
