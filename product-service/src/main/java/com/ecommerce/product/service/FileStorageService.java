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
            @Value("${minio.endpoint:http://localhost:9001}") String endpoint,
            @Value("${minio.access-key:minioadmin}") String accessKey,
            @Value("${minio.secret-key:minioadmin}") String secretKey,
            @Value("${minio.bucket:product-images}") String bucket) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.minioClient = MinioClient.builder().endpoint(endpoint)
                .credentials(accessKey, secretKey).build();
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            try {
                boolean exists = minioClient.bucketExists(
                        io.minio.BucketExistsArgs.builder().bucket(bucket).build());
                if (!exists) {
                    minioClient.makeBucket(io.minio.MakeBucketArgs.builder().bucket(bucket).build());
                }
            } catch (Exception ignored) {}
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build());
            return endpoint + "/" + bucket + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }
}
