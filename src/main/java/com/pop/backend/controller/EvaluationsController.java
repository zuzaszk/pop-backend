package com.pop.backend.controller;

import com.pop.backend.entity.Evaluations;
import com.pop.backend.entity.Projects;
import com.pop.backend.service.IEvaluationsService;
import com.pop.backend.service.IProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/evaluations")
public class EvaluationsController {

    @Autowired
    IEvaluationsService evaluationsService;


    @GetMapping("/evaluateList")
    @Operation(
            summary = "List all projects that assigned to supervisor/reviewer to evaluate",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<List<Projects>> getProjectsAssignedToUser(
            @RequestParam("userId") Integer userId,
            @RequestParam("roleId") Integer roleId,
            @RequestParam(value = "editionId", required = false) Integer editionId) {

        List<Projects> projects = evaluationsService.getProjectsAssignedToUser(userId, roleId, editionId);
        return ResponseEntity.ok(projects);
    }


    @PostMapping("/add")
    @Operation(
            summary = "Supervisor/Reviewer add evaluation for a certain project",
            description = "Author: YL"
    )
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<String> addEvaluation(
            @RequestBody Evaluations evaluation) {

        evaluationsService.addEvaluation(evaluation);
        return ResponseEntity.ok("Evaluation added successfully.");
    }


}
