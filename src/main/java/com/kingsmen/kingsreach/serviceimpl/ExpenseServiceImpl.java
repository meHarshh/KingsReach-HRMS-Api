package com.kingsmen.kingsreach.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingsmen.kingsreach.entity.Employee;
import com.kingsmen.kingsreach.entity.Expense;
import com.kingsmen.kingsreach.repo.EmployeeRepo;
import com.kingsmen.kingsreach.repo.ExpenseRepo;
import com.kingsmen.kingsreach.service.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private ExpenseRepo expenseRepo;

	@Override
	public Expense addExpense(Expense expense) {

		Optional<Employee> byEmployeeId = employeeRepo.findByEmployeeId(expense.getEmployeeId());

		expense.setEmployee(byEmployeeId.get());

		return expenseRepo.save(expense);
	}

}
