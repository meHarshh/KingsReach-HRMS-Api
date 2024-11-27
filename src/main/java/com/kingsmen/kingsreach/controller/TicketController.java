package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Ticket;
import com.kingsmen.kingsreach.service.TicketService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@RestController
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@PostMapping(value = "/raisedTicket")
	private ResponseEntity<ResponseStructure<Ticket>> raisedTicket(@RequestBody Ticket ticket) {
		return ticketService.raisedTicket(ticket);
	}
}
