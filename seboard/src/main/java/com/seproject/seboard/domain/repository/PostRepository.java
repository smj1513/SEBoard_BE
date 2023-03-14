package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {

}
