package com.newhorizon.nhmonolithicapplication.service;

import java.io.File;

public interface BucketService {
    byte[] getByteObject(String keyName);

    File getFileObject(String keyName);

    Boolean uploadFile(String keyName, File inputStream);
}
