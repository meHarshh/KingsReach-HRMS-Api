package com.kingsmen.kingsreach.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Onsite;

public interface OnsiteRepo extends JpaRepository<Onsite, Integer>{

	List<Onsite> findByDate(LocalDate now);

	

	

}
