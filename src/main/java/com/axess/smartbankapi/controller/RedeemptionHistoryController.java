package com.axess.smartbankapi.controller;

import com.axess.smartbankapi.dynamodb.DynamoDBConfig;
import com.axess.smartbankapi.dynamodb.User;
import com.axess.smartbankapi.dynamodb.UserRepository;
import com.axess.smartbankapi.dynamodb.UserService;
import com.axess.smartbankapi.ses.EMailService;
import com.axess.smartbankapi.ses.Email;
import com.axess.smartbankapi.sqs.SQSService;
import com.axess.smartbankapi.sqs.TestMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.axess.smartbankapi.config.restapi.ApiSuccessResponse;
import com.axess.smartbankapi.dto.UserRedeemptionHistoryDto;
import com.axess.smartbankapi.exception.RecordExistException;
import com.axess.smartbankapi.exception.RecordNotCreatedException;
import com.axess.smartbankapi.exception.RecordNotFoundException;
import com.axess.smartbankapi.service.RedeemptionHistoryService;

import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/history")
@Slf4j
//@Import(DynamoDBConfig.class)
//@EnableDynamoDBRepositories(basePackageClasses = UserRepository.class)
public class RedeemptionHistoryController {

	
	@Autowired
	private RedeemptionHistoryService historyService;

	@Autowired
	SQSService sqsService;

	@Autowired
	EMailService eMailService;

	@Autowired
	UserService userService;

//	@SqsListener(value = "${cloud.aws.end-point.uri-useraccess}",deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	@PostMapping("/")
	public ResponseEntity<?> saveHistory(@RequestBody UserRedeemptionHistoryDto historyDto) throws RecordNotFoundException, RecordExistException, RecordNotCreatedException {

		ApiSuccessResponse response = new ApiSuccessResponse();

		response.setMessage("Successfully added to history. ");
		response.setHttpStatus(String.valueOf(HttpStatus.CREATED));
		response.setHttpStatusCode(201);
		response.setBody(historyService.saveHistory(historyDto));
		response.setError(false);
		response.setSuccess(true);

//		sqs
		sqsService.sendMessage("User jack has placed the order");
		log.info("Message from jack is sent to SQS");

//		ses
		Email email = new Email();
		email.setBody("Thanks for your order");
		email.setFrom("admin@cloudtech-training.com");
		email.setTo("zfireear@gmail.com");
		email.setSubject("order reply");
		eMailService.sendEmail(email);
		log.info("Sent email to the customer thanking for order");

//		dynamndb
		User user = new User();
		user.setEmail("admin@cloudtech-training.com");
		user.setFirstName("jack");
		user.setLastName("Zhang");
		user.setAccessTime(new Date());
		user.setUrlAccessed("/history");
		userService.saveAccessLog(user);
		log.info("User access log record");



		return ResponseEntity.status(HttpStatus.OK).header("status", String.valueOf(HttpStatus.OK))
				.body(response);



	}

	
	
}
