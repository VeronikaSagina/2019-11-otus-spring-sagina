package ru.otus.spring.sagina.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "minio_file_meta")
public class MinioFileMeta {
    @Id
    @GeneratedValue
    @Column(name = "minio_file_id")
    private UUID id;

    @Column(name = "book_id")
    private UUID bookId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "content_type")
    private String contentType;
}
