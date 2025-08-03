package org.ahavah.portal.repositories;

import org.ahavah.portal.dtos.post.CreatePostRequest;
import org.ahavah.portal.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {


    List<Post> findPostByDivisionEqualsIgnoreCase(String division);

    List<Post> findPostByScheduleBefore(OffsetDateTime schedule);
}
