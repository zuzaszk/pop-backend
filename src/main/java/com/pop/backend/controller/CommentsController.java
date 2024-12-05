package com.pop.backend.controller;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Comments;
import com.pop.backend.entity.Editions;
import com.pop.backend.service.ICommentsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

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


    @GetMapping("/getByElementId")
    @Operation(
            summary = "Get comments by elementId",
            description = "Author: YL"
    )
    public ResponseEntity<ApiResponse<List<Comments>>> getCommentsByElementId(
            @RequestParam("elementId") Integer elementId) {
        try {
            List<Comments> comments = commentsService.getCommentsByElementId(elementId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Comments retrieved successfully.", comments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Failed to retrieve comments.", null));
        }
    }



}
