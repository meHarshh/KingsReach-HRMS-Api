package com.kingsmen.kingsreach.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, Integer> {

}
