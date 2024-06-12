package com.microverse.users.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.microverse.users.models.UsersData;


/**
 * 
 */

@Repository
public interface UserRepository extends JpaRepository<UsersData, Long> {
	
	Optional<UsersData> findByEmail(String email);
	Boolean existsByEmail(String email);
	

}
