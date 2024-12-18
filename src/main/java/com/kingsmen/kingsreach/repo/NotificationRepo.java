package com.kingsmen.kingsreach.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Integer>{

	Notification findByEmployeeId(String employeeId);

}
