package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	boolean existsByofficialEmailAndPassword(String officialEmail, String password);

	boolean existsByofficialEmailAndUserName(String officialEmail, String userName);

	public Optional<Employee> findByofficialEmailAndPassword(String officialEmail, String password);

	public Optional<Employee> findByFirstName(String firstName);

	public Optional<Employee> findByEmployeeId(String employeeId);

}
