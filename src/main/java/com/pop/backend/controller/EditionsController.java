package com.pop.backend.controller;

import com.pop.backend.entity.Editions;
import com.pop.backend.mapper.EditionsMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/listAll")
    @Operation(
            summary = "List all editions",
            description = "Author: YL"
    )
    public ResponseEntity<List<Editions>> getAllEditions() {
        List<Editions> editions = editionsMapper.selectList(null);
        return ResponseEntity.ok(editions);
    }

}
