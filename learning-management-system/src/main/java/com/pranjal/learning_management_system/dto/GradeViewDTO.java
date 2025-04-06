package com.pranjal.learning_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeViewDTO {
    private Long gradeId;
    private Integer score;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseName;
} 