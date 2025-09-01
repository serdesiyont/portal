package org.ahavah.portal.dtos.exercise;

import lombok.Data;
import org.ahavah.portal.dtos.user.UserDto;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class ExerciseAllDto {

    private Long id;
    private String title;
    private String description;
    private String language;
    private Map<String, Object> boilerplate;
    private UserDto user;
    private Map<String, Object> testCases;
    private Map<String, Object> output;
    private OffsetDateTime schedule;

}
