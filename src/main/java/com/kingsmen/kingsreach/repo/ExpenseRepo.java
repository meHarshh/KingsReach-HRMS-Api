package com.kingsmen.kingsreach.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Expense;

public interface ExpenseRepo extends JpaRepository<Expense, Integer> {

}
