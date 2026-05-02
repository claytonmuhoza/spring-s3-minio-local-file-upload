package com.claytonmuhoza.upload.fileupload.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {
    String message() default "The file is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] allowedMimeTypes() default {};
}
