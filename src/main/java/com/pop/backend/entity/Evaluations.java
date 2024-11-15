package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@TableName("evaluations")
public class Evaluations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "evaluation_id", type = IdType.AUTO)
    private Integer evaluationId;

    private Integer projectId;

    private Integer userId;

    private Integer roleId;

    private BigDecimal score;

    private String comment;

    private Integer state;

    private Boolean isPublic;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;



    @TableField(exist = false)
    private Projects project;

    @TableField(exist = false)
    private Users user;


}
