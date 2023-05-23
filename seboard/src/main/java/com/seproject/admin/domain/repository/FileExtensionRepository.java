package com.seproject.admin.domain.repository;

import com.seproject.admin.domain.FileExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileExtensionRepository extends JpaRepository<FileExtension, Long> {
    void removeByExtensionName(String extensionName);
}
