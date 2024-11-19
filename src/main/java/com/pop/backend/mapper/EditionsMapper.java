package com.pop.backend.mapper;

import com.pop.backend.entity.Editions;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-11
 */
public interface EditionsMapper extends BaseMapper<Editions> {

    Integer getEditionIdByProjectId(@Param("projectId") Integer projectId);

    List<Map<String, Object>> getAverageGradesForLastEditions(@Param("n") int n);

}
