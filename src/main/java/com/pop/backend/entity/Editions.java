package com.pop.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;



@Data
@TableName("editions")
public class Editions implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "edition_id", type = IdType.AUTO)
    private Integer editionId;

    private String name;

    private Integer year;

    private Integer semester;

    private Boolean isActive;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;



    @TableField(exist = false)
    private List<UserRole> userRoles;
}