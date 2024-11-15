package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;



@Data
@TableName("deadlines")
public class Deadlines implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "deadline_id", type = IdType.AUTO)
    private Integer deadlineId;

    private Integer elementTypeId;

    private Integer editionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime softDeadline;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime hardDeadline;

    private Boolean isActive;

    private LocalDateTime createdAt;



    @TableField(exist = false)
    private ElementTypes elementType;

    @TableField(exist = false)
    private Editions edition;
}