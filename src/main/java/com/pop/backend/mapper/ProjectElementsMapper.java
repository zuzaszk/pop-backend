package com.pop.backend.mapper;

import com.pop.backend.entity.ProjectElements;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
public interface ProjectElementsMapper extends BaseMapper<ProjectElements> {

    List<ProjectElements> getElementsByProjectId(Integer projectId);

    ProjectElements getByProjectIdAndElementTypeId(@Param("projectId") Integer projectId,
                                                   @Param("elementTypeId") Integer elementTypeId);


}
