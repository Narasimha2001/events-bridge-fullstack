package com.eb.eventsbridge.shared.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Primary
@Slf4j
public class LocalStorageServiceImpl implements StorageService {

    private final String uploadDir = "uploads/";

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {

        	Path copyLocation = Paths.get(uploadDir + folder + File.separator + 
                                          UUID.randomUUID() + "-" + file.getOriginalFilename());
            Files.createDirectories(copyLocation.getParent());

            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            return copyLocation.toString();
        } catch (IOException e) {
            log.error("Could not store file", e);
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            Files.deleteIfExists(Paths.get(fileUrl));
        } catch (IOException e) {
            log.error("Could not delete file", e);
        }
    }
}
