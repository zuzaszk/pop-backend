package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@TableName("project_elements")
public class ProjectElements implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "element_id", type = IdType.AUTO)
    private Integer elementId;

    private Integer elementTypeId;

    private Integer projectId;

    private String vFilePath;

    private String description;

    private LocalDateTime createdAt;

    private String vText;

    private Double vNumber;




    @TableField(exist = false)
    private ElementTypes elementType;

    @TableField(exist = false)
    private Projects project;
}