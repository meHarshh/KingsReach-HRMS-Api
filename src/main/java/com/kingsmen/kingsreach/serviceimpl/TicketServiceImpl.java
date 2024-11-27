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

	@Override
	public Ticket updateTicket(Ticket ticket) {
		
		
		Ticket ticket2 = ticketRepo.findById(ticket.getTicketId())
				.orElseThrow(() -> new RuntimeException("Invalid ticketID"));

		ticket2.setPriority(ticket.getPriority());
		ticket2.setDescription(ticket.getDescription());
		ticket2.setStatus(ticket.getStatus());
		ticket2.setUpdatedAt(ticket.getUpdatedAt());
		ticket2.setUpdatedBy(ticket.getUpdatedBy());

		return ticketRepo.save(ticket2);
	}

}
