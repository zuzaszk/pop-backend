package com.pop.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String usosId;

    private String email;

    private String name;

    private String alternativeEmail;

    private Integer state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp lastLoginAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp lastUpdatedAt;

    private String password;

    private String firstName;

    private String lastName;

    private String keywords;

    @TableField(exist = false)
    private List<UserRole> userRole;

    public Users(
        String usosId,
        String email,
        String name,
        Timestamp createdAt,
        Timestamp lastLoginAt,
        String password,
        String firstName,
        String lastName
    ) {
        this.usosId = usosId;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}