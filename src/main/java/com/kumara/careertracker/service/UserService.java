package com.kumara.careertracker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kumara.careertracker.config.JwtUtil;
import com.kumara.careertracker.dto.LoginRequestDto;
import com.kumara.careertracker.dto.LoginResponseDto;
import com.kumara.careertracker.dto.UserRequestDto;
import com.kumara.careertracker.dto.UserResponseDto;
import com.kumara.careertracker.entity.User;
import com.kumara.careertracker.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	public User save(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	public List<User> saveAll(List<User> users) {

		users.forEach(user -> {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		});

		return userRepository.saveAll(users);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public User findByEmail(String email) {

		return userRepository.findByEmail(email).orElse(null);
	}

	public List<User> findByRole(String role) {
		return userRepository.findByRole(role);
	}

	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	public UserResponseDto convertToDTO(User user) {

		UserResponseDto dto = new UserResponseDto();

		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());
		dto.setRole(user.getRole());

		return dto;
	}

	public LoginResponseDto login(LoginRequestDto dto) {

		Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

		if (optionalUser.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		}

		User user = optionalUser.get();

		boolean matched = passwordEncoder.matches(dto.getPassword(), user.getPassword());

		if (!matched) {
			return new LoginResponseDto(null, "Invalid password", null);
		}

		String token = jwtUtil.generateToken(user.getEmail());

		UserResponseDto userDto = convertToDTO(user);

		LoginResponseDto response = new LoginResponseDto(token, "Login successful", userDto);

		System.out.println("Response User = " + response.getUser());

		return response;
	}

	public UserResponseDto getCurrentUser(String email) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		return convertToDTO(user);
	}

	public UserResponseDto createUser(UserRequestDto dto) {

		User user = new User();

		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(dto.getRole());

		User savedUser = userRepository.save(user);

		return convertToDTO(savedUser);
	}

}