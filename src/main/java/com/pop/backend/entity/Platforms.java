package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("platforms")
public class Platforms implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "platform_id", type = IdType.AUTO)
    private Integer platformId;

    private String name;

    private String url;
}