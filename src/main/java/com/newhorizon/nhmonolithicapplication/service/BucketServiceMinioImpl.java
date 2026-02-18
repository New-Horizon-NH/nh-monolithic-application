package com.newhorizon.nhmonolithicapplication.service;

import io.minio.BucketExistsArgs;
import io.minio.DownloadObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketVersioningArgs;
import io.minio.UploadObjectArgs;
import io.minio.messages.VersioningConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketServiceMinioImpl implements BucketService {
    private final MinioClient minioClient;

    @Override
    public byte[] getByteObject(String keyName) {
        try {
            File file = generateTempDownloadFile(keyName);
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(extractBucketFromConnectionString(keyName))
                    .object(removeBucketFromConnectionString(keyName))
                    .filename(file.getAbsolutePath())
                    .build());
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new byte[0];
        }
    }

    @Override
    public File getFileObject(String keyName) {
        try {
            File file = generateTempDownloadFile(keyName);
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(extractBucketFromConnectionString(keyName))
                    .object(removeBucketFromConnectionString(keyName))
                    .filename(file.getAbsolutePath())
                    .build());
            return file;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean uploadFile(String keyName, File inputStream) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(extractBucketFromConnectionString(keyName))
                    .build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(extractBucketFromConnectionString(keyName))
                        .build());
                minioClient.setBucketVersioning(
                        SetBucketVersioningArgs.builder()
                                .bucket(extractBucketFromConnectionString(keyName))
                                .config(new VersioningConfiguration(VersioningConfiguration.Status.ENABLED,
                                        null))
                                .build());
            }
            minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket(extractBucketFromConnectionString(keyName))
                    .object(removeBucketFromConnectionString(keyName))
                    .filename(inputStream.getAbsolutePath())
                    .build())
            ;
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Boolean.FALSE;
        }

    }

    private String extractBucketFromConnectionString(String connectionString) {
        return connectionString.split("/")[0];
    }

    private String removeBucketFromConnectionString(String connectionString) {
        return connectionString.replaceFirst(extractBucketFromConnectionString(connectionString) + "/", "");
    }

    @SneakyThrows
    private File generateTempDownloadFile(String keyName) {
        String filename = keyName.split("/")[keyName.split("/").length - 1];
        File file = Files.createTempFile(filename.split("\\.")[0], filename.split("\\.")[1]).toFile();
        file.deleteOnExit();
        return file;
    }
}
