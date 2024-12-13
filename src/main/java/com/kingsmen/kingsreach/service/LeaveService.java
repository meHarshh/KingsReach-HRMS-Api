package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.enums.Department;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface LeaveService {

	ResponseEntity<ResponseStructure<Leave>> applyLeave(Leave leave);

	ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave);

	ResponseEntity<ResponseStructure<List<Leave>>> getLeave();

	ResponseEntity<ResponseStructure<List<Leave>>> getEmployeeLeave(String employeeId);

	ResponseEntity<ResponseStructure<List<Leave>>> findAbsentEmployees();

	ResponseEntity<ResponseStructure<int[]>> getRemainingLeave(String employeeId);

	ResponseEntity<ResponseStructure<List<Leave>>> findAbsentEmployees(Department department);

	ResponseEntity<ResponseStructure<List<Leave>>> fetchLeaveBasedOnManagerEmployee(String employeeId);


}
