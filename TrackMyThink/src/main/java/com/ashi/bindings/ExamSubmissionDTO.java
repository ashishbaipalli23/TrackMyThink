package com.ashi.bindings;


import lombok.Data;

import java.util.Map;

@Data
public class ExamSubmissionDTO {
    private Integer examId;
    private Map<Integer, String> answers; // key = question index, value = selected option (A, B, C, D)
}
