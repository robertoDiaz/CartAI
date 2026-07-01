/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.storage;

import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.storage.StoragePort;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
@Component
@Slf4j
public class MinIOStorageAdapter extends BaseStorageAdapter implements StoragePort {

    public MinIOStorageAdapter(
            S3Client s3Client,
            @Value("${minio.bucket-name}") String bucketName,
            @Value("${minio.bucket-name}") String urlBucketName,
            @Value("${minio.endpoint}") String minioEndpoint) {

        super(s3Client, bucketName, urlBucketName, minioEndpoint);
    }

    @PostConstruct
    public void init() {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
        } catch (NoSuchBucketException e) {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.createBucket(createBucketRequest);
        } catch (Exception e) {
            log.error("Error checking or creating bucket in MinIO: " + bucketName, e);
        }

        try {
            String policy = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": \"*\",\n" +
                    "      \"Action\": \"s3:GetObject\",\n" +
                    "      \"Resource\": \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            PutBucketPolicyRequest policyReq = PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(policy)
                    .build();
            s3Client.putBucketPolicy(policyReq);
        } catch (Exception e) {
            log.error("Error setting public policy for bucket: " + bucketName, e);
        }
    }

    @Override
    public StoredFile uploadFile(InputStream inputStream, String fileName, String contentType, long contentLength) {
        return super.upload(inputStream, fileName, contentType, contentLength);
    }

    @Override
    public InputStream downloadFile(String fileName) {
        return super.download(fileName);
    }

    @Override
    public void deleteFile(String fileName) {
        super.delete(fileName);
    }

    @Override
    public void promoteFile(String fileName, String tempBucketName) {
        try {
            CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                    .sourceBucket(tempBucketName)
                    .sourceKey(fileName)
                    .destinationBucket(bucketName)
                    .destinationKey(fileName)
                    .build();

            s3Client.copyObject(copyObjectRequest);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(tempBucketName)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error promoting file from " + tempBucketName + " to " + bucketName + ": " + e.getMessage(), e);
        }
    }
}
