package com.pop.backend.mapper;

import com.pop.backend.entity.ElementTypes;
import com.pop.backend.entity.ProjectElements;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pop.backend.entity.Reviews;

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

}
