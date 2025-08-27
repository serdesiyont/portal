package org.ahavah.portal.dtos.exercise;

import lombok.Data;
import org.ahavah.portal.dtos.user.UserDto;

import java.util.Map;

@Data
public class ExerciseDto {
    private Long id;
    private String title;
    private String description;
    private String language;
    private Map<String, Object> boilerplate;
    private UserDto user;

}
