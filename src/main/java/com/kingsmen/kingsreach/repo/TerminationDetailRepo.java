package com.kingsmen.kingsreach.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.TerminationDetail;

public interface TerminationDetailRepo extends JpaRepository<TerminationDetail, Integer> {

	Optional<TerminationDetail> findByEmployeeName(String employeeName);

	Optional<TerminationDetail> findByEmployeeId(String employeeId);

}
