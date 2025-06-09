package com.ashi.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashi.entities.ExamEntity;
import com.ashi.repository.ExamRepository;

@Service
public class ExamDAO {

	@Autowired
	private ExamRepository repo;

	public List<ExamEntity> getAllExams() {
		return repo.findAll();
	}

	public ExamEntity getExamById(int examId) {
		return repo.findById(examId).get();
	}

	public void saveExam(ExamEntity exam) {
		repo.save(exam);
	}

	public void deleteExam(int examId) {
		repo.deleteById(examId);
	}

	public void updateExam(ExamEntity exam) {
		repo.save(exam);
	}

	public List<ExamEntity> getAllExamsSortedByDate() {
		return repo.findAllByOrderByExamDateAsc();
	}

	public List<ExamEntity> findByIsActiveFalseOrderByExamDateAsc() {

		return repo.findByIsActiveFalseOrderByExamDateAsc();
	}

	public List<ExamEntity> findByIsActiveTrueOrderByExamDateAsc() {
		// TODO Auto-generated method stub
		return repo.findByIsActiveTrueOrderByExamDateAsc();
	}

	public List<ExamEntity> findByIsPublishedTrueOrderByExamDateAsc() {
		// TODO Auto-generated method stub
		return repo.findByIsPublishedTrueOrderByExamDateAsc();
	}

	public List<ExamEntity> findByIsActiveTrueAndIsPublishedFalseOrderByExamDateAsc() {

		return repo.findByIsActiveTrueAndIsPublishedFalseOrderByExamDateAsc();
	}

	public List<ExamEntity> findByIsActiveTrueAndIsPublishedTrueAndExamDateGreaterThanEqualOrderByExamDateAsc(
			LocalDate date) {

		return repo.findByIsActiveTrueAndIsPublishedTrueAndExamDateGreaterThanEqualOrderByExamDateAsc(date);
	}

	public List<ExamEntity> findByIsActiveTrueAndIsPublishedTrueOrderByExamDateAsc() {

		return repo.findByIsActiveTrueAndIsPublishedTrueOrderByExamDateAsc();
	}

}
