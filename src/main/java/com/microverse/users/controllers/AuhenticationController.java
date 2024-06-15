package com.microverse.users.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microverse.users.Responses.LoginResponse;
import com.microverse.users.dto.UserLoginDTO;
import com.microverse.users.dto.UserRegistrationDTO;
import com.microverse.users.models.UsersData;
import com.microverse.users.security.jwtservices.JwtService;
import com.microverse.users.services.AuthenticationService;

import io.jsonwebtoken.io.Decoders;



@RestController
@CrossOrigin(origins = "http://localhost:5200")
@RequestMapping("/api/v1/auth")
public class AuhenticationController {
	
	
	@Autowired
	private JwtService jwtService;
    
	@Autowired
    private AuthenticationService authenticationService;
	
	// get all employees
	@PostMapping("/signup")
	public ResponseEntity<UsersData> test(@RequestBody UserRegistrationDTO userRegistrationDTO) {
		 UsersData registeredUser = authenticationService.signUp(userRegistrationDTO);

	    return ResponseEntity.ok(registeredUser);
//		return new ResponseEntity<String>("true", HttpStatus.OK);
		
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody UserLoginDTO UserLoginDto) {
		UsersData authenticatedUser = authenticationService.authenticate(UserLoginDto);
		byte[] keyBytes = Decoders.BASE64.decode("bWljcm92ZXJzZQ==");
        String jwtToken = jwtService.generateToken(authenticatedUser);
        

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
	
	
	
	@GetMapping("/test")
	public ResponseEntity<String> userSignUp() {
		return new ResponseEntity<String>("true", HttpStatus.OK);
		
	}
	

}
