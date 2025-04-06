package com.pranjal.learning_management_system.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CourseRequestDTO {
    @NotBlank(message = "Course name is required")
    private String courseName;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;
}
