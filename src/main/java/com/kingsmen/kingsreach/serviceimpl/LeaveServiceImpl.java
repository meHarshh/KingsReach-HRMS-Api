package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.service.LeaveService;

@Service
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private LeaveRepo leaveRepo;

	@Override
	public Leave applyLeave(Leave leave) {
		String approvedBy = leave.getApprovedBy();
		String[] split = approvedBy.split(" ");

		Optional<Employee> byEmployeeName = employeeRepo.findByEmployeeFirstName(split[0]);

		leave.setApproved(byEmployeeName.get());

		return leaveRepo.save(leave);

	}

}
