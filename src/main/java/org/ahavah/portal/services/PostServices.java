package org.ahavah.portal.services;

import lombok.AllArgsConstructor;
import org.ahavah.portal.dtos.post.CreatePostRequest;
import org.ahavah.portal.dtos.post.PostDto;
import org.ahavah.portal.dtos.post.UpdatePostRequest;
import org.ahavah.portal.mappers.PostMapper;
import org.ahavah.portal.repositories.PostRepository;
import org.ahavah.portal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostServices {
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostMapper postMapper;



    public PostDto getPostById(Long id) {
        var post = this.postRepository.findById(id).orElse(null);
        return postMapper.toDto(post);

    }


    public PostDto createPost(CreatePostRequest createPostRequest) {
        var user = this.userRepository.findById(createPostRequest.getPostedBy()).orElse(null);
        var post = this.postMapper.toEntity(createPostRequest);
        if (user == null)
            return null;
        post.setPostedBy(user);
        this.postRepository.save(post);
        return postMapper.toDto(post);

    }


    public void deletePostById(Long id) {
        this.postRepository.deleteById(id);
    }

    public PostDto updatePost(UpdatePostRequest updatePostRequest) {

        var post = this.postRepository.findById(updatePostRequest.getId()).orElse(null);
        if (post == null)
            return null;

        post.setTitle(updatePostRequest.getTitle());
        postRepository.save(post);
        return postMapper.toDto(post);
    }






}
