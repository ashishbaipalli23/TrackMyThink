package com.ashi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashi.entities.StudentEntity;
import com.ashi.repository.StudentRepository;

@Service
public class StudentDAO {

	@Autowired
	private StudentRepository repo;
	
	//registration to save student data 
	public void saveStudent(StudentEntity student) throws Exception {
		   repo.save(student);
	}
	
	//login check 
	public StudentEntity loginCheck(String username,String password) {
		return repo.findByEmailAndPassword(username, password);
	}
	
	
	//update profile 
	public void updateProfile(StudentEntity student) {
		repo.save(student);
	}
	
	//get student by id 
	public StudentEntity getStudentById(int studentId) {
		return repo.findById(studentId).get();
	}
	
	//get all students
	public List<StudentEntity> getAllStudents(){
		return repo.findAll();
	}

	
	
	
	
	
	 	
	
}
