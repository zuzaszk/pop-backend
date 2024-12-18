package com.pop.backend.controller;
import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Projects;
import com.pop.backend.security.AccessControlService;
import com.pop.backend.service.IProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @Autowired
    AccessControlService accessControlService;

    @GetMapping("/listAll")
    @Operation(
            summary = "List all projects with their title, arc and description",
            description = "Author: YL",
            tags = {"Projects"}
    )
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
            description = "Author: YL",
            tags = {"Projects"}
    )
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
            description = "Author: YL",
            tags = {"Projects"}
    )
    @CrossOrigin(origins = {"https://269593.kieg.science/api", "https://269593.kieg.science"})
    @PreAuthorize("hasRole('ROLE_CHAIR')")
    public ResponseEntity<String> createProject(
                @RequestParam String title,
            @RequestParam Integer editionId) {

        projectsService.createProject(title, editionId);
        return ResponseEntity.ok("Project created successfully.");
    }


    @PutMapping ("/saveBasicInfo")
    @Operation(
            summary = "Student fill info about the project",
            description = "Author: YL",
            tags = {"Projects"}
    )
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    @CrossOrigin(origins = {"https://269593.kieg.science/api", "https://269593.kieg.science"})
    public ResponseEntity<Integer> saveBasicInfo(@RequestBody Projects projects) {
        if (!accessControlService.isTeamMember(projects.getProjectId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        Integer projectId = projectsService.saveBasicInfo(projects);
        return ResponseEntity.ok(projectId);
    }



    @GetMapping("/getByUserRole")
    @Operation(
            summary = "Get projects connected to a user in a certain role, optional filtered by edition and language",
            description = "Fetch projects connected to a specific user and role, with optional filters for edition and language.",
            tags = {"Projects"}
    )
    @CrossOrigin(origins = {"https://269593.kieg.science/api", "https://269593.kieg.science"})
    public ResponseEntity<ApiResponse<List<Projects>>> getProjectsByUserRole(
            @RequestParam Integer userId,
            @RequestParam Integer roleId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer language) {
        try {
            List<Projects> projects = projectsService.getProjectsByUserRole(userId, roleId, title, year, language);
            return ResponseEntity.ok(new ApiResponse<>(true, "Projects retrieved successfully.", projects));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while fetching projects.", null));
        }
    }










}
