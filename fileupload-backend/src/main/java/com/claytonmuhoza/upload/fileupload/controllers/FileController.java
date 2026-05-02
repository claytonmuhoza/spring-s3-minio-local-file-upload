package com.claytonmuhoza.upload.fileupload.controllers;

import com.claytonmuhoza.upload.fileupload.dtos.UploadRequestDto;
import com.claytonmuhoza.upload.fileupload.dtos.UploadResponseDto;
import com.claytonmuhoza.upload.fileupload.services.FileManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileManagementService fileManagementService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDto> uploadFile(@Valid @ModelAttribute UploadRequestDto request) {
        UploadResponseDto response = fileManagementService.processUpload(request);
        return ResponseEntity.ok(response);
    }

    // Example call: GET /api/v1/files?uri=s3://urban-data/projet-123/carte.pdf
    @GetMapping
    public ResponseEntity<Resource> downloadFile(@RequestParam("uri") String fileUri) {
        Resource resource = fileManagementService.downloadFile(fileUri);
        String filename = fileUri.substring(fileUri.lastIndexOf("/") + 1);
        MediaType mediaType = MediaTypeFactory.getMediaType(filename)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .contentType(mediaType)
                .body(resource);
    }

    // Example call: DELETE /api/v1/files?uri=s3://urban-data/projet-123/carte.pdf
    @DeleteMapping
    public ResponseEntity<Void> deleteFile(@RequestParam("uri") String fileUri) {
        fileManagementService.deleteFile(fileUri);
        return ResponseEntity.noContent().build();
    }
}
