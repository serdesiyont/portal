package org.ahavah.portal.dtos.lesson;


import lombok.Data;
import org.ahavah.portal.dtos.user.UserDto;

import java.time.OffsetDateTime;

@Data
public class LessonDto {
    private Long id;
    private String title;
    private String address;
    private OffsetDateTime schedule;
    private UserDto user;

}
