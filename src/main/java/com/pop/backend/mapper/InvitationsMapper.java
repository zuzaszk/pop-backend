package com.pop.backend.mapper;

import com.pop.backend.entity.Invitations;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InvitationsMapper extends BaseMapper<Invitations> {

    @Select({
            "<script>",
            "SELECT * FROM invitations",
            "<where>",
            "<if test='isForCurrentUser != null and isForCurrentUser == true'>",
            "AND user_id = #{userId}",
            "</if>",
            "</where>",
            "ORDER BY created_at DESC",
            "</script>"
    })
    List<Invitations> getAllInvitations(@Param("userId") Integer userId, @Param("isForCurrentUser") Boolean isForCurrentUser);

}
