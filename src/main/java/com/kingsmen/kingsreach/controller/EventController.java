package com.kingsmen.kingsreach.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Event;
import com.kingsmen.kingsreach.service.EventService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "https://hrms.kingsmenrealty.com")
@RestController
public class EventController {

	@Autowired
	private EventService eventService;
	
	@PostMapping(value = "/addEvent")
	private ResponseEntity<ResponseStructure<Event>> addEvent(@RequestBody Event event) {
		return eventService.addEvent(event);
	}
	
	@GetMapping(value = "/getEvents")
	private ResponseEntity<ResponseStructure<List<Event>>> getEvents(){
		return eventService.getEvents();
	}
}
