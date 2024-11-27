package com.kingsmen.kingsreach.service;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Expense;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface ExpenseService {

	ResponseEntity<ResponseStructure<Expense>> addExpense(Expense expense);

}
