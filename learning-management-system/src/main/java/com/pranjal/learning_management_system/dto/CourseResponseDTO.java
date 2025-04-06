package com.pranjal.learning_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.CourseStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseDTO {
    private Long courseId;
    private String name;
    private String description;
    private CourseStatus status;
    private Long instructorId;
    private String instructorName;
}
