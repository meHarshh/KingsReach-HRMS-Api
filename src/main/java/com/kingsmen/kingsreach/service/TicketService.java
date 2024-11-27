package com.kingsmen.kingsreach.service;

import com.kingsmen.kingsreach.entity.Ticket;

public interface TicketService {

	Ticket raisedTicket(Ticket ticket);

	Ticket updateTicket(Ticket ticket);

}
