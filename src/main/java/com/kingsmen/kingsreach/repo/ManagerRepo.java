package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Manager;

public interface ManagerRepo extends JpaRepository<Manager, Integer> {

	Optional<Manager> findByEmployeeId(String managerId);

}
