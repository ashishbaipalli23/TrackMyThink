package com.ashi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ashi.entities.AdminEntity;

@Repository
public interface AdminRepository  extends JpaRepository<AdminEntity, Integer>{

	AdminEntity findByEmailAndPassword(String username, String password);
	

}
