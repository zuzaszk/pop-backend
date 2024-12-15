package com.pop.backend.controller;

import com.pop.backend.service.IProjectElementsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
            description = "Author: YL",
            tags = {"Project Elements"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<String> uploadElement(
            @RequestParam Integer projectId,
            @RequestParam Integer elementTypeId,
            @RequestParam MultipartFile file) {

        projectElementsService.uploadElement(projectId, elementTypeId, file);
        return ResponseEntity.ok("Element uploaded successfully.");
    }


    @GetMapping("/retrieve")
    @Operation(
            summary = "Retrieve element :)",
            description = "Author: YL",
            tags = {"Project Elements"}
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Resource> retrieveFile(
                @RequestParam Integer projectElementId) {

        // Delegate file retrieval to FileUtil
        return projectElementsService.retrieveFile(projectElementId);
    }




}
