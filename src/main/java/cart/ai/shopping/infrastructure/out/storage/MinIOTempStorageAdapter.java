/**
 * Copyright (C) 2026 Roberto Díaz. All rights reserved.
 * Licensed under the GNU General Public License v3.0. See LICENSE for details.
 */

package cart.ai.shopping.infrastructure.out.storage;

import cart.ai.shopping.domain.model.storage.StoredFile;
import cart.ai.shopping.domain.ports.storage.TempStoragePort;
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
public class MinIOTempStorageAdapter extends BaseStorageAdapter implements TempStoragePort {

    public MinIOTempStorageAdapter(
            S3Client s3Client,
            @Value("${minio.temp-bucket-name}") String bucketName,
            @Value("${minio.temp-bucket-name}") String urlBucketName,
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
            log.error("Error checking or creating temp bucket in MinIO: " + bucketName, e);
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
            log.error("Error setting public policy for temp bucket: " + bucketName, e);
        }

        try {
            LifecycleRule filterRule = LifecycleRule.builder()
                    .id("Delete temporary files")
                    .filter(LifecycleRuleFilter.builder().prefix("").build())
                    .expiration(LifecycleExpiration.builder().days(1).build())
                    .status(ExpirationStatus.ENABLED)
                    .build();

            PutBucketLifecycleConfigurationRequest lifecycleRequest = PutBucketLifecycleConfigurationRequest.builder()
                    .bucket(bucketName)
                    .lifecycleConfiguration(BucketLifecycleConfiguration.builder()
                            .rules(filterRule)
                            .build())
                    .build();
            s3Client.putBucketLifecycleConfiguration(lifecycleRequest);
        } catch (Exception e) {
            log.error("Error setting temp bucket lifecycle configuration: " + bucketName, e);
        }
    }

    @Override
    public StoredFile uploadFile(InputStream inputStream, String fileName, String contentType, long contentLength) {
        return super.upload(inputStream, fileName, contentType, contentLength);
    }

    @Override
    public void deleteFile(String fileName) {
        super.delete(fileName);
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

}
