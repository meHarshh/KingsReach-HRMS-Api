package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Ticket;
import com.kingsmen.kingsreach.enums.TicketStatus;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.TicketRepo;
import com.kingsmen.kingsreach.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private TicketRepo ticketRepo;

	@Override
	public Ticket raisedTicket(Ticket ticket) {

		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(ticket.getEmployeeId());

		ticket.setEmployee(byEmployeeId.get());

		ticket.setStatus(TicketStatus.NEW);
		
		return ticketRepo.save(ticket);
	}
	

}
