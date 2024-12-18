package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("projects")
public class Projects implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "project_id", type = IdType.AUTO)
    private Integer projectId;

    private Integer editionId;

    private String title;

    private String acronym;

    private String description;

    private Integer language;

    private Integer status;

    private String keywords;

    private Boolean isArchived;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Boolean isComplete;

    private Integer year;

    private String overview;



    @TableField(exist = false)
    private List<UserRole> userRole;


    @TableField(exist = false)
    private List<ProjectElements> elements;

    @TableField(exist = false)
    private Editions editions;

    @TableField(exist = false)
    private List<Evaluations> evaluations;

    @TableField(exist = false)
    private List<Reviews> reviews;

}
