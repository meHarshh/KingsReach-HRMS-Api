package com.kingsmen.kingsreach.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kingsmen.kingsreach.entity.Notification;

public interface NotificationRepo extends JpaRepository<Notification, Integer>{

	List<Notification> findByEmployeeId(String employeeId);

}
