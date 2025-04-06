package com.pranjal.learning_management_system.dto;

import org.hibernate.validator.constraints.NotBlank;
import lombok.Data;

@Data
public class SyllabusUpdateDTO {
    @NotBlank(message = "Content cannot be blank")
    private String content;
}
