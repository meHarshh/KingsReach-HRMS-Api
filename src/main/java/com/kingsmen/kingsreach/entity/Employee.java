package com.kingsmen.kingsreach.entity;

import java.time.LocalDate;

import com.kingsmen.kingsreach.enums.Department;
import com.kingsmen.kingsreach.enums.EmployeeRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String employeeId;
	private String firstName;
	private String lastName;
	private String userName;
	private EmployeeRole role;
	private String email;
	private String password;
	private String confirmPassword;
	private LocalDate joiningDate;
	private long phoneNumber;
	private Department department;
	
//	Personal Detail
	
	private String aadharCardNumber;
	private String panCardNumber;
	private LocalDate dob;
	private String bloodGroup;
	private String fatherName;
	private String motherName;
	private long fatherContactNumber;
	private long motherContactNumber;
	private String permanentAdres;
	private Long emergencyContact;
	
	

	

	public String getAadharCardNumber() {
		return aadharCardNumber;
	}

	public void setAadharCardNumber(String aadharCardNumber) {
		this.aadharCardNumber = aadharCardNumber;
	}

	public String getPanCardNumber() {
		return panCardNumber;
	}

	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public long getFatherContactNumber() {
		return fatherContactNumber;
	}

	public void setFatherContactNumber(long fatherContactNumber) {
		this.fatherContactNumber = fatherContactNumber;
	}

	public long getMotherContactNumber() {
		return motherContactNumber;
	}

	public void setMotherContactNumber(long motherContactNumber) {
		this.motherContactNumber = motherContactNumber;
	}

	public String getPermanentAdres() {
		return permanentAdres;
	}

	public void setPermanentAdres(String permanentAdres) {
		this.permanentAdres = permanentAdres;
	}

	public Long getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(Long emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public EmployeeRole getRole() {
		return role;
	}

	public void setRole(EmployeeRole role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

}
