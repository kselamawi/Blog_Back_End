package com.programming.SoloMedia.MyBlog.repo;

import com.programming.SoloMedia.MyBlog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
