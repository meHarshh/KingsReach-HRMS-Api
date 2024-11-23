package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	boolean existsByEmailAndPassword(String email, String password);

	boolean existsByEmailAndUserName(String email, String userName);

	public Optional<Employee> findByEmailAndPassword(String email, String password);

	public Optional<Employee> findByFirstName(String firstName);

	public Optional<Employee> findByEmployeeId(String employeeId);

}
