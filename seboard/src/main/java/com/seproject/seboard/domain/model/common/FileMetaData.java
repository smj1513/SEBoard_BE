package com.seproject.seboard.domain.model.common;


import com.seproject.seboard.domain.model.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

import javax.persistence.*;

@Entity
@Table(name = "file_meta_data")
@Getter
@NoArgsConstructor
public class FileMetaData {
    @Id @GeneratedValue
    @Column(name = "file_meta_data_id")
    private Long fileMetaDataId;
    private String originalFileName;
    private String storedFileName;
    private String filePath;
    private String urlPath;
    private Long fileSize;
    private BaseTime baseTime;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    public FileMetaData(String originalFileName, String storedFileName, String filePath, String urlPath, Long fileSize, BaseTime baseTime) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.urlPath = urlPath;
        this.fileSize = fileSize;
        this.baseTime = baseTime;
    }

}
