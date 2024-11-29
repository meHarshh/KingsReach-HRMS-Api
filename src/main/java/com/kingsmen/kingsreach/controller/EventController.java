package com.kingsmen.kingsreach.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Event;
import com.kingsmen.kingsreach.service.EventService;

@RestController
public class EventController {

	@Autowired
	private EventService eventService;
	
	@PostMapping(value = "/addEvent")
	private Event addEvent(@PathVariable Event event) {
		return eventService.addEvent(event);
	}
	
	@GetMapping(value = "/getEvents")
	private List<Event> getEvents(){
		return eventService.getEvents();
	}
}
