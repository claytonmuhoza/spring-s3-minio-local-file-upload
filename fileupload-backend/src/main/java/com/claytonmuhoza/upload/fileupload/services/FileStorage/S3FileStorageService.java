package com.claytonmuhoza.upload.fileupload.services.FileStorage;

import com.claytonmuhoza.upload.fileupload.exceptions.StorageException;
import com.claytonmuhoza.upload.fileupload.exceptions.StorageFileNotFoundException;
import io.minio.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Profile("S3")
public class S3FileStorageService extends AbstractFileStorageService{
    private final MinioClient minioClient;
    @Value("${minio.bucket-name}")
    private String bucketName;

    public S3FileStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    protected String storeInternal(InputStream inputStream, String objectPath, String contentType, long size) {

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .stream(inputStream, size, (long)-1)
                            .contentType(contentType)
                            .build());
            return "s3://" + bucketName + "/" + objectPath;

        } catch (Exception e) {

            throw new StorageException("Échec de l'envoi du fichier vers S3 : " + objectPath, e);
        }
    }

    @Override
    public InputStream loadFile(String fileUri) {
        try {
            String objectPath = extractObjectPath(fileUri);
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .build());
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Impossible de lire le fichier depuis S3 : " + fileUri, e);
        }
    }

    @Override
    public void deleteFile(String fileUri) {
        try {
            String objectPath = extractObjectPath(fileUri);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectPath)
                            .build());
        } catch (Exception e) {
            throw new StorageException("Impossible de supprimer le fichier depuis S3 : " + fileUri, e);
        }
    }

    /**
     * Transforms "s3://urban-data/monProjet/fichier.tif"
     * into "monProjet/fichier.tif" (the actual name of the object in S3).
     */
    private String extractObjectPath(String fileUri) {
        String prefix = "s3://" + bucketName + "/";
        if (fileUri.startsWith(prefix)) {
            return fileUri.substring(prefix.length());
        }
        throw new IllegalArgumentException("L'URI fournie (" + fileUri + ") ne correspond pas au bucket configuré.");
    }
}
