package com.newhorizon.nhmonolithicapplication.exception;

public class UnrecognizedFileConnectionString extends RuntimeException {
    public UnrecognizedFileConnectionString() {
        super("Unrecognized file connection string. It should be like " +
                "s3:file-path or file:file-path or classpath:file-path");
    }
}
