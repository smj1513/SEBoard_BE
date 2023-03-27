package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

}