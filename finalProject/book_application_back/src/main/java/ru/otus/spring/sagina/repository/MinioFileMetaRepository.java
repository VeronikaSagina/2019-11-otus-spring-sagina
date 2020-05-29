package ru.otus.spring.sagina.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.sagina.entity.MinioFileMeta;

import java.util.List;
import java.util.UUID;

public interface MinioFileMetaRepository extends JpaRepository<MinioFileMeta, UUID> {
    List<MinioFileMeta> findAllByBookId(UUID bookId);
}
