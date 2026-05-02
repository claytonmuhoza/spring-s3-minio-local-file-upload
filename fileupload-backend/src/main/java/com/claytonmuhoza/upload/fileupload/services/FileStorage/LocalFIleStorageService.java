package com.claytonmuhoza.upload.fileupload.services.FileStorage;

import com.claytonmuhoza.upload.fileupload.exceptions.StorageException;
import com.claytonmuhoza.upload.fileupload.exceptions.StorageFileNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Profile("local")
public class LocalFIleStorageService extends AbstractFileStorageService{
    @Value("${storage.local.dir:urban-uploads}")
    private String localDirProperty;
    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(localDirProperty);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Unable to initialize the local folder:" + localDirProperty, e);
        }
    }

    @Override
    protected String storeInternal(InputStream inputStream, String objectPath, String contentType, long size) {
        try {
            Path destinationFile = rootLocation.resolve(objectPath).normalize().toAbsolutePath();
            Files.createDirectories(destinationFile.getParent());

            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return "file://" + destinationFile.toString();

        } catch (IOException e) {
            throw new StorageException("Unable to initialize the local folder:", e);
        }
    }

    @Override
    public InputStream loadFile(String fileUri) {
        try {
            String pathWithoutPrefix = fileUri.replace("file://", "");
            return Files.newInputStream(Paths.get(pathWithoutPrefix));
        } catch (IOException e) {
            throw new StorageFileNotFoundException("Unable to read local file:" + fileUri, e);
        }
    }

    @Override
    public void deleteFile(String fileUri) {
        try {
            String pathWithoutPrefix = fileUri.replace("file://", "");
            Files.deleteIfExists(Paths.get(pathWithoutPrefix));
        } catch (IOException e) {
            throw new StorageException("Unable to delete the local file: " + fileUri, e);
        }
    }
}
