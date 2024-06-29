/*
 * Copyright (c) Xipu Tech. 2023.
 */

package com.fulinx.spring.service.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileUploadPathService {

    @Value("${file.upload-dir.general}")
    private String uploadDir;

    @Value("${file.upload-dir.windows}")
    private String uploadDirWindows;

    @Value("${file.upload-dir.linux}")
    private String uploadDirLinux;

    @Value("${file.upload-dir.urlPrefix}")
    private String urlPrefix;

    public String getUploadDir() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return uploadDirWindows;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return uploadDirLinux;
        }
        return uploadDir + "images/"; // Fallback to the common directory
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }
}
