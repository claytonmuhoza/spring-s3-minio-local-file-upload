package com.claytonmuhoza.upload.fileupload.services.FileStorage;

import com.claytonmuhoza.upload.fileupload.services.FileStorageService;

import java.io.InputStream;
import java.util.UUID;

public abstract class AbstractFileStorageService implements FileStorageService {
    @Override
    public String saveFile(InputStream inputStream, String originalFilename, String contentType, long size, String projectId) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        String objectPath = projectId + "/" + uniqueFilename;
        return storeInternal(inputStream, objectPath, contentType, size);
    }
    protected abstract String storeInternal(InputStream inputStream, String objectPath, String contentType, long size);
}
