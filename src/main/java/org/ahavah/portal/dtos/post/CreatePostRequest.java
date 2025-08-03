package org.ahavah.portal.dtos.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ahavah.portal.validators.DivisionSubset;
import java.time.OffsetDateTime;

@Data
public class CreatePostRequest {
    @NotBlank
    String title;

    String pdfTitle;
    String pdfLink;
    String videoTitle;
    String videoLink;


    @DivisionSubset
    String division;

    OffsetDateTime schedule;


    Long postedBy;

}
