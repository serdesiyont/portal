package org.ahavah.portal.dtos.exercise;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class UpdateExerciseDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;
    @NotEmpty
    private String language;
    @NotEmpty
    private Map<String, Object> boilerplate;
    @NotEmpty
    private Map<String, Object> testCases;
//    @NotEmpty
//    private Map<String, Object> output;
    private OffsetDateTime schedule;
}
