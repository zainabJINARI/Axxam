package com.example.demo.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.security.entities.AppUser;

public interface AppUserRepository  extends JpaRepository<AppUser, Long>{
	AppUser findByUsername(String username);
	AppUser findByEmail(String email);
}
