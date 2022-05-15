package com.axess.smartbankapi.dynamodb;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "sba-jack-UserAccess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	UserKey key;

	private String email;
	private Date accessTime;
	private String firstName;
	private String lastName;
	private String urlAccessed;

	public User(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@DynamoDBHashKey
	public String getEmail() {
		return email;
	}

	@DynamoDBRangeKey
	public Date getAccessTime() {
		return accessTime;
	}

	@DynamoDBAttribute
	public String getFirstName() {
		return firstName;
	}

	@DynamoDBAttribute
	public String getLastName() {
		return lastName;
	}

	@DynamoDBAttribute
	public String getUrlAccessed() {
		return urlAccessed;
	}

}