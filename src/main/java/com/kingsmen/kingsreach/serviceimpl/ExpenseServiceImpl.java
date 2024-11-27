package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Expense;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.ExpenseRepo;
import com.kingsmen.kingsreach.service.ExpenseService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private ExpenseRepo expenseRepo;

	@Override
	public ResponseEntity<ResponseStructure<Expense>> addExpense(Expense expense) {
		
		System.out.println(expense.getEmployeeName());

		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(expense.getEmployeeId());

		expense.setEmployee(byEmployeeId.get());

		expense=expenseRepo.save(expense);

		ResponseStructure<Expense> responseStructure = new ResponseStructure<Expense>();
		responseStructure.setStatusCode(HttpStatus.CREATED.value());
		responseStructure.setMessage(expense.getEmployeeName() + " expenses are added");
		responseStructure.setData(expense);

		return new ResponseEntity<ResponseStructure<Expense>>(responseStructure, HttpStatus.CREATED);
	}

}
