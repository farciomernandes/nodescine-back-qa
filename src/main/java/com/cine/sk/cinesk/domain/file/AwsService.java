package com.cine.sk.cinesk.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.*;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class AwsService extends FileManagerService {

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Value("${AWS_S3_REGION}")
    private String region;

    @Value("${AWS_S3_ACCESS_KEY:}")
    private String accessKey;

    @Value("${AWS_S3_SECRET_KEY:}")
    private String secretKey;

    @Value("${AWS_S3_SESSION_TOKEN:}")
    private String sessionToken;

    private S3Client s3Client;

    private StorageType storageType;

    @PostConstruct
    public void init() {
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(region));

        AwsCredentialsProvider provider;
        if (StringUtils.hasText(accessKey) && StringUtils.hasText(secretKey)) {
            if (StringUtils.hasText(sessionToken)) {
                provider = StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(accessKey, secretKey, sessionToken));
            } else {
                provider = StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey));
            }
        } else {
            provider = DefaultCredentialsProvider.create();
        }

        this.s3Client = builder.credentialsProvider(provider).build();
        this.storageType = StorageType.AWS_S3;
    }

    @Override
    public Page<File> list(Pageable pageable, String path) {
        try {
            List<File> files = new ArrayList<>();
            String prefix = buildPrefix(path);
            ListObjectsV2Request.Builder listReqBuilder = ListObjectsV2Request.builder()
                    .bucket(bucketName);
            if (StringUtils.hasText(prefix)) {
                listReqBuilder = listReqBuilder.prefix(prefix);
            }
            ListObjectsV2Request listReq = listReqBuilder.build();
            ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
            for (S3Object obj : listRes.contents()) {
                if (obj.size() == 0) continue; // skip folder placeholders

                String key = obj.key();
                String name = key.contains("/") ? key.substring(key.lastIndexOf("/") + 1) : key;
                files.add(new File(
                    key,
                    name,
                    storageType,
                    obj.eTag(),
                    s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(key)).toString(),
                    obj.lastModified(),
                    null // createdBy unknown
                ));
            }
            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;
            List<File> pageContent;
            if (startItem >= files.size()) {
                pageContent = java.util.Collections.emptyList();
            } else {
                int toIndex = Math.min(startItem + pageSize, files.size());
                pageContent = files.subList(startItem, toIndex);
            }
            return new PageImpl<>(pageContent, pageable, files.size());
        } catch (SdkClientException e) {
            throw new RuntimeException("AWS credentials not configured or invalid. Configure AWS_ACCESS_KEY_ID/AWS_SECRET_ACCESS_KEY, an AWS profile, or an instance role.", e);
        }
    }

    @Override
    public Optional<File> get(String fileId, String path) {
        try {
            String key = resolveKey(fileId, path);
            HeadObjectRequest headReq = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            HeadObjectResponse headRes = s3Client.headObject(headReq);
        String name = key.contains("/") ? key.substring(key.lastIndexOf("/") + 1) : key;
        return Optional.of(new File(
            key, // id (S3 key)
            name,   // name (basename)
            storageType,
            headRes.eTag(),
            s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(key)).toString(),
            headRes.lastModified(),
            null // createdBy unknown
        ));
        } catch (NoSuchKeyException e) {
            return Optional.empty();
        }
    }

    @Override
    public File upload(MultipartFile file, String path, String id, String name) {
        try {
            List<String> allowedMimeTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");
            String contentType = file.getContentType();
            if (!allowedMimeTypes.contains(contentType)) {
                throw new IllegalArgumentException("Only image files (JPEG, PNG, GIF) are allowed.");
            }

            String extension = getFileExtension(contentType);
            String prefix = path.endsWith("/") ? path : path + "/";
            String key = prefix + id + "." + extension;

            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            PutObjectResponse putRes = s3Client.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return new File(
                    key,
                    name,
                    storageType,
                    putRes.eTag(),
                    s3Client.utilities().getUrl(b -> b.bucket(bucketName).key(key)).toString(),
                    Instant.now(),
                    null
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }

    private String getFileExtension(String contentType) {
        switch (contentType) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            default:
                throw new IllegalArgumentException("Unsupported image type: " + contentType);
        }
    }

    @Override
    public void delete(String fileId, String path) {
        try {
            String key = resolveKey(fileId, path);
            DeleteObjectRequest delReq = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            s3Client.deleteObject(delReq);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    private String buildPrefix(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        String raw = path;
        if (raw.contains("..") || raw.startsWith("/") || raw.startsWith("\\")) {
            throw new RuntimeException("Invalid path");
        }
        String trimmed = path.trim();
        while (trimmed.startsWith("/")) trimmed = trimmed.substring(1);
        while (trimmed.endsWith("/")) trimmed = trimmed.substring(0, trimmed.length() - 1);
        return StringUtils.hasText(trimmed) ? trimmed + "/" : "";
    }

    private String resolveKey(String fileId, String path) {
        String prefix = buildPrefix(path);
        if (!StringUtils.hasText(prefix)) {
            return fileId;
        }
        if (StringUtils.hasText(fileId) && (fileId.startsWith(prefix) || fileId.startsWith(prefix.substring(0, prefix.length() - 1)))) {
            return fileId; // already includes prefix
        }
        return prefix + fileId;
    }
}
