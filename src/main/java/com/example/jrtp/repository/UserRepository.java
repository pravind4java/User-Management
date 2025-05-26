package com.example.jrtp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jrtp.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findByEmail(String email);

	UserEntity findByEmailAndPwd(String email, String pwd);
}
