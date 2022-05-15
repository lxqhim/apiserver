package com.axess.smartbankapi.dynamodb;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	/**
	 * localhost:8080/accesslogs/admin@cloudtech-training.com/2022-05-07T00:00:00/2022-05-14T00:00:00
	 * 
	 * @param email
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping(value = "/accesslogs/{email}/{from}/{to}")
	public ResponseEntity<Collection<User>> accessLogs(@PathVariable("email") String email,
			@PathVariable("from") String from, @PathVariable("to") String to) {

		List<User> accessLogs = userService.getUserLogs(email, from, to);
		return new ResponseEntity<Collection<User>>(accessLogs, HttpStatus.OK);
	}

	/**
	 * localhost:8080/accesslogs/admin@cloudtech-training.com
	 * 
	 * @param email
	 * @param sort
	 * @param order
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/accesslogs/{email}")
	public Page<User> getUserAccessLogs(@PathVariable("email") String email,
			@RequestParam(value = "sort", defaultValue = "accessTime") String sort,
			@RequestParam(value = "order", defaultValue = "desc") String order,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "20") int size) {

		log.info("Get access logs");
		Pageable pageable = getPageable(sort, order, page, size);
		return userService.getScrollableUserLogs(email, pageable);

	}

	private Pageable getPageable(String sort, String order, int page, int size) {
		boolean asc = order.contains("asc") ? true : false;
		Sort sortOrder = null;
		if (asc) {
			sortOrder = Sort.by(Sort.Direction.ASC, sort);
		} else {
			sortOrder = Sort.by(Sort.Direction.DESC, sort);
		}

		return PageRequest.of(page, size, sortOrder);
	}
}
