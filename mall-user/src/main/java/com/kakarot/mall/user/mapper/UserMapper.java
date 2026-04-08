package com.kakarot.mall.user.mapper;

import com.kakarot.mall.user.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User selectById(@Param("id") Long id);

    User selectByOpenId(@Param("openId") String openId);

    void insert(User user);
}
