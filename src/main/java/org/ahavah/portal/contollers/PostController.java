package org.ahavah.portal.contollers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.post.CreatePostRequest;
import org.ahavah.portal.dtos.post.PostDto;
import org.ahavah.portal.dtos.post.UpdatePostRequest;
import org.ahavah.portal.services.PostServices;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/post")
@AllArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PostController {
    private final PostServices postServices;



    @GetMapping("{id}")
    public PostDto getPostById(
            @PathVariable Long id
    ){
        return this.postServices.getPostById(id);
    }

    @PostMapping
    public PostDto createPost(
          @Valid @RequestBody CreatePostRequest createPostRequest
    ){
        return this.postServices.createPost(createPostRequest);
    }

    @PutMapping
    public PostDto updatePost(
            @Valid   @RequestBody UpdatePostRequest updatePostRequest
    ){
        return this.postServices.updatePost(updatePostRequest);
    }

}
