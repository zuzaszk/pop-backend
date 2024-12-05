package com.pop.backend.serviceImpl;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Comments;
import com.pop.backend.mapper.CommentsMapper;
import com.pop.backend.mapper.ProjectElementsMapper;
import com.pop.backend.service.ICommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-18
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments> implements ICommentsService {


    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    ProjectElementsMapper projectElementsMapper;


    @Override
    public ApiResponse<String> addComment(Comments comments) {
        // Validate required fields
        if (comments.getUserId() == null || comments.getElementId() == null || comments.getComment() == null) {
            return new ApiResponse<>(false, "Missing required fields: userId, elementId, or comment.", null);
        }


        // Set creation timestamp
        comments.setCreatedAt(LocalDateTime.now());
        comments.setIsPublic(false);

        // Insert the edition into the database
        commentsMapper.insert(comments);

        return new ApiResponse<>(true, "Comments added successfully.", null);
    }


    @Override
    public List<Comments> getCommentsByElementId(Integer elementId) {
        return commentsMapper.getCommentsByElementId(elementId);
    }
}
