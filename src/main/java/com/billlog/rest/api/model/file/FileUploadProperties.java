package com.billlog.rest.api.model.file;

import org.springframework.boot.context.properties.ConfigurationProperties;


/*
    @CongifurationProperties Annotation 에 prefix = "file" 로 선언 된 부분은 application.properties 에 선언한
    file.upload-dir=./uploads
    file 필드에 접근한다는 말이다.
 */

//@Component
@ConfigurationProperties(prefix="file")
public class FileUploadProperties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
