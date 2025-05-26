package com.example.jrtp.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {

	private String email;
	
	private String oldPwd;
	
	private String newPwd;
	
	private String confirmPwd;
}
