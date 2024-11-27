package com.kingsmen.kingsreach.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Expense;
import com.kingsmen.kingsreach.service.ExpenseService;

@RestController
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;
	
	@PostMapping(value = "/addExpense")
	private Expense addExpense(Expense expense) {
		return expenseService.addExpense(expense);
	}
	
	
}
