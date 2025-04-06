package com.pranjal.learning_management_system.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SyllabusDTO {
    private String content;
    private LocalDateTime lastUpdated;
}
