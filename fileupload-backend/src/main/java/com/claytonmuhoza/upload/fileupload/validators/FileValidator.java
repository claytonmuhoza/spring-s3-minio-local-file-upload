package com.claytonmuhoza.upload.fileupload.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile>{
    private List<String> allowedMimeTypes;
    private final Tika tika = new Tika();
    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.allowedMimeTypes = Arrays.asList(constraintAnnotation.allowedMimeTypes());
    }
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            buildError(context, "The file is required and cannot be empty.");
            return false;
        }
        if (!allowedMimeTypes.isEmpty()) {
            try (InputStream is = file.getInputStream()) {
                String realMimeType = tika.detect(is);
                if (!allowedMimeTypes.contains(realMimeType)) {
                    buildError(context, "Invalid file. Type detected: " + realMimeType +
                            ". Accepted types : " + allowedMimeTypes);
                    return false;
                }
            } catch (IOException e) {
                buildError(context, "Unable to read the file for verification.");
                return false;
            }
        }

        return true;
    }
    private void buildError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
