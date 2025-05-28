package com.example.jrtp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.jrtp.constants.AppConstants;
import com.example.jrtp.dto.QuoteApiResponseDto;
import com.example.jrtp.dto.ResetPasswordDto;
import com.example.jrtp.dto.UserDto;
import com.example.jrtp.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index(Model model) {
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		return AppConstants.INDEX_PAGE;
	}

	@PostMapping("/login")
	public String login(@ModelAttribute("user") UserDto userDto, Model model) {
		UserDto loginUser = userService.login(userDto.getEmail(), userDto.getPwd());
		if (loginUser == null) {
			model.addAttribute("emsg", "Invalid Credentials.");
			return AppConstants.INDEX_PAGE;
		}
		String pwdUpdated = loginUser.getPwdUpdated();
		if (pwdUpdated.equalsIgnoreCase("No")) {
			ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
			resetPasswordDto.setEmail(userDto.getEmail());
			model.addAttribute("resetPassword", resetPasswordDto);
			return "resetPwd";
		} else {
			QuoteApiResponseDto dashboardDto = userService.buildDashboard();
			model.addAttribute("dashboardInfo", dashboardDto);
			return "dashboard";
		}
	}

	@GetMapping("/register")
	public String register(Model model) {
		UserDto userDto = new UserDto();
		model.addAttribute("user", userDto);
		model.addAttribute("countries", userService.getCountries());
		return "register";
	}

	@GetMapping("/states")
	// Return data in JSON format.
	@ResponseBody
	public Map<Integer, String> getStates(@RequestParam Integer countryId) {
		return userService.getStatesByCountryId(countryId);
	}

	@GetMapping("/cities")
	@ResponseBody
	public Map<Integer, String> getCities(@RequestParam Integer stateId) {
		return userService.getCitiesByStateId(stateId);
	}

	@PostMapping("/register")
	public String handleRegistration(@ModelAttribute("user") UserDto user, Model model) {
		boolean emailUnique = userService.isEmailUnique(user.getEmail());
		if (!emailUnique) {
			model.addAttribute("emsg", "Duplicate Email found.");
		}

		boolean userRegister = userService.isUserRegister(user);
		if (userRegister) {
			model.addAttribute("smsg", "Registration Success, please check your email for login details.");
		} else {
			model.addAttribute("emsg", "Registration failed.");
		}
		model.addAttribute("countries", userService.getCountries());
		return "register";
	}

	@PostMapping("/resetPwd")
	public String resetPassword(@ModelAttribute("resetPassword") ResetPasswordDto dto, Model model) {
		UserDto userByEmail = userService.getUserByEmail(dto.getEmail());
		if (userByEmail != null && !userByEmail.getPwd().equals(dto.getOldPwd())) {
			model.addAttribute("emsg", "Old password is incorrect.");
		}

		if (!dto.getConfirmPwd().equals(dto.getNewPwd())) {
			model.addAttribute("emsg", "New and confirm password is not same.");
		}

		boolean pwdReset = userService.isPwdReset(dto);
		if (pwdReset) {
			model.addAttribute("smsg", "Password updated.");
		}
		return "resetPwd";
	}

	@GetMapping("/new-quote")
	public String getQuote(Model model) {
		QuoteApiResponseDto dashboardDto = new QuoteApiResponseDto();
		model.addAttribute("dashboardInfo", dashboardDto);
		return "dashboard";
	}

}
