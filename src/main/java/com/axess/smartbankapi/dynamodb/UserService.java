package com.axess.smartbankapi.dynamodb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author rajesh
 *
 */
@Service
@Slf4j
public class UserService {

	@Autowired
	private UserRepository repository;
	@Autowired
	PagingUserRepository pagingUserRepository;

	@Autowired
	AmazonDynamoDB amazonDynamoDB;

	public void saveAccessLog(User user) {
		repository.save(user);
	}

	/**
	 * 
	 * @param email
	 * @param from
	 * @param to
	 * @return
	 */
	public List<User> getUserLogs(String email, String from, String to) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date fromDate = new Date();// some initialization
		Date toDate = new Date();
		try {
			fromDate = sdf.parse(from);
			toDate = sdf.parse(to);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Oops... Server could not process your request.");
		}
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
		int numberOfThreads = 4;
		Map<String, String> expressionAttributesNames = new HashMap<>();
		expressionAttributesNames.put("#timestamp", "accessTime");
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":email", new AttributeValue().withS(email));
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		expressionAttributeValues.put(":from", new AttributeValue().withS(dateFormatter.format((Date) fromDate)));
		expressionAttributeValues.put(":to", new AttributeValue().withS(dateFormatter.format((Date) toDate)));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("email = :email AND #timestamp BETWEEN :from AND :to")
				.withExpressionAttributeNames(expressionAttributesNames)
				.withExpressionAttributeValues(expressionAttributeValues);

		List<User> accessLogs = mapper.parallelScan(User.class, scanExpression, numberOfThreads);
		log.info(String.format("received request for %s  # %d access log events: ", email, accessLogs.size()));
		return accessLogs;
	}

	public Page<User> getScrollableUserLogs(String email, Pageable pageable) {
		return pagingUserRepository.findByEmail(email, pageable);
	}
}
