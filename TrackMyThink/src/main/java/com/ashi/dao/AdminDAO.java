package com.ashi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashi.entities.AdminEntity;
import com.ashi.repository.AdminRepository;

@Service
public class AdminDAO {

	@Autowired
	private AdminRepository repository;
	
	//login check
	public AdminEntity loginCheck(String username,String password) {
		return repository.findByEmailAndPassword(username, password);
	}
	
	//get admin by id
	public AdminEntity getAdminById(Integer adminId) {
		return repository.findById(adminId).get();
	}

	//update admin
	public AdminEntity updateAdmin(AdminEntity adminEntity) {
		return repository.save(adminEntity);
	}
}
