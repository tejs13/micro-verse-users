package com.microverse.users.models;

import jakarta.persistence.*;


@Entity
@Table(name="UserDetails")
public class UserDetails {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "address")
	private String address;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "UsersData_id", nullable = false)
	private UsersData user;
	

	public UsersData getUser() {
		return user;
	}

	public void setUser(UsersData user) {
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public UserDetails(){
		
	}

	public UserDetails(long id, String phone, String address, UsersData userData) {
		super();
		this.id = id;
		this.phone = phone;
		this.address = address;
		this.user = userData;
	}

}
