package com.claytonmuhoza.upload.fileupload.dtos;

public record UploadResponseDto(
        String message,
        String fileId,
        String projectId,
        boolean isNewProject,
        String fileUri
) {}