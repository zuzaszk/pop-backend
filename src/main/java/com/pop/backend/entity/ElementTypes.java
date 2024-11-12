package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private LocalDateTime createdAt;

    private String modality;
}