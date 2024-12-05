package com.pop.backend.controller;


import com.pop.backend.common.ApiResponse;
import com.pop.backend.service.IEditionsService;
import com.pop.backend.service.IProjectsService;
import com.pop.backend.service.IStatisticService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Statistic Controller
 * </p>
 *
 * @author yl
 * @since 2024-11-17
 */


@RestController
@RequestMapping("/statistic")
public class StatisticController {


    @Autowired
    private IStatisticService statisticService;

    @Autowired
    private IProjectsService projectsService;

    @Autowired
    private IEditionsService editionsService;


    @GetMapping("/getCounts")
    @Operation(
            summary = "Fetch total number of users(optional filtered by roles), projects and reviews(optional filtered by edition)",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCounts(
            @RequestParam(value = "roleId", required = false) Integer roleId,
            @RequestParam(value = "editionId", required = false) Integer editionId) {
        try {
            Map<String, Object> statistics = statisticService.getCounts(roleId, editionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Statistics retrieved successfully.", statistics));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve statistics.", null));
        }
    }





    @GetMapping("/topTechnologies")
    @Operation(
            summary = "Retrieve the most frequently used technologies, optionally filtered by edition.",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTopTechnologies(
            @RequestParam(value = "editionId", required = false) Integer editionId) {
        try {
            List<Map<String, Object>> topTechnologies = projectsService.getTopTechnologies(editionId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Top technologies retrieved successfully.", topTechnologies));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve top technologies.", null));
        }
    }



    @GetMapping("/averageGrades")
    @Operation(
            summary = "Get average grades for the last n editions",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAverageGrades(
            @RequestParam("n") int n) {

        try {
            List<Map<String, Object>> results = editionsService.getAverageGradesForLastEditions(n);
            return ResponseEntity.ok(new ApiResponse<>(true, "Average grades retrieved successfully.", results));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve average grades.", null));
        }
    }


    @GetMapping("/evaluationDetails")
    @Operation(
            summary = "List project evaluation details with final weighted scores",
            description = "Author: YL"
    )
    public ResponseEntity<List<Map<String, Object>>> listProjectEvaluationDetails(
            @RequestParam(required = false) Integer editionId) {
        List<Map<String, Object>> projectDetails = projectsService.listProjectEvaluationDetails(editionId);
        return ResponseEntity.ok(projectDetails);
    }



}
