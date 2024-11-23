package com.kingsmen.kingsreach.entity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
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
        Optional<Employee> employeeOpt = employeeRepo.findByEmployeeId(employeeId);
        if (!employeeOpt.isPresent()) {
            return buildErrorResponse("Invalid Employee ID");
        }
        
        Employee employee = employeeOpt.get();
        String approvedBy = leave.getApprovedBy();
        
        // Assuming approvedBy logic is already handled in the service.
        
        LocalDate fromDate = leave.getFromDate();
        int month = fromDate.getMonthValue();
        
        // Determine the max leave allowed for the month (3 leaves for most months, 2 for Feb/Mar)
        int maxLeavePerMonth = (month == Month.FEBRUARY.getValue() || month == Month.MARCH.getValue()) ? 2 : 3;

        // Check if total leaves for the year exceed the annual limits before applying
        if (leave.getLeaveType() == LeaveType.CASUAL) {
            if (employee.getClBalance() + leave.getNumberOfDays() > 10) {
                return buildErrorResponse("Cannot exceed 10 days of Casual Leave for the year.");
            }
            if (month == Month.FEBRUARY.getValue() || month == Month.MARCH.getValue()) {
                return buildErrorResponse("Casual Leave is not allowed in February and March.");
            }
            if (employee.getClBalance() < leave.getNumberOfDays()) {
                return buildErrorResponse("Insufficient Casual Leave balance.");
            }
            employee.setClBalance(employee.getClBalance() + leave.getNumberOfDays());
        } else if (leave.getLeaveType() == LeaveType.SICK) {
            if (employee.getSlBalance() + leave.getNumberOfDays() > 12) {
                return buildErrorResponse("Cannot exceed 12 days of Sick Leave for the year.");
            }
            if (employee.getSlBalance() < leave.getNumberOfDays()) {
                return buildErrorResponse("Insufficient Sick Leave balance.");
            }
            employee.setSlBalance(employee.getSlBalance() + leave.getNumberOfDays());
        } else if (leave.getLeaveType() == LeaveType.PAID) {
            if (employee.getPlBalance() + leave.getNumberOfDays() > 12) {
                return buildErrorResponse("Cannot exceed 12 days of Paid Leave for the year.");
            }
            if (employee.getPlBalance() < leave.getNumberOfDays()) {
                return buildErrorResponse("Insufficient Paid Leave balance.");
            }
            employee.setPlBalance(employee.getPlBalance() + leave.getNumberOfDays());
        }

        // Apply the leave
        leave.setApprovedBy(approvedBy);
        leave = leaveRepo.save(leave);

        // Calculate and handle Loss of Pay (LOP) if necessary
        double lopDeduction = calculateLopDeduction(employee, leave, month);
        if (lopDeduction > 0) {
            // If LOP is applicable, reduce the salary in the Payroll entity
            if (employee.getPayroll() != null) {
                double newSalary = employee.getPayroll().getGrossSalary() - lopDeduction;
                employee.getPayroll().setGrossSalary(newSalary);
            } else {
                return buildErrorResponse("Employee payroll information is missing.");
            }
        }

        // Save the updated employee after applying leave and salary adjustments
        employeeRepo.save(employee);

        String message = "Employee ID: " + leave.getEmployeeId() + " applied for " + leave.getLeaveType() + " leave.";
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.CREATED.value());
        responseStructure.setMessage(message);
        responseStructure.setData(leave);

        return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
    }

    // Helper method to calculate Loss of Pay deduction
    private double calculateLopDeduction(Employee employee, Leave leave, int month) {
        double lopDeduction = 0.0;

        // Determine the maximum leave limit based on the month
        int maxAllowedLeaves = (month == Month.FEBRUARY.getValue() || month == Month.MARCH.getValue()) ? 2 : 3;
        
        // If the employee exceeds the allowed leave for the month
        if (leave.getNumberOfDays() > maxAllowedLeaves) {
            int excessLeaves = leave.getNumberOfDays() - maxAllowedLeaves;
            lopDeduction = employee.getPayroll().getGrossSalary() * (excessLeaves / 30.0); // Assuming 30 days in a month
        }

        return lopDeduction;
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
