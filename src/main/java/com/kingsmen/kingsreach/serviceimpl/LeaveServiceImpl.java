package com.kingsmen.kingsreach.serviceimpl;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.enums.LeaveStatus;
import com.kingsmen.kingsreach.enums.LeaveType;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.LeaveRepo;
import com.kingsmen.kingsreach.service.LeaveService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LeaveRepo leaveRepo;

    @Override
    public ResponseEntity<ResponseStructure<Leave>> applyLeave(Leave leave) {
        String employeeId = leave.getEmployeeId();
        // Find employee by employeeId
        Employee employee = employeeRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("No employee found with the selected id"));

        // Get the fromDate of leave
        LocalDate d1 = leave.getFromDate();

        // Check if the leave type is CASUAL
        if (leave.getLeaveType() == LeaveType.CASUAL) {
            // Check if Casual leave is restricted in March or April
            if (isCasualLeaveRestricted(d1)) {
                throw new RuntimeException("Casual leave cannot be taken in March or April");
            }
        }

        // Further leave application logic can be added here, e.g., checking leave balance, etc.

        // Assuming leave is applied successfully, you can save the leave entity
        leave.setLeaveStatus(LeaveStatus.PENDING);  // Example: Set status to pending initially
        leaveRepo.save(leave); // Save leave application

        // Create a structured response to return to the user
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setMessage("Leave applied successfully");
        responseStructure.setStatusCode(200);
        responseStructure.setData(leave);

        return ResponseEntity.ok(responseStructure);
    }

    // Helper method to check if Casual leave is restricted in March or April
    private boolean isCasualLeaveRestricted(LocalDate date) {
        return date.getMonthValue() == 3 || date.getMonthValue() == 4;  // March (3) and April (4) restrictions
    }

    @Override
    public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {
        // Implement logic for changing leave status
        // Example: Approve or Reject leave requests
        
        // Returning a placeholder response for now
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setMessage("Leave status updated successfully");
        responseStructure.setStatusCode(200);
        responseStructure.setData(leave);

        return ResponseEntity.ok(responseStructure);
    }
}
