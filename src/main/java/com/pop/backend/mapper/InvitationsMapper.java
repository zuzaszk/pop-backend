package com.pop.backend.mapper;

import com.pop.backend.entity.Invitations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InvitationsMapper extends BaseMapper<Invitations> {

    @Select("SELECT * FROM invitations ORDER BY created_at DESC")
    List<Invitations> getAllInvitations();

}
