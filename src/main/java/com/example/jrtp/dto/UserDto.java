package com.example.jrtp.dto;

import lombok.Data;

@Data
public class UserDto {

	private Integer userId;
	
	private String userName;
	
	private String email;
	
	private String pwd;
	
	private String pwdUpdated;
	
	private Long phoneNo;
	
	private Integer countryId;
	
	private Integer stateId;
	
	private Integer cityId;
}
