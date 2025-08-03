package org.ahavah.portal.contollers;

import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.post.PostDto;
import org.ahavah.portal.services.FeedServices;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/feed")
@AllArgsConstructor
public class FeedController {

    private final FeedServices feedServices;

    @GetMapping("/list")
    public List<PostDto> getPostsByDivision(
            @RequestParam String division
    ) {
        return this.feedServices.getPostsByDivision(division);
    }

    @PostMapping
    public List<PostDto> getPostsByScheduleBefore(
           @RequestParam OffsetDateTime schedule) {
        return this.feedServices.getPostsByScheduleBefore(schedule);
    }
}
