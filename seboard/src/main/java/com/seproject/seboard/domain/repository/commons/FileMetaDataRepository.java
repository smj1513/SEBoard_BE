package com.seproject.seboard.domain.repository.commons;

import com.seproject.seboard.domain.model.common.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetaDataRepository extends JpaRepository<FileMetaData, Long> {
}
