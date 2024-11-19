package com.pop.backend.mapper;

import com.pop.backend.entity.Evaluations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pop.backend.entity.Projects;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-14
 */
public interface EvaluationsMapper extends BaseMapper<Evaluations> {

    List<Evaluations> getEvaluationsByProjectId(Integer projectId);

    Evaluations getExistingScore(Integer projectId, Integer userId, Integer roleId);

    Evaluations hasUserAlreadyEvaluated(Integer projectId, Integer userId);

    Evaluations getEvaluationByUserProjectEvaluationRole(@Param("projectId") Integer projectId,
                                      @Param("userId") Integer userId,
                                      @Param("evaluationRoleId") Integer evaluationRoleId);


}
