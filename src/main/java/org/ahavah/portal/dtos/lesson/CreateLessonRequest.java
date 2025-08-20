package org.ahavah.portal.dtos.lesson;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ahavah.portal.validators.DivisionSubset;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Data
public class CreateLessonRequest {
    @NotBlank
    String title;

    OffsetDateTime schedule;



}
