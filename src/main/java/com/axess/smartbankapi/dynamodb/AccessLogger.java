package com.axess.smartbankapi.dynamodb;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccessLogger {

	@Autowired
	UserService userService;

	@Scheduled(fixedDelay = 60000)
	public void logAccess() {

		User user = new User();
		user.setEmail("admin@cloudtech-training.com");
		user.setFirstName("Admin");
		user.setLastName("Class");
		user.setAccessTime(new Date());
		user.setUrlAccessed("/reward");

		userService.saveAccessLog(user);
		log.info("User access log saved");
	}
}
