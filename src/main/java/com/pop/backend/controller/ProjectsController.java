package com.pop.backend.controller;
import com.pop.backend.entity.ProjectElements;
import com.pop.backend.entity.Projects;
import com.pop.backend.entity.Users;
import com.pop.backend.service.IProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Project Controller
 * </p>
 *
 * @author yl
 * @since 2024-10-22
 */


@RestController
@RequestMapping("/project")
public class ProjectsController {

    @Autowired
    IProjectsService projectsService;

    @GetMapping("/listAll")
    @Operation(
            summary = "List all projects with their title, arc and description",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<List<Projects>> listAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer language) {
        List<Projects> projects = projectsService.listAll(title,year,language);
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/basicInfo")
    @Operation(
            summary = "Get project information, and everything related to this project",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Projects> getBasicProjectInfoById(
            @RequestParam Integer projectId) {
        Projects project = projectsService.getBasicProjectInfoById(projectId);
        return ResponseEntity.ok(project);
    }

    /**
     * Chairs to create project
     * @param title
     * @param editionId
     * @return
     */
    @PostMapping("/create")
    @Operation(
            summary = "Chair initially create project",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> createProject(
                @RequestParam("title") String title,
            @RequestParam("editionId") Integer editionId) {

        projectsService.createProject(title, editionId);
        return ResponseEntity.ok("Project created successfully.");
    }


    @PutMapping ("/saveBasicInfo")
    @Operation(
            summary = "Student fill info about the project",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<Integer> saveBasicInfo(@RequestBody Projects projects) {
        Integer projectId = projectsService.saveBasicInfo(projects);
        return ResponseEntity.ok(projectId);
    }







}
