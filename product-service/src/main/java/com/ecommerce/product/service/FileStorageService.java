package com.ecommerce.product.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class FileStorageService {

    private final MinioClient minioClient;
    private final String bucket;
    private final String endpoint;

    public FileStorageService(
            @Value("${spring.minio.endpoint:http://localhost:9001}") String endpoint,
            @Value("${spring.minio.access-key:minioadmin}") String accessKey,
            @Value("${spring.minio.secret-key:minioadmin}") String secretKey,
            @Value("${spring.minio.bucket:product-images}") String bucket) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.minioClient = MinioClient.builder().endpoint(endpoint)
                .credentials(accessKey, secretKey).build();
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FileStorageService.class);
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File is empty");
        if (file.getSize() > MAX_SIZE) throw new IllegalArgumentException("File too large (max 5MB)");
        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) throw new IllegalArgumentException("Filename missing");
        String sanitized = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        // prevent path traversal
        sanitized = sanitized.replace("..", "_");
        String fileName = UUID.randomUUID() + "-" + sanitized;
        String ct = file.getContentType();
        if (ct == null || !(ct.startsWith("image/"))) throw new IllegalArgumentException("Only image files allowed");
        try {
            try {
                boolean exists = minioClient.bucketExists(
                        io.minio.BucketExistsArgs.builder().bucket(bucket).build());
                if (!exists) {
                    minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucket).build());
                }
            } catch (Exception e) {
                log.warn("MinIO bucket check failed: {}", e.getMessage());
                throw new RuntimeException("Storage not available: " + e.getMessage(), e);
            }
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(ct).build());
            return endpoint + "/" + bucket + "/" + fileName;
        } catch (IllegalArgumentException e) { throw e; }
        catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
}
