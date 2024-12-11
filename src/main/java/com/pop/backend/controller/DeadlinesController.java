package com.pop.backend.controller;

import com.pop.backend.entity.Deadlines;
import com.pop.backend.service.IDeadlinesService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-13
 */
@RestController
@RequestMapping("/deadlines")
public class DeadlinesController {


    @Autowired
    private IDeadlinesService deadlinesService;


    @GetMapping("/getDeadlineByProjectIdAndElementTypeId")
    @Operation(
            summary = "Get hard/soft deadline by projectId and elementId",
            description = "Author: YL"
    )
    public ResponseEntity<Deadlines> getDeadlineByProjectIdAndElementTypeId(
            @RequestParam("projectId") Integer projectId,
            @RequestParam("elementTypeId") Integer elementTypeId) {

        Deadlines deadline = deadlinesService.getDeadlineByProjectIdAndElementTypeId(projectId, elementTypeId);
        if (deadline == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deadline);
    }

}
