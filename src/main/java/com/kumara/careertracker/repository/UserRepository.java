package com.kumara.careertracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kumara.careertracker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByRole(String role);

	User findByName(String name);

	Optional<User> findByEmail(String email);

}