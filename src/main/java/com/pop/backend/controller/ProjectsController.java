package com.pop.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pop.backend.entity.Projects;
import com.pop.backend.service.IProjectsService;

@RestController
@RequestMapping("/project")
public class ProjectsController {

    @Autowired
    IProjectsService projectsService;

    @GetMapping("/listAll")
    public ResponseEntity<List<Projects>> listAll(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer language) {
        List<Projects> projects = projectsService.listAll(title,year,language);
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/basicInfo")
    public ResponseEntity<Projects> getBasicProjectInfoById(
            @RequestParam Integer projectId) {
        Projects project = projectsService.getBasicProjectInfoById(projectId);
        return ResponseEntity.ok(project);
    }

}