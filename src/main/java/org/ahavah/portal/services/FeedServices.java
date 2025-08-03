package org.ahavah.portal.services;

import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.post.PostDto;
import org.ahavah.portal.entities.Post;
import org.ahavah.portal.mappers.PostMapper;
import org.ahavah.portal.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class FeedServices {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    public List<PostDto> getPostsByDivision(String division) {
        var posts = this.postRepository.findPostByDivisionEqualsIgnoreCase(division);
        return posts.stream().map(postMapper::toDto).toList();
    }

    public List<PostDto> getPostsByScheduleBefore(OffsetDateTime schedule) {
        var posts = this.postRepository.findPostByScheduleBefore(schedule);
        return posts.stream().map(postMapper::toDto).toList();
    }

}
