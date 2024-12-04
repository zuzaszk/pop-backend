package com.pop.backend.mapper;

import com.pop.backend.entity.Comments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2024-11-18
 */
public interface CommentsMapper extends BaseMapper<Comments> {

    List<Comments> getCommentsByElementId(@Param("elementId") Integer elementId);

}
