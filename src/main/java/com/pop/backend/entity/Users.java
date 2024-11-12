package com.pop.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


@Data
@TableName("users")
public class Users {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String usosId;

    private String email;

    private String name;

    private String alternativeEmail;

    private Integer state;

    private Timestamp createdAt;

    private Timestamp lastLoginAt;

    private Timestamp lastUpdatedAt;

    private String password;

    private String firstName;

    private String lastName;

    private String keywords;

    @TableField(exist = false)
    private List<UserRole> userRole;
}