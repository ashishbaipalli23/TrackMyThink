package com.ashi.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id", columnDefinition = "INT") //explicitly signed
    private Integer examId;
    
    @Column(nullable = false, length = 100)
    private String examTitle;

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer durationMinutes; // in minutes

    @Column(nullable = false)
    private LocalDate examDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false,name = "ACTIVE_SW")
	private Boolean isActive = false;
    
    @Column(name = "PUBLISHED_SW" )
    private Boolean isPublished = false;
    
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminEntity admin;

    @CreationTimestamp
	@Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionEntity> questions = new ArrayList<>();
}
