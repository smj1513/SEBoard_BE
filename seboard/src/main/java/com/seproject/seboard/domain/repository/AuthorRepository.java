package com.seproject.seboard.domain.repository;

import com.seproject.seboard.domain.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
