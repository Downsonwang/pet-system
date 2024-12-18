package com.pet.project.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${file.upload.path}")
    private String uploadPath;

    public String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() +
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(uploadPath + fileName);
        file.transferTo(dest);

        return fileName;
    }
}