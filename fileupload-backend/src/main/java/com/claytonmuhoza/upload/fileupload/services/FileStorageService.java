package com.claytonmuhoza.upload.fileupload.services;

import java.io.InputStream;

public interface FileStorageService {

    /**
     * Saves a file to storage under a specific project.
     *
     * @param inputStream The data stream (decoupled from the Web/HTTP)
     * @param originalFilename The original filename (to extract the extension)
     * @param contentType The MIME type (required by Minio/S3)
     * @param size The file size (required by Minio/S3)
     * @param projectId The project ID
     * @return The absolute URI of the stored file (e.g., s3://bucket/project/uuid.tif)
     */
    String saveFile(InputStream inputStream, String originalFilename, String contentType, long size, String projectId);

    /**
     * Loads a file from storage.
     * @param fileUri The full URI of the file (generated during save)
     * @return The data stream of the file
     */
    InputStream loadFile(String fileUri);

    /**
     * Deletes a file from storage.
     * @param fileUri The full URI of the file
     */
    void deleteFile(String fileUri);
}
