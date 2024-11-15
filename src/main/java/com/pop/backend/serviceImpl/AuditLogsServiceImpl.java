package com.pop.backend.serviceImpl;

import com.pop.backend.entity.AuditLogs;
import com.pop.backend.mapper.AuditLogsMapper;
import com.pop.backend.service.IAuditLogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
@Service
public class AuditLogsServiceImpl extends ServiceImpl<AuditLogsMapper, AuditLogs> implements IAuditLogsService {

    @Override
    public void logAction(String entityType, Integer entityId, String action, Integer changedBy, String changeDetails) {
        AuditLogs log = new AuditLogs();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setChangedBy(changedBy);
        log.setChangeTimestamp(LocalDateTime.now());
        log.setChangeDetails(changeDetails);

        this.save(log);
    }

}
