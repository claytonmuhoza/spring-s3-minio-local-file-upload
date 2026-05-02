package com.claytonmuhoza.upload.fileupload.services;

import com.claytonmuhoza.upload.fileupload.dtos.UploadRequestDto;
import com.claytonmuhoza.upload.fileupload.dtos.UploadResponseDto;
import com.claytonmuhoza.upload.fileupload.exceptions.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileManagementService {
    private final FileStorageService storageService;

    public UploadResponseDto processUpload(UploadRequestDto request) {
        String fileId = UUID.randomUUID().toString();
        String projectId = request.getProjectId();
        boolean isNewProject = false;

        if (projectId == null || projectId.trim().isEmpty()) {
            projectId = UUID.randomUUID().toString();
            isNewProject = true;
        }

        String fileUri;

        try {
            fileUri = storageService.saveFile(
                    request.getFile().getInputStream(),
                    request.getFile().getOriginalFilename(),
                    request.getFile().getContentType(),
                    request.getFile().getSize(),
                    projectId
            );
        } catch (IOException e) {
            throw new StorageException("Unable to read the incoming file stream.", e);
        }

        return new UploadResponseDto("File uploaded successfully.", fileId, projectId, isNewProject, fileUri);
    }

    public Resource downloadFile(String fileUri) {
        InputStream inputStream = storageService.loadFile(fileUri);
        return new InputStreamResource(inputStream);
    }

    public void deleteFile(String fileUri) {
        storageService.deleteFile(fileUri);
    }
}
