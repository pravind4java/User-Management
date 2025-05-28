package com.example.jrtp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.jrtp.dto.QuoteApiResponseDto;
import com.example.jrtp.dto.ResetPasswordDto;
import com.example.jrtp.dto.UserDto;
import com.example.jrtp.entity.CityEntity;
import com.example.jrtp.entity.CountryEntity;
import com.example.jrtp.entity.StateEntity;
import com.example.jrtp.entity.UserEntity;
import com.example.jrtp.repository.CityRepository;
import com.example.jrtp.repository.CountryRepository;
import com.example.jrtp.repository.StateRepository;
import com.example.jrtp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private CountryRepository countryRepo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private CityRepository cityRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private EmailService emailService;

	private Random random = new Random();

	@Override
	public Map<Integer, String> getCountries() {
		Map<Integer, String> countryMap = new HashMap<>();
		List<CountryEntity> countriesList = countryRepo.findAll();
		countriesList.forEach(e -> countryMap.put(e.getCountryId(), e.getCountryName()));
		return countryMap;
	}

	@Override
	public Map<Integer, String> getStatesByCountryId(Integer countryId) {
		List<StateEntity> statesList = stateRepo.findByCountryCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		statesList.forEach(e -> stateMap.put(e.getStateId(), e.getStateName()));
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCitiesByStateId(Integer stateId) {
		List<CityEntity> cityList = cityRepo.findByStateStateId(stateId);
		Map<Integer, String> map = new HashMap<>();
		cityList.forEach(e -> map.put(e.getCityId(), e.getCityName()));
		return map;
	}

	@Override
	public boolean isEmailUnique(String email) {
		return userRepo.findByEmail(email) == null;
	}

	@Override
	public boolean isUserRegister(UserDto dto) {
		UserEntity entity = new UserEntity();
		BeanUtils.copyProperties(dto, entity);

		Optional<CountryEntity> countryEntity = countryRepo.findById(dto.getCountryId());
		Optional<StateEntity> stateEntity = stateRepo.findById(dto.getStateId());
		Optional<CityEntity> cityEntity = cityRepo.findById(dto.getCityId());

		if (countryEntity.isPresent()) {
			entity.setCountry(countryEntity.get());
		}
		if (stateEntity.isPresent()) {
			entity.setState(stateEntity.get());
		}
		if (cityEntity.isPresent()) {
			entity.setCity(cityEntity.get());
		}

		String randomGeneratedPassword = generateRandomPassword();
		entity.setPwd(randomGeneratedPassword);

		entity.setPwdUpdated("No");
		UserEntity saveEntity = userRepo.save(entity);

		if (saveEntity.getUserId() != null) {
			// send email
			return emailService.isEmailSend(dto.getEmail(), "Your registration done successfully.",
					"Your temporary password is: " + randomGeneratedPassword);
		} else {
			return false;
		}

	}

	@Override
	public UserDto login(String email, String password) {
		UserEntity entity = userRepo.findByEmailAndPwd(email, password);
		if (entity != null) {
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(entity, dto);
			return dto;
		}
		return null;
	}

	@Override
	public boolean isPwdReset(ResetPasswordDto dto) {
		UserEntity entityByEmail = userRepo.findByEmail(dto.getEmail());
		entityByEmail.setPwd(dto.getNewPwd());
		entityByEmail.setPwdUpdated("Yes");

		userRepo.save(entityByEmail);
		return true;
	}

	@Override
	public QuoteApiResponseDto buildDashboard() {
		String apiUrl = "https://dummyjson.com/quotes/random";
		RestTemplate rt = new RestTemplate();
		ResponseEntity<QuoteApiResponseDto> forEntity = rt.getForEntity(apiUrl, QuoteApiResponseDto.class);
		return forEntity.getBody();
	}

	private String generateRandomPassword() {
		String charPool = "ABCDEFGHIJKLMNOPQRTUVWXYZ0123456789";
		int pwdMaxLength = 5;
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < pwdMaxLength; i++) {
			int index = random.nextInt(charPool.length());
			char charAt = charPool.charAt(index);
			builder.append(charAt);
		}
		return builder.toString();
	}

	@Override
	public UserDto getUserByEmail(String email) {
		UserEntity byEmail = userRepo.findByEmail(email);
		if (byEmail != null) {
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(byEmail, dto);
			return dto;
		}
		return null;
	}
}
