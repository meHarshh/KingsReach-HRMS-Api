package com.kingsmen.kingsreach.service;

import java.util.List;

import com.kingsmen.kingsreach.entity.Event;

public interface EventService {

	Event addEvent(Event event);

	List<Event> getEvents();

}
