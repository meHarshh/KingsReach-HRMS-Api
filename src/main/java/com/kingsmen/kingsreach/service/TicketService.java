package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Ticket;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface TicketService {

	ResponseEntity<ResponseStructure<Ticket>> raisedTicket(Ticket ticket);

	Ticket updateTicket(Ticket ticket);

}
