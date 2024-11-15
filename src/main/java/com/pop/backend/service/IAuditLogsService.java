package com.pop.backend.service;

import com.pop.backend.entity.AuditLogs;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * </p>
 *
 * @author yl
 * @since 2024-11-12
 */
public interface IAuditLogsService extends IService<AuditLogs> {

    void logAction(String entityType, Integer entityId, String action, Integer changedBy, String changeDetails);

}
