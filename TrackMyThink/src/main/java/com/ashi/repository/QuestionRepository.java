package com.ashi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ashi.entities.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer>{
		
	int countByExam_ExamId(Integer examId);

	List<QuestionEntity> findByExam_ExamId(Integer examId);
}
