package com.newhorizon.nhmonolithicapplication.service;

import com.newhorizon.nhmonolithicapplication.exception.UnrecognizedFileConnectionString;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.regex.Pattern;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BucketServiceFacadeImpl implements BucketService {
    private final BucketService bucketServiceMinioImpl;
    private final Pattern minioPattern = Pattern.compile("^minio:.*", Pattern.CASE_INSENSITIVE);
    private final Pattern minioPatternReplace = Pattern.compile("^minio:", Pattern.CASE_INSENSITIVE);

    @Override
    public byte[] getByteObject(String keyName) {
        if (minioPattern.matcher(keyName).find()) {
            return bucketServiceMinioImpl.getByteObject(keyName.replaceAll(minioPatternReplace.pattern(), ""));
        } else {
            throw new UnrecognizedFileConnectionString();
        }
    }

    @Override
    public File getFileObject(String keyName) {
        if (minioPattern.matcher(keyName).find()) {
            return bucketServiceMinioImpl.getFileObject(keyName.replaceAll(minioPatternReplace.pattern(), ""));
        } else {
            throw new UnrecognizedFileConnectionString();
        }
    }

    @Override
    public Boolean uploadFile(String keyName, File inputStream) {
        if (minioPattern.matcher(keyName).find()) {
            return bucketServiceMinioImpl.uploadFile(keyName.replaceAll(minioPatternReplace.pattern(), ""),
                    inputStream);
        } else {
            throw new UnrecognizedFileConnectionString();
        }
    }

}
