package com.axess.smartbankapi.dynamodb;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableScan
public interface PagingUserRepository extends PagingAndSortingRepository<User, UserKey> {
	
	Page<User> findByEmail(String email, Pageable pageable);

	@EnableScanCount
	Page<User> findAll(Pageable pageable);
}