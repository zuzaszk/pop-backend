package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Comments;
import com.pop.backend.entity.Editions;
import com.pop.backend.service.ICommentsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-18
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    ICommentsService commentsService;

    @PostMapping("/add")
    @Operation(
            summary = "Supervisor and reviewer add comments to element",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<String>> addComment(@RequestBody Comments comments) {
        ApiResponse<String> response = commentsService.addComment(comments);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }


}
