package com.example.demo.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.security.entities.AppRole;

public interface AppRoleRepository  extends JpaRepository<AppRole, Long>{
	AppRole findByRoleName(String roleName);

}
