package ru.otus.spring.sagina.services;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.sagina.dto.response.FileResultDto;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.entity.MinioFileMeta;
import ru.otus.spring.sagina.exceptions.MinioException;
import ru.otus.spring.sagina.exceptions.NotFoundException;
import ru.otus.spring.sagina.repository.MinioFileMetaRepository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class MinioService {
    private final static Logger LOGGER = LoggerFactory.getLogger(MinioService.class);
    private final MinioClient minioClient;
    private final MinioFileMetaRepository minioFileMetaRepository;

    public MinioService(@Value("${minio.path:http://172.17.0.2:9000}") String path,
                        @Value("${minio.key.access}") String accessKey,
                        @Value("${minio.key.secret}") String secretKey,
                        MinioFileMetaRepository minioFileMetaRepository) {
        this.minioFileMetaRepository = minioFileMetaRepository;
        try {
            minioClient = new MinioClient(path, accessKey, secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            LOGGER.error("Не удалось создать MinioClient", e);
            throw new MinioException("Не удалось создать MinioClient", e);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void uploadBook(Book book, MultipartFile multipartFile) {
        MinioFileMeta fileMeta = new MinioFileMeta();
        fileMeta.setBucketName(String.valueOf(book.getAuthor().getId()));
        fileMeta.setFileName(multipartFile.getOriginalFilename());
        fileMeta.setBookId(book.getId());
        fileMeta.setContentType(multipartFile.getContentType());
        MinioFileMeta savedMeta = minioFileMetaRepository.save(fileMeta);
        try {
            if (!minioClient.bucketExists(fileMeta.getBucketName())) {
                minioClient.makeBucket(fileMeta.getBucketName());
            }
            putToMinio(new ByteArrayInputStream(multipartFile.getBytes()), savedMeta);
        } catch (Exception e) {
            LOGGER.error("ошибка при сохраниении файла в minio: ", e);
            throw new MinioException("ошибка при сохраниении файла в minio: " + e);
        }
    }

    private void putToMinio(ByteArrayInputStream inputStream, MinioFileMeta meta) throws Exception {
        PutObjectOptions putObjectOptions = new PutObjectOptions(inputStream.available(), -1);
        putObjectOptions.setContentType(Objects.requireNonNull(meta.getContentType()));
        minioClient.putObject(
                meta.getBucketName(),
                String.valueOf(meta.getId()),
                inputStream, putObjectOptions);
    }

    public FileResultDto getFile(UUID id) {
        MinioFileMeta fileMeta = minioFileMetaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("не найден файл с id: " + id));
        try {
            InputStream inputStream = minioClient.getObject(fileMeta.getBucketName(), String.valueOf(fileMeta.getId()));
            return new FileResultDto(fileMeta.getFileName(), fileMeta.getContentType(), inputStream);
        } catch (Exception e) {
            LOGGER.error("ошибка получения файла из minio: ", e);
            throw new MinioException("ошибка получения файла из minio: " + e);
        }
    }

    public void delete(UUID bookId) {
        List<MinioFileMeta> allByBookId = minioFileMetaRepository.findAllByBookId(bookId);
        if (allByBookId.isEmpty()) {
            return;
        }
        try {
            for (MinioFileMeta fileMeta : allByBookId) {
                minioClient.removeObject(fileMeta.getBucketName(), String.valueOf(fileMeta.getId()));
            }
        } catch (Exception e) {
            throw new MinioException("ошибка удаления файла из minio: ", e);
        }
    }
}
