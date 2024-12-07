package com.kingsmen.kingsreach.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int attendanceId;

	private int totalWorkMinutes;
	private LocalDateTime firstPunchIn;
	private LocalDateTime lastPunchOut;
	private boolean isPresent;
	private boolean isAbsent;
	private String location;
	private int totalBreakTime;

	private String employeeId;

	@JsonIgnore
	@ManyToOne
	private Employee employee;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(int attendanceId) {
		this.attendanceId = attendanceId;
	}

	public int getTotalWorkMinutes() {
		return totalWorkMinutes;
	}

	public void setTotalWorkMinutes(int totalWorkMinutes) {
		this.totalWorkMinutes = totalWorkMinutes;
	}

	public LocalDateTime getFirstPunchIn() {
		return firstPunchIn;
	}

	public void setFirstPunchIn(LocalDateTime firstPunchIn) {
		this.firstPunchIn = firstPunchIn;
		updateAttendanceStatus();
	}

	private void updateAttendanceStatus() {
		if (this.firstPunchIn != null) {
			this.isPresent = true;
			this.isAbsent = false;
		} else {
			this.isPresent = false;
			this.isAbsent = true;
		}
	}

	public LocalDateTime getLastPunchOut() {
		return lastPunchOut;
	}

	public void setLastPunchOut(LocalDateTime lastPunchOut) {
		this.lastPunchOut = lastPunchOut;
	}

	public boolean getIsPresent() {
		return isPresent;
	}

	public void setIsPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	public boolean getIsAbsent() {
		return isAbsent;
	}

	public void setIsAbsent(boolean isAbsent) {
		this.isAbsent = isAbsent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	public void setAbsent(boolean isAbsent) {
		this.isAbsent = isAbsent;
	}

	public int getTotalBreakTime() {
		return totalBreakTime;
	}

	public void setTotalBreakTime(int totalBreakTime) {
		this.totalBreakTime = totalBreakTime;
	}

}
