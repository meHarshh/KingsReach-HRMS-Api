package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Event;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface EventService {

	ResponseEntity<ResponseStructure<Event>> addEvent(Event event);

	ResponseEntity<ResponseStructure<List<Event>>> getEvents();

}
