package com.microverse.users.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microverse.users.Responses.LoginResponse;
import com.microverse.users.dto.RefreshTokenDTO;
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
	
	@Autowired
    private UserDetailsService userDetailsService;
	
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
        String jwtToken = jwtService.generateToken(authenticatedUser);
        String jwtRefreshToken = jwtService.generateRefreshToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRefreshToken(jwtRefreshToken);

        return ResponseEntity.ok(loginResponse);
    }
	

	@PostMapping("/refresh")
	public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDto) {
		HashMap<String, String> responseObj = new HashMap<String, String>();
		LoginResponse loginResponse = new LoginResponse();
		try {
			System.out.println("in REFRESH ----------------");
			String userEmail = jwtService.extractUsernameRefresh(refreshTokenDto.getRefreshToken());
			System.out.println(userEmail + "&&&&&&&");
			if(userEmail != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
				if(jwtService.isTokenValidRefresh(refreshTokenDto.getRefreshToken(), userDetails)) {
					String jwtToken = jwtService.generateToken(userDetails);
					String jwtRefreshToken = jwtService.generateRefreshToken(userDetails);
					
			        loginResponse.setToken(jwtToken);
			        loginResponse.setExpiresIn(jwtService.getExpirationTime());
			        loginResponse.setRefreshToken(jwtRefreshToken);

			        return ResponseEntity.ok(loginResponse);
					 
				}
			}
			
			
			
		}
		catch(Exception e) {
//			HashMap<String, String> er = new HashMap<String, String>();
//			er.put("Error", e.getMessage());
			
			return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/test")
	public ResponseEntity<String> userSignUp() {
		return new ResponseEntity<String>("true", HttpStatus.OK);
		
	}
	

}
