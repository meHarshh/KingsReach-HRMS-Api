package com.kingsmen.kingsreach.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.util.ResponseStructure;

public interface NotificationService {

	ResponseEntity<ResponseStructure<Notification>> saveNotification(Notification notification);

	ResponseEntity<ResponseStructure<List<Notification>>> findAllNotification();

	ResponseEntity<ResponseStructure<Notification>> fetchNotification(String employeeId);

}
