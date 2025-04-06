package com.pranjal.learning_management_system.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Data;
import models.CourseStatus;

@Data
public class CourseStatusUpdateDTO {
    @NotNull(message = "Status is required")
    private CourseStatus status;
}
