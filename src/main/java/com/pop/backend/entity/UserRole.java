package com.pop.backend.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName("user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;

    @TableField("user_id")
    private Integer userId;

    @TableField("role_id")
    private Integer roleId;

    @TableField("project_id")
    private Integer projectId;

    @TableField("edition_id")
    private Integer editionId;

    @TableField(exist = false)
    private Users users;

    @TableField(exist = false)
    private Roles roles;

    @TableField(exist = false)
    private Projects project;

    @TableField(exist = false)
    private Editions edition;
}