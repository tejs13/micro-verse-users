package com.microverse.users.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microverse.users.dto.UserLoginDTO;
import com.microverse.users.dto.UserRegistrationDTO;
import com.microverse.users.models.UsersData;
import com.microverse.users.repositories.UserRepository;



@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	 public UsersData signUp(UserRegistrationDTO input) {
		 UsersData user = new UsersData();
		 user.setFirstName(input.getFirstName());
		 user.setLastName(input.getLastName());
		 user.setEmail(input.getEmail());
		 user.setPassword(passwordEncoder.encode(input.getPassword()));
	        return userRepository.save(user);
	    }
	 
	 public UsersData authenticate(UserLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
	    }

}
