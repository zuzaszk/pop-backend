package com.pop.backend.controller;

import com.pop.backend.service.IProjectElementsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;


/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/projectElements")
public class ProjectElementsController {

    @Autowired
    private IProjectElementsService projectElementsService;

    @PostMapping("/uploadElement")
    @Operation(
            summary = "Upload elements",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> uploadElement(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("elementTypeId") Integer elementTypeId,
            @RequestParam("file") MultipartFile file) {

        projectElementsService.uploadElement(projectId, elementTypeId, file);
        return ResponseEntity.ok("Element uploaded successfully.");
    }


    @GetMapping("/retrieve")
    @Operation(
            summary = "Retrieve element :)",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Resource> retrieveFile(
                @RequestParam("projectElementId") Integer projectElementId) {

        // Delegate file retrieval to FileUtil
        return projectElementsService.retrieveFile(projectElementId);
    }




}
