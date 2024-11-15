package com.pop.backend.util;


import ch.qos.logback.core.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommonUtil {

    private static String storageDirectory;
    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);


    @Value("${storage.directory}")
    public void setStorageDirectory(String directory) {
        storageDirectory = directory;
    }

    public static String saveFileToStorage(MultipartFile file, String projectArc, String elementTypesName) {
        // Create a unique file name using a timestamp
        String originalFileName = file.getOriginalFilename();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueFileName = timestamp + "_" + originalFileName;

        // Ensure the storage directory exists
        Path storagePath = Paths.get(storageDirectory, projectArc, elementTypesName);
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create storage directory", e);
            }
        }

        Path dbFilePath = Paths.get(projectArc, elementTypesName, uniqueFileName);
        // Full path where the file will be saved
        Path filePath = storagePath.resolve(uniqueFileName);

        try {
            // Save the file to the specified path
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + uniqueFileName, e);
        }

        // Return the path as a string to store in the database
        return dbFilePath.toString();
    }




    public static ResponseEntity<Resource> retrieveFile(String filePath) {
        try {
            // Construct the full path
            Path path = Paths.get(storageDirectory, filePath);
            log.info("Attempting to retrieve file from path: " + path.toAbsolutePath());
            Resource resource = new UrlResource(path.toUri());

            // Check if the file exists and is readable
            if (resource.exists() && resource.isReadable()) {
                String mimeType = Files.probeContentType(path);

                // Handle images (display inline)
                if (mimeType != null && mimeType.startsWith("image")) {
                    log.info("File is an image. Serving inline.");
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(mimeType))
                            .body(resource);
                }

                // Handle PDFs (preview inline)
                else if ("application/pdf".equals(mimeType)) {
                    log.info("File is a PDF. Serving inline.");
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                }

                // Handle other files (download as attachment)
                else {
                    log.info("File is of unknown type. Serving as download.");
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);
                }
            } else {
                log.error("File not found or not readable at path: " + path.toAbsolutePath());
                throw new RuntimeException("File not found or not readable: " + filePath);
            }
        } catch (IOException e) {
            log.error("IOException while retrieving file from path: " + filePath, e);
            throw new RuntimeException("Error retrieving file: " + e.getMessage());
        }
    }




}
