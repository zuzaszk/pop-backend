package com.pop.backend.mapper;

import com.pop.backend.entity.Deadlines;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper
 * </p>
 *
 * @author yl
 * @since 2024-11-13
 */
public interface DeadlinesMapper extends BaseMapper<Deadlines> {

    Deadlines getDeadlineByEditionIdAndElementTypeId(@Param("editionId") Integer editionId,
                                                     @Param("elementTypeId") Integer elementTypeId);


}
