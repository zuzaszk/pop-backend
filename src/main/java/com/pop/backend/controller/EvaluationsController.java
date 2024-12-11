package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Evaluations;
import com.pop.backend.entity.Projects;
import com.pop.backend.service.IEvaluationsService;
import com.pop.backend.service.IProjectsService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/evaluations")
public class EvaluationsController {

    private static final Logger logger = LoggerFactory.getLogger(EvaluationsController.class);

    @Autowired
    IEvaluationsService evaluationsService;


    @GetMapping("/assignedEvaluateList")
    @Operation(
            summary = "List all projects that assigned to supervisor/reviewer to evaluate",
            description = "Author: YL"
    )
    public ResponseEntity<List<Projects>> getProjectsAssignedToUser(
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "editionId", required = false) Integer editionId) {

        List<Projects> projects = evaluationsService.getProjectsAssignedToUser(userId, editionId);
        return ResponseEntity.ok(projects);
    }


    @PostMapping("/add")
    @Operation(
            summary = "Supervisor/Reviewer add evaluation for a certain project",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<String>> addEvaluation(@RequestBody Evaluations evaluation) {
        logger.debug("Received evaluation request: {}", evaluation);

        try {
            ApiResponse<String> response = evaluationsService.addEvaluation(evaluation);
            logger.debug("Response: {}", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error while adding evaluation: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Unexpected error occurred.", null));
        }
    }



    @PutMapping("/update")
    @Operation(
            summary = "Update an existing evaluation for a certain project",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<String>> updateEvaluation(@RequestBody Evaluations evaluation) {
        logger.debug("Received evaluation update request: {}", evaluation);

        try {
            ApiResponse<String> response = evaluationsService.updateEvaluation(evaluation);
            logger.debug("Update Response: {}", response);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error while updating evaluation: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("Unexpected error while updating evaluation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Unexpected error occurred while updating evaluation.", null));
        }
    }



    @GetMapping("/getEvaluation")
    @Operation(
            summary = "Retrieve evaluation based on projectId, userId, and evaluationRoleId",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<Evaluations>> getEvaluationByUserRoleProject(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("userId") Integer userId) {

        try {
            Evaluations evaluation = evaluationsService.getEvaluationByUser(projectId, userId);
            if (evaluation != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Evaluation retrieved successfully.", evaluation));
            } else {
                return ResponseEntity.ok(new ApiResponse<>(false, "No score found for the given parameters.", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An error occurred while retrieving the score.", null));
        }
    }

}
