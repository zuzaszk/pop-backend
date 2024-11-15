package com.pop.backend.service;

import com.pop.backend.entity.Deadlines;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-13
 */
public interface IDeadlinesService extends IService<Deadlines> {

    Deadlines getDeadlineByProjectIdAndElementTypeId(Integer projectId, Integer elementTypeId);

}
