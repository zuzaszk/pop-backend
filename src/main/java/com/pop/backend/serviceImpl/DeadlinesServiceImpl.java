package com.pop.backend.serviceImpl;

import com.pop.backend.entity.Deadlines;
import com.pop.backend.mapper.DeadlinesMapper;
import com.pop.backend.mapper.EditionsMapper;
import com.pop.backend.service.IDeadlinesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yl
 * @since 2024-11-13
 */
@Service
public class DeadlinesServiceImpl extends ServiceImpl<DeadlinesMapper, Deadlines> implements IDeadlinesService {

    @Autowired
    private EditionsMapper editionsMapper;

    @Autowired
    private DeadlinesMapper deadlinesMapper;

    @Override
    public Deadlines getDeadlineByProjectIdAndElementTypeId(Integer projectId, Integer elementTypeId) {
        Integer editionId = editionsMapper.getEditionIdByProjectId(projectId);
        if (editionId != null) {
            return deadlinesMapper.getDeadlineByEditionIdAndElementTypeId(editionId, elementTypeId);
        }
        return null;
    }
}
