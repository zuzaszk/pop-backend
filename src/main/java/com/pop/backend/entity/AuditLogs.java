package com.pop.backend.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
@TableName("audit_logs")
public class AuditLogs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    private String entityType;

    private Integer entityId;

    private String action;

    @TableField("changed_by")
    private Integer changedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime changeTimestamp;

    private String changeDetails;



    @TableField(exist = false)
    private Users changedByUser; // Reference to the user who made the change
}