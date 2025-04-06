package com.pranjal.learning_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeResponseDTO {
    private Long gradeId;
    private Integer score;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;
}
