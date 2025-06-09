package com.ashi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashi.entities.ExamEntity;

public interface ExamRepository extends JpaRepository<ExamEntity, Integer> {
	List<ExamEntity> findAllByOrderByExamDateAsc();

	List<ExamEntity> findByIsActiveFalseOrderByExamDateAsc();

	List<ExamEntity> findByIsActiveTrueOrderByExamDateAsc();

	List<ExamEntity> findByIsPublishedTrueOrderByExamDateAsc();

	List<ExamEntity> findByIsActiveTrueAndIsPublishedFalseOrderByExamDateAsc();

	List<ExamEntity> findByIsActiveTrueAndIsPublishedTrueOrderByExamDateAsc();

	List<ExamEntity> findByIsActiveTrueAndIsPublishedTrueAndExamDateGreaterThanEqualOrderByExamDateAsc(LocalDate date);
}
