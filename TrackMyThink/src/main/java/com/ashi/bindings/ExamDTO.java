package com.ashi.bindings;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExamDTO {

    
    private Integer examId;

   
    private String examTitle;

   
    private String subject;

   
    private Integer totalQuestions;

    
    private Integer durationMinutes; // in minutes

    private LocalDate examDate;

   
    private LocalTime startTime;

   
    private LocalTime endTime;

    private Boolean isActive = false;
   
    private Boolean isPublished = false;
    
    private Admin admin;

   
    private LocalDateTime createdAt;
    
    private List<QuestionDTO> questions = new ArrayList<>();
}
