package com.axess.smartbankapi.dynamodb;


import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
public interface UserRepository extends CrudRepository<User, UserKey> {
	List<User> findByLastName(String lastName);
	List<User> findAll();
}