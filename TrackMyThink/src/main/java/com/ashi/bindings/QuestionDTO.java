package com.ashi.bindings;

import lombok.Data;

@Data
public class QuestionDTO {
	
    private Integer questionId;

 
    private ExamDTO exam;

    
    private String questionText;

   
    private String optionA;

   
    private String optionB;

    private String optionC;


    private String optionD;

    
    private String correctOption; // A, B, C, or D
}
