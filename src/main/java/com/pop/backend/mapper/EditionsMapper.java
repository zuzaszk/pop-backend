package com.pop.backend.mapper;

import com.pop.backend.entity.Editions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yl
 * @since 2024-11-11
 */
public interface EditionsMapper extends BaseMapper<Editions> {

    Integer getEditionIdByProjectId(@Param("projectId") Integer projectId);

}
