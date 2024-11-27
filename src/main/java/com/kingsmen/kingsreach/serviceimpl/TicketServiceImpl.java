package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Ticket;
import com.kingsmen.kingsreach.enums.TicketStatus;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.TicketRepo;
import com.kingsmen.kingsreach.service.TicketService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private TicketRepo ticketRepo;

	@Override
	public ResponseEntity<ResponseStructure<Ticket>> raisedTicket(Ticket ticket) {

		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(ticket.getEmployeeId());

		ticket.setEmployee(byEmployeeId.get());

		ticket.setStatus(TicketStatus.NEW);
		
		 ticketRepo.save(ticket);
		 
		 ResponseStructure<Ticket> responseStructure = new ResponseStructure<Ticket>();
		 responseStructure.setStatusCode(HttpStatus.OK.value());
		 responseStructure.setMessage(" Ticket Raised successfully.");
		 responseStructure.setData(ticket);
		 
		 return new ResponseEntity<ResponseStructure<Ticket>>(responseStructure, HttpStatus.OK);
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
