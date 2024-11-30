package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Event;
import com.kingsmen.kingsreach.repo.EventRepo;
import com.kingsmen.kingsreach.service.EventService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepo eventRepo;

	@Override
	public ResponseEntity<ResponseStructure<Event>> addEvent(Event event) {
	 eventRepo.save(event);
	 
	 ResponseStructure<Event> responseStructure = new ResponseStructure<Event>();
	 responseStructure.setStatusCode(HttpStatus.CREATED.value());
	 responseStructure.setData(event);
	 responseStructure.setMessage("Event detail added.");
	 
	 return new ResponseEntity<ResponseStructure<Event>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<Event>>> getEvents() {
		
	List<Event> list = eventRepo.findAll();
	 
	 ResponseStructure<List<Event>> responseStructure = new ResponseStructure<List<Event>>();
	 responseStructure.setStatusCode(HttpStatus.OK.value());
	 responseStructure.setData(list);
	 responseStructure.setMessage("Event detail added.");
	 
	 return new ResponseEntity<ResponseStructure<List<Event>>>(responseStructure, HttpStatus.OK);
	}

}
