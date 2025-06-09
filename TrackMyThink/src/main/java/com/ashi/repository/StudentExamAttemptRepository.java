package com.ashi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ashi.entities.StudentEntity;
import com.ashi.entities.StudentExamAttemptEntity;

public interface StudentExamAttemptRepository extends JpaRepository<StudentExamAttemptEntity, Integer> {


    List<StudentExamAttemptEntity> findByStudent(StudentEntity student);

    @Query("SELECT AVG(a.score) FROM StudentExamAttemptEntity a WHERE a.student = :student")
    Double findAverageScoreByStudent(@Param("student") StudentEntity student);

	List<StudentExamAttemptEntity> findByStudent_StudentId(Integer studentId);

	Boolean existsByStudent_StudentIdAndExam_ExamId(Integer studentId, Integer examId);

	List<StudentExamAttemptEntity> findByExam_ExamId(Integer examId);

    
    
}
