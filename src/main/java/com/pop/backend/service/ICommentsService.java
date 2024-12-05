package com.pop.backend.service;

import com.pop.backend.common.ApiResponse;
import com.pop.backend.entity.Comments;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pop.backend.entity.Editions;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-18
 */
public interface ICommentsService extends IService<Comments> {

    ApiResponse<String> addComment(Comments comments);

    List<Comments> getCommentsByElementId(Integer elementId);

}
