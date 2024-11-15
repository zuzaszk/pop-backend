package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;



@Data
@TableName("element_types")
public class ElementTypes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "element_type_id", type = IdType.AUTO)
    private Integer elementTypeId;

    private String name;

    private String description;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String modality;
}