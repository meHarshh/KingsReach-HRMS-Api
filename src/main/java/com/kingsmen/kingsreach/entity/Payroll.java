package com.kingsmen.kingsreach.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class Payroll implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int payrollId;

	@Column(name = "employee_id")
	private String employeeId;

	private String department;
	private double salary;
	private double taxDeduction;
	private double specialAllowance;
	private double employeeStateInsurance;
	private double houseRentAllowance;
	private double providentFund;
	private double grossSalary;
	private double otherAllowance;
	private double employeeProvidentFund;

	@JsonIgnore
	@OneToOne
	private Employee employee;

	public double getEmployeeProvidentFund() {
		return employeeProvidentFund;
	}

	public void setEmployeeProvidentFund(double employeeProvidentFund) {
		this.employeeProvidentFund = employeeProvidentFund;
	}

	public int getPayrollId() {
		return payrollId;
	}

	public void setPayrollId(int payrollId) {
		this.payrollId = payrollId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public double getTaxDeduction() {
		return taxDeduction;
	}

	public void setTaxDeduction(double taxDeduction) {
		this.taxDeduction = taxDeduction;
	}

	public double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance(double specialAllowance) {
		this.specialAllowance = specialAllowance;
	}

	public double getEmployeeStateInsurance() {
		return employeeStateInsurance;
	}

	public void setEmployeeStateInsurance(double employeeStateInsurance) {
		this.employeeStateInsurance = employeeStateInsurance;
	}

	public double getHouseRentAllowance() {
		return houseRentAllowance;
	}

	public void setHouseRentAllowance(double houseRentAllowance) {
		this.houseRentAllowance = houseRentAllowance;
	}

	public double getProvidentFund() {
		return providentFund;
	}

	public void setProvidentFund(double providentFund) {
		this.providentFund = providentFund;
	}

	public double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public double getOtherAllowance() {
		return otherAllowance;
	}

	public void setOtherAllowance(double otherAllowance) {
		this.otherAllowance = otherAllowance;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Payroll(int payrollId, String employeeId, String department, double salary, double taxDeduction,
			double specialAllowance, double employeeStateInsurance, double houseRentAllowance, double providentFund,
			double grossSalary, double otherAllowance, double employeeProvidentFund, Employee employee) {
		super();
		this.payrollId = payrollId;
		this.employeeId = employeeId;
		this.department = department;
		this.salary = salary;
		this.taxDeduction = taxDeduction;
		this.specialAllowance = specialAllowance;
		this.employeeStateInsurance = employeeStateInsurance;
		this.houseRentAllowance = houseRentAllowance;
		this.providentFund = providentFund;
		this.grossSalary = grossSalary;
		this.otherAllowance = otherAllowance;
		this.employeeProvidentFund = employeeProvidentFund;
		this.employee = employee;
	}

	public Payroll() {
	}

}
