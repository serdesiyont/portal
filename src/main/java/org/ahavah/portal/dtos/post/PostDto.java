package org.ahavah.portal.dtos.post;

import lombok.Data;
import org.ahavah.portal.dtos.user.UserDto;
import org.ahavah.portal.entities.User;


@Data
public class PostDto {

    String title;
    String pdfTitle;
    String pdfLink;
    String videoTitle;
    String videoLink;
    UserDto postedBy;

}
