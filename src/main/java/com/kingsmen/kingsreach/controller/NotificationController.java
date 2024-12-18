package com.kingsmen.kingsreach.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kingsmen.kingsreach.entity.Notification;
import com.kingsmen.kingsreach.service.NotificationService;
import com.kingsmen.kingsreach.util.ResponseStructure;

@RestController
public class NotificationController {
	@Autowired
	private NotificationService notificationService;
	
	@PostMapping(value = "/saveNotification")
	private ResponseEntity<ResponseStructure<Notification>> saveNotification(@RequestBody Notification notification) {
		return notificationService.saveNotification(notification);
	}

	@GetMapping(value = "/fetchAllNotification")
	private ResponseEntity<ResponseStructure<List<Notification>>> findAllNotification(){
		return notificationService.findAllNotification();
	}
	
	@GetMapping(value = "/findEmployeeNotification")
	private ResponseEntity<ResponseStructure<Notification>> fetchNotification(@RequestParam String employeeId){
		return notificationService.fetchNotification(employeeId);
	}
}
