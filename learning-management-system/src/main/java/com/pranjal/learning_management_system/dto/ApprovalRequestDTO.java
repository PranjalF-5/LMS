package com.pranjal.learning_management_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import models.ApprovalStatus;

@Data
public class ApprovalRequestDTO {
    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Status is required")
    private ApprovalStatus status; // APPROVED/REJECTED
}
