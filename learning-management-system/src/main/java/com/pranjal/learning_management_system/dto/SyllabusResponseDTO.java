package com.pranjal.learning_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusResponseDTO {
    private Long syllabusId;
    private String content;
    private LocalDateTime lastUpdated;
    private Long courseId;
    private String courseName;
} 