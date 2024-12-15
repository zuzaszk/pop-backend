package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Editions;
import com.pop.backend.mapper.EditionsMapper;
import com.pop.backend.service.IEditionsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-11
 */
@Controller
@RequestMapping("/editions")
public class EditionsController {
    @Autowired
    private EditionsMapper editionsMapper;

    @Autowired IEditionsService editionsService;

    @GetMapping("/listAll")
    @Operation(
            summary = "List all editions",
            description = "Author: YL",
            tags = {"Editions"}
    )
    public ResponseEntity<List<Editions>> getAllEditions() {
        List<Editions> editions = editionsMapper.selectList(null);
        return ResponseEntity.ok(editions);
    }


    @PostMapping("/add")
    @Operation(
            summary = "For Chair adding new edition",
            description = "Author: YL",
            tags = {"Editions"}
    )
    @PreAuthorize("hasRole('ROLE_CHAIR')")
    @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:5173"})
    public ResponseEntity<ApiResponse<String>> addEdition(@RequestBody Editions edition) {
        ApiResponse<String> response = editionsService.addEdition(edition);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }



}
