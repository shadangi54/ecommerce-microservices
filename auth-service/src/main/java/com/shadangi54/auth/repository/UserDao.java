package com.shadangi54.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shadangi54.auth.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>{
	
	Optional<User> findByUserName(String userName);
	Boolean existsByUserName(String userName);
	Boolean existsByEmail(String email);
}
