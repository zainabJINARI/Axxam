package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.ServiceA;

public interface ServiceaRepository  extends JpaRepository<ServiceA,Long>{
	public List<ServiceA> findByAnnouncementId(String id);

}
