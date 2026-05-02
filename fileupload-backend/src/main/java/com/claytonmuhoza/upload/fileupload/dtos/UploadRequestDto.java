package com.claytonmuhoza.upload.fileupload.dtos;

import com.claytonmuhoza.upload.fileupload.validators.ValidFile;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UploadRequestDto {
    @ValidFile(allowedMimeTypes = {"image/tiff", "application/zip", "application/x-zip-compressed","application/pdf",
            "image/jpeg", "image/png", "image/svg+xml"})
    private MultipartFile file;
    private String projectId;
}