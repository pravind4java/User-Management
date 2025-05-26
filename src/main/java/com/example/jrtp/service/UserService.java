package com.example.jrtp.service;

import java.util.Map;

import com.example.jrtp.dto.QuoteApiResponseDto;
import com.example.jrtp.dto.ResetPasswordDto;
import com.example.jrtp.dto.UserDto;

public interface UserService {

	Map<Integer, String> getCountries();

	Map<Integer, String> getStatesByCountryId(Integer countryId);

	Map<Integer, String> getCitiesByStateId(Integer stateId);

	boolean isEmailUnique(String email);

	boolean isUserRegister(UserDto dto);

	UserDto login(String email, String password);

	boolean isPwdReset(ResetPasswordDto dto);

	QuoteApiResponseDto buildDashboard();
	
	UserDto getUserByEmail(String email);
}
