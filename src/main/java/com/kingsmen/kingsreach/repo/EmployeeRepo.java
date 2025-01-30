package com.kingsmen.kingsreach.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Manager;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

	boolean existsByofficialEmailAndPassword(String officialEmail, String password);

	boolean existsByofficialEmailAndUserName(String officialEmail, String userName);

	public Optional<Employee> findByofficialEmailAndPassword(String officialEmail, String password);

	public Optional<Employee> findByFirstName(String firstName);

	public Optional<Employee> findByEmployeeId(String employeeId);

	public Optional<Employee> findByName(String employeeName);

	boolean existsByEmployeeId(String employeeId);

	List<Employee> findEmployeesByDate(LocalDate now);

	List<Employee> findByDate(LocalDate date);

	List<Employee> findByManager(Manager manager);


}
