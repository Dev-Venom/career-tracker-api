package com.kumara.careertracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kumara.careertracker.dto.LoginRequestDto;
import com.kumara.careertracker.dto.LoginResponseDto;
import com.kumara.careertracker.dto.UserResponseDto;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PutMapping("/{id}")
	public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {

		User user = userService.findById(id);

		if (user != null) {
			user.setName(updatedUser.getName());
			user.setEmail(updatedUser.getEmail());
			user.setPassword(updatedUser.getPassword());
			user.setRole(updatedUser.getRole());

			return userService.save(user);
		}

		return null;
	}

	@PostMapping("/bulk")
	public List<User> createUsers(@Valid @RequestBody List<User> users) {
		return userService.saveAll(users);
	}

	@PostMapping
	public User createUser(@Valid @RequestBody User user) {
		return userService.save(user);
	}

	@GetMapping
	public List<User> getAllUsers() {
		return userService.findAll();
	}

	@GetMapping("/me")
	public UserResponseDto getCurrentUser(Authentication authentication) {

		return userService.getCurrentUser(authentication.getName());

	}

	@GetMapping("/id{id}")
	public UserResponseDto getUserById(@PathVariable Long id) {

		User user = userService.findById(id);

		return userService.convertToDTO(user);
	}

	@DeleteMapping("/{id}")
	public String deleteUser(@PathVariable Long id) {
		userService.deleteById(id);
		return "User deleted successfully";
	}

	@GetMapping("/email/{email}")
	public User getUserByEmail(@PathVariable String email) {
		return userService.findByEmail(email);
	}

	@GetMapping("/role/{role}")
	public List<User> getUsersByRole(@PathVariable String role) {
		return userService.findByRole(role);
	}

	@PostMapping("/login")
	public LoginResponseDto login(@RequestBody LoginRequestDto dto) {

		return userService.login(dto);
	}

}
