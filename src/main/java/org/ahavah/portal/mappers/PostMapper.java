package org.ahavah.portal.mappers;

import org.ahavah.portal.dtos.post.CreatePostRequest;
import org.ahavah.portal.dtos.post.PostDto;
import org.ahavah.portal.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target="postedBy", ignore=true)
    Post toEntity(CreatePostRequest createPostRequest);
    PostDto toDto(Post post);
}
