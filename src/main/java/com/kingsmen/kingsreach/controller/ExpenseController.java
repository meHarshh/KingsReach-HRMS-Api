package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Expense;
import com.kingsmen.kingsreach.service.ExpenseService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@CrossOrigin(allowCredentials = "true", origins = "https://hrms.kingsmenrealty.com")
@RestController
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;
	
	@PostMapping(value = "/addExpense")
	public ResponseEntity<ResponseStructure<Expense>> addExpense(@RequestBody Expense expense) {
		return expenseService.addExpense(expense);
	}
	
	@GetMapping(value = "/findAllExpense")
	private ResponseEntity<ResponseStructure<List<Expense>>> findAllExpense()
	{
		return expenseService.findAllExpense();
	}
	
}
