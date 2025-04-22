package com.kingsmen.kingsreach.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {

	List<Ticket> findByEmployeeId(String employeeId);

}
