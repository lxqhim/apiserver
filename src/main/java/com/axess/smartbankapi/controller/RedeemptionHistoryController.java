package com.axess.smartbankapi.controller;

import com.axess.smartbankapi.ses.EMailService;
import com.axess.smartbankapi.ses.Email;
import com.axess.smartbankapi.sqs.SQSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@CrossOrigin
@RequestMapping("/history")
@Slf4j
public class RedeemptionHistoryController {

	
	@Autowired
	private RedeemptionHistoryService historyService;

	@Autowired
	SQSService sqsService;

	@Autowired
	EMailService eMailService;
	
	@PostMapping("/")
	public ResponseEntity<?> saveHistory(@RequestBody UserRedeemptionHistoryDto historyDto) throws RecordNotFoundException, RecordExistException, RecordNotCreatedException {

		sqsService.sendMessage("User jack has placed the order");
		log.info("Message from jack is sent to SQS");

		ApiSuccessResponse response = new ApiSuccessResponse();

		response.setMessage("Successfully added to history. ");
		response.setHttpStatus(String.valueOf(HttpStatus.CREATED));
		response.setHttpStatusCode(201);
		response.setBody(historyService.saveHistory(historyDto));
		response.setError(false);
		response.setSuccess(true);

		Email email = new Email();
		email.setBody("Thanks for your order");
		email.setFrom("admin@cloudtech-training.com");
		email.setTo("zfireear@gmail.com");
		email.setSubject("order reply");
		eMailService.sendEmail(email);
		log.info("Sent email to the customer thanking for order");


		return ResponseEntity.status(HttpStatus.OK).header("status", String.valueOf(HttpStatus.OK))
				.body(response);



	}

	
	
}
