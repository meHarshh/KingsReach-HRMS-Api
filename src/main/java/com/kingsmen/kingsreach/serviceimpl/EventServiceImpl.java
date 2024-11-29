package com.kingsmen.kingsreach.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Event;
import com.kingsmen.kingsreach.repo.EventRepo;
import com.kingsmen.kingsreach.service.EventService;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepo eventRepo;

	@Override
	public Event addEvent(Event event) {
		return eventRepo.save(event);
	}

	@Override
	public List<Event> getEvents() {
		// TODO Auto-generated method stub
		return eventRepo.findAll();
	}

}
