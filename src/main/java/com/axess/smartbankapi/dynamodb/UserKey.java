package com.axess.smartbankapi.dynamodb;

import java.io.Serializable;
import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBDocument
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKey implements Serializable {

	@DynamoDBHashKey
	private String email;
	@DynamoDBRangeKey
	private Date accessTime;
}
