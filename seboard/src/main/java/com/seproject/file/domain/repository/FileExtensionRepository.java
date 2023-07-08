package com.seproject.file.domain.repository;

import com.seproject.file.domain.model.FileExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {
    void removeByExtensionName(String extensionName);
}
