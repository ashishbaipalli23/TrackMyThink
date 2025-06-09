package com.ashi.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashi.entities.QuestionEntity;
import com.ashi.repository.QuestionRepository;

@Service
public class QuestionDAO {

	@Autowired
	private QuestionRepository repo;
	
	public void saveQuestion(QuestionEntity question) {
		repo.save(question);
	}
	
	public void deleteQuestion(int questionId) {
		repo.deleteById(questionId);
	}

	public int countByExamId(Integer examId) {
		
		return repo.countByExam_ExamId(examId);
	}

	public List<QuestionEntity> getQuestionsByExamId(Integer examId) {
		
		List<QuestionEntity> questions = repo.findByExam_ExamId(examId);
		return questions;
	}
	
	
	
}
