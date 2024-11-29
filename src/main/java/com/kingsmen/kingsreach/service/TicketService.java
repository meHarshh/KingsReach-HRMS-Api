package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Ticket;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface TicketService {

	ResponseEntity<ResponseStructure<Ticket>> raisedTicket(Ticket ticket);

	ResponseEntity<ResponseStructure<Ticket>> updateTicket(Ticket ticket);

	ResponseEntity<ResponseStructure<List<Ticket>>> findAllTicket();

	ResponseEntity<ResponseStructure<Ticket>> deleteTicket(int ticketId);

}
