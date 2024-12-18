package com.kingsmen.kingsreach.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Reimbursement;

public interface ReimbursementRepo extends JpaRepository<Reimbursement, Integer>{

	List<Reimbursement> findByEmployeeId(String employeeId);

}
