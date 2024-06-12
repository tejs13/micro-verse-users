package com.microverse.users.models;

import java.util.List;

import jakarta.persistence.*;



@Entity
@Table(name="UsersData", uniqueConstraints = {
		 @UniqueConstraint(columnNames = "email"),
})

public class UsersData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@Column(name = "first_name", nullable=false)
	private String firstName;
	
	@Column(name = "last_name", nullable=false)
	private String lastName;
	
	@Column(name = "email", nullable=false)
	private String email;
	
	

	
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserDetails> userDetails;
	
	public List<UserDetails> getUserDetails() {
		return userDetails;
	}


	public void setUserDetails(List<UserDetails> userDetails) {
		this.userDetails = userDetails;
	}
	


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public UsersData() {
		
	}
	
	public UsersData(long id, String firstName, String lastName, String em) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = em;
		
	}
	
	
	
	
	
	
	
	

}
