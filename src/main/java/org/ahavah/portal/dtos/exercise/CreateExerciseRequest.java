package org.ahavah.portal.dtos.exercise;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.ahavah.portal.validators.LangSubset;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class CreateExerciseRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @LangSubset
    private String language;
    @NotEmpty
    private Map<String, Object> boilerplate;
    @NotEmpty
    private Map<String, Object> testCases;

    private OffsetDateTime schedule;


}
