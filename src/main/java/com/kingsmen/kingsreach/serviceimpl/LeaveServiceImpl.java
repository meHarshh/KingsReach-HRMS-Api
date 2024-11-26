package com.kingsmen.kingsreach.serviceimpl;


import java.time.LocalDate;

import java.time.Month;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Leave;
import com.kingsmen.kingsreach.entity.Payroll;
import com.kingsmen.kingsreach.enums.LeaveType;
import com.kingsmen.kingsreach.exceptions.InvalidEmployeeIdException;
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
		String employeeId=leave.getEmployeeId();

		Optional<Employee> employeeOpt=employeeRepo.findByEmployeeId(employeeId);
		if(!employeeOpt.isPresent()) {
			return buildErrorResponse("Invalid Employee ID");
		}

		Employee employee = employeeOpt.get();
		String approvedBy = leave.getApprovedBy();

		ResponseEntity<ResponseStructure<Leave>> validationResponse = validateAndApplyLeave(employee , leave);
		if(validationResponse != null) {
			return  validationResponse;
		}

		leave.setApprovedBy(approvedBy);
		leave = leaveRepo.save(leave);

		double lopDeduction = calculateLopDeduction(employee , leave);
		if(lopDeduction > 0) {
			if(employee.getPayroll() != null) {
				double newSalary=employee.getPayroll().getGrossSalary() - lopDeduction;
				employee.getPayroll().setGrossSalary(newSalary);
			}else {
				return buildErrorResponse("Employee Payroll information is missing");
			}
		}

		employeeRepo.save(employee);

		String message= "Employee Id:" + leave.getEmployeeId() + " Applied for " + leave.getLeaveType() + " leave";
		ResponseStructure<Leave> responseStructure=new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(leave);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure, HttpStatus.CREATED);	

	}


	private ResponseEntity<ResponseStructure<Leave>> validateAndApplyLeave(Employee employee, Leave leave) {
		int month = leave.getFromDate().getMonthValue();

		switch (leave.getLeaveType()){
		case CASUAL:
			return validateCasualLeave(employee , leave , month);

		case SICK:
			return validateSickLeave(employee , leave);

		case PAID:
			return validatePaidLeave(employee , leave);

		default:
			return buildErrorResponse("Invalid leave type");
		}
	}

	private ResponseEntity<ResponseStructure<Leave>> validateCasualLeave(Employee employee, Leave leave, int month) {
		if(month == Month.MARCH.getValue() || month == Month.APRIL.getValue() ) {
			return buildErrorResponse("Casual leave is not allowed in march and April. ");	
		}
		if(employee.getClBalance() + leave.getNumberOfDays() > 10 ) {
			return buildErrorResponse("Cannot exceeds 10 days of casual Leave for the year. ");	
		}
		if(employee.getClBalance() < leave.getNumberOfDays()) {
			return buildErrorResponse("Insufficient casual leave balance. ");	
		}

		employee.setClBalance(employee.getClBalance() - leave.getNumberOfDays());
		return null;
	}

	private ResponseEntity<ResponseStructure<Leave>> validateSickLeave(Employee employee, Leave leave) {
		if(employee.getSlBalance() + leave.getNumberOfDays() > 12 ) {
			return buildErrorResponse("Cannot exceeds 12 days of sick Leave for the year. ");	
		}
		if(employee.getSlBalance() < leave.getNumberOfDays()) {
			return buildErrorResponse("Insufficient sick leave balance. ");	
		}

		employee.setSlBalance(employee.getSlBalance() - leave.getNumberOfDays());
		return null;
	}

	private ResponseEntity<ResponseStructure<Leave>> validatePaidLeave(Employee employee, Leave leave) {
		if(employee.getPlBalance() + leave.getNumberOfDays() > 12 ) {
			return buildErrorResponse("Cannot exceeds 12 days of Paid Leave for the year. ");	
		}
		if(employee.getPlBalance() < leave.getNumberOfDays()) {
			return buildErrorResponse("Insufficient paid leave balance. ");	
		}

		employee.setPlBalance(employee.getPlBalance() - leave.getNumberOfDays());
		return null;
	}


	private double calculateLopDeduction(Employee employee, Leave leave) {
		int maxAllowedLeaves = (leave.getFromDate().getMonthValue() == Month.MARCH.getValue() || leave.getFromDate().getMonthValue() == Month.APRIL.getValue()) ? 2 : 0;

		if(leave.getNumberOfDays() > maxAllowedLeaves) {
			int excessLeaves = leave.getNumberOfDays() - maxAllowedLeaves;
			return employee.getPayroll().getGrossSalary() * (excessLeaves/30.0);
		}
		return 0.0;
	}

	private ResponseEntity<ResponseStructure<Leave>> buildErrorResponse(String message) {
		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
		responseStructure.setMessage(message);
		responseStructure.setData(null);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure, HttpStatus.BAD_REQUEST);
	}


	@Override
	public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {

		String employeeId = leave.getEmployeeId();

		Optional<Leave> optional = leaveRepo.findByEmployeeId(employeeId);

		Leave leave2 = optional.get();

		leave2.setLeaveStatus(leave.getLeaveStatus());
		Leave leave3=leaveRepo.save(leave2);

		String message="Employee ID: " + leave.getEmployeeId() + " leave is " + leave.getLeaveStatus();

		ResponseStructure<Leave> responseStructure = new ResponseStructure<Leave>();
		responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
		responseStructure.setMessage(message);
		responseStructure.setData(leave3);

		return new ResponseEntity<ResponseStructure<Leave>>(responseStructure,HttpStatus.ACCEPTED);

	}


    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private LeaveRepo leaveRepo;

    @Override
    public ResponseEntity<ResponseStructure<Leave>> applyLeave(Leave leave) {
        String employeeId = leave.getEmployeeId();

        // Check if employee exists
        if (employeeRepo.findByEmployeeId(employeeId) == null) {
            throw new InvalidEmployeeIdException("The employee-id is invalid");
        }

        // Get the employee data
        Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(employeeId);
        Employee employee = byEmployeeId.get();

        // Get the approver information (assuming approvedBy is a full name)
        String approvedBy = leave.getApprovedBy();
        String[] split = approvedBy.split(" ");
        Optional<Employee> approver = employeeRepo.findByFirstName(split[0]);

        // Set approval information
        leave.setApproved(approver.orElse(null));
        
        Payroll payroll = employee.getPayroll();

        // Get the leave date and month
        LocalDate fromDate = leave.getFromDate();
        int month = fromDate.getMonthValue();
        int maxLeave = (month == Month.FEBRUARY.getValue() || month == Month.MARCH.getValue()) ? 2 : 3;

        // Handle Casual Leave (CL) restriction for February and March
        if (leave.getLeaveType() == LeaveType.CASUAL && (month == Month.FEBRUARY.getValue() || month == Month.MARCH.getValue())) {
            return buildErrorResponse("Casual leave cannot be taken in February or March.");
        }

        // Handle leave limits (sick and paid leave) for February and March
        if ((leave.getLeaveType() == LeaveType.SICK || leave.getLeaveType() == LeaveType.PAID) && leave.getNumberOfDays() > maxLeave) {
            return buildErrorResponse("You can only take " + maxLeave + " " + leave.getLeaveType() + " leave(s) in this month.");
        }

        // Check leave balance
        if (leave.getLeaveType() == LeaveType.SICK && employee.getSlBalance() < leave.getNumberOfDays()) {
            return buildErrorResponse("Insufficient Sick Leave balance.");
        }

        if (leave.getLeaveType() == LeaveType.PAID && employee.getPlBalance() < leave.getNumberOfDays()) {
            return buildErrorResponse("Insufficient Paid Leave balance.");
        }

        // Apply the leave and deduct from the balance
        if (leave.getLeaveType() == LeaveType.SICK) {
            employee.setSlBalance(employee.getSlBalance() - leave.getNumberOfDays());
        } else if (leave.getLeaveType() == LeaveType.PAID) {
            employee.setPlBalance(employee.getPlBalance() - leave.getNumberOfDays());
        }

        // Calculate Loss of Pay (LOP) if applicable
        double lopDeduction = calculateLopDeduction(employee, leave, month);
        if (lopDeduction > 0) {
            payroll.setSalary(employee.getPayroll().getGrossSalary() - lopDeduction);
        }

        // Save updated leave data
        leave = leaveRepo.save(leave);
        employeeRepo.save(employee);

        String message = "Employee ID: " + leave.getEmployeeId() + " applied for " + leave.getLeaveType() + " leave.";
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.CREATED.value());
        responseStructure.setMessage(message);
        responseStructure.setData(leave);

        return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
    }

    private ResponseEntity<ResponseStructure<Leave>> buildErrorResponse(String message) {
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseStructure.setMessage(message);
        responseStructure.setData(null);
        return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);
    }

    private double calculateLopDeduction(Employee employee, Leave leave, int month) {
        int maxLeave = (month == Month.FEBRUARY.getValue() || month == Month.MARCH.getValue()) ? 2 : 3;
        if (leave.getNumberOfDays() > maxLeave) {
            int excessDays = leave.getNumberOfDays() - maxLeave;
            return excessDays * (employee.getPayroll().getGrossSalary() / 30.0); // Assuming 30 days in a month for salary calculation
        }
        return 0;
    }

    @Override
    public ResponseEntity<ResponseStructure<Leave>> changeLeaveStatus(Leave leave) {
        String employeeId = leave.getEmployeeId();

        Optional<Leave> optional = leaveRepo.findByEmployeeId(employeeId);

        if (!optional.isPresent()) {
            return buildErrorResponse("Leave request not found for employee ID: " + employeeId);
        }

        Leave existingLeave = optional.get();
        existingLeave.setLeaveStatus(leave.getLeaveStatus());
        Leave updatedLeave = leaveRepo.save(existingLeave);

        String message = "Employee ID: " + leave.getEmployeeId() + " leave status updated to " + leave.getLeaveStatus();
        ResponseStructure<Leave> responseStructure = new ResponseStructure<>();
        responseStructure.setStatusCode(HttpStatus.ACCEPTED.value());
        responseStructure.setMessage(message);
        responseStructure.setData(updatedLeave);

        return new ResponseEntity<>(responseStructure, HttpStatus.ACCEPTED);
    }

}
