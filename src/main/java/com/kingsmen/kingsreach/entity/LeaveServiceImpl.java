package com.kingsmen.kingsreach.entity;

import java.time.Month;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        Optional<Employee> employeeOpt = employeeRepo.findByEmployeeId(employeeId);
        if (!employeeOpt.isPresent()) {
            return buildErrorResponse("Invalid Employee ID");
        }
        
        Employee employee = employeeOpt.get();
        String approvedBy = leave.getApprovedBy();
        
        // Validate and apply leave based on type
        ResponseEntity<ResponseStructure<Leave>> validationResponse = validateAndApplyLeave(employee, leave);
        if (validationResponse != null) {
            return validationResponse; // Return error response if validation fails
        }

        // Apply the leave
        leave.setApprovedBy(approvedBy);
        leave = leaveRepo.save(leave);

        // Calculate and handle Loss of Pay (LOP) if necessary
        double lopDeduction = calculateLopDeduction(employee, leave);
        if (lopDeduction > 0) {
            if (employee.getPayroll() != null) {
                double newSalary = employee.getPayroll().getGrossSalary() - lopDeduction;
                employee.getPayroll().setGrossSalary(newSalary);
            } else {
                return buildErrorResponse("Employee payroll information is missing.");
            }
        }

        // Save the updated employee after applying leave and salary adjustments
        employeeRepo.save(employee);

        // Create and return response structure
        String message = "Employee ID: " + leave.getEmployeeId() + " applied for " + leave.getLeaveType() + " leave.";
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.CREATED.value());
        responseStructure.setMessage(message);
        responseStructure.setData(leave);

        return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
    }

    private ResponseEntity<ResponseStructure<Leave>> validateAndApplyLeave(Employee employee, Leave leave) {
        int month = leave.getFromDate().getMonthValue();

        switch (leave.getLeaveType()) {
            case CASUAL:
                return validateCasualLeave(employee, leave, month);
            case SICK:
                return validateSickLeave(employee, leave);
            case PAID:
                return validatePaidLeave(employee, leave);
            default:
                return buildErrorResponse("Invalid leave type.");
        }
    }

    private ResponseEntity<ResponseStructure<Leave>> validateCasualLeave(Employee employee, Leave leave, int month) {
        if (employee.getClBalance() + leave.getNumberOfDays() > 10) {
            return buildErrorResponse("Cannot exceed 10 days of Casual Leave for the year.");
        }
        if (month == Month.MARCH.getValue() || month == Month.APRIL.getValue()) {
            return buildErrorResponse("Casual Leave is not allowed in February and March.");
        }
        if (employee.getClBalance() < leave.getNumberOfDays()) {
            return buildErrorResponse("Insufficient Casual Leave balance.");
        }

        // Deduct the leave from balance
        employee.setClBalance(employee.getClBalance() - leave.getNumberOfDays());
        return null;
    }

    private ResponseEntity<ResponseStructure<Leave>> validateSickLeave(Employee employee, Leave leave) {
        if (employee.getSlBalance() + leave.getNumberOfDays() > 12) {
            return buildErrorResponse("Cannot exceed 12 days of Sick Leave for the year.");
        }
        if (employee.getSlBalance() < leave.getNumberOfDays()) {
            return buildErrorResponse("Insufficient Sick Leave balance.");
        }

        // Deduct the leave from balance
        employee.setSlBalance(employee.getSlBalance() - leave.getNumberOfDays());
        return null;
    }

    private ResponseEntity<ResponseStructure<Leave>> validatePaidLeave(Employee employee, Leave leave) {
        if (employee.getPlBalance() + leave.getNumberOfDays() > 12) {
            return buildErrorResponse("Cannot exceed 12 days of Paid Leave for the year.");
        }
        if (employee.getPlBalance() < leave.getNumberOfDays()) {
            return buildErrorResponse("Insufficient Paid Leave balance.");
        }

        // Deduct the leave from balance
        employee.setPlBalance(employee.getPlBalance() - leave.getNumberOfDays());
        return null;
    }

    // Helper method to calculate Loss of Pay deduction
    private double calculateLopDeduction(Employee employee, Leave leave) {
        int maxAllowedLeaves = (leave.getFromDate().getMonthValue() == Month.MARCH.getValue() || leave.getFromDate().getMonthValue() == Month.APRIL.getValue()) ? 2 : 3;
        
        // If the employee exceeds the allowed leave for the month
        if (leave.getNumberOfDays() > maxAllowedLeaves) {
            int excessLeaves = leave.getNumberOfDays() - maxAllowedLeaves;
            return employee.getPayroll().getGrossSalary() * (excessLeaves / 30.0); // Assuming 30 days in a month
        }
        return 0.0;
    }

    // Helper method to build error response
    private ResponseEntity<ResponseStructure<Leave>> buildErrorResponse(String message) {
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseStructure.setMessage(message);
        responseStructure.setData(null);
        return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {
        String employeeId = leave.getEmployeeId();

        Optional<Leave> optional = leaveRepo.findByEmployeeId(employeeId);

        if (!optional.isPresent()) {
            return buildErrorResponse("Leave record not found.");
        }

        Leave leave2 = optional.get();
        leave2.setLeaveStatus(leave.getLeaveStatus());
        leave2 = leaveRepo.save(leave2);

        String message = "Employee ID: " + leave.getEmployeeId() + " leave status is " + leave.getLeaveStatus();
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
        responseStructure.setMessage(message);
        responseStructure.setData(leave2);

        return new ResponseEntity<>(responseStructure, HttpStatus.ACCEPTED);
    }
}
