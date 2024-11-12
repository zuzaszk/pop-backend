package com.pop.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;



@Data
@TableName("invitations")
public class Invitations implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "invitation_id", type = IdType.AUTO)
    private Integer invitationId;

    private Integer userId;

    private String invitationLink;

    private Integer state;

    private LocalDateTime createdAt;

    private LocalDateTime expirationDate;

    private Boolean isArchived;

    private Integer userRoleId;

    private String emailAddress;


    @TableField(exist = false)
    private Users user;

    @TableField(exist = false)
    private UserRole userRole;
}