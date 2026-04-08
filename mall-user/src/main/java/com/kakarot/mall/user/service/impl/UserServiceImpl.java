package com.kakarot.mall.user.service.impl;

import com.kakarot.mall.common.exception.BizException;
import com.kakarot.mall.user.entity.User;
import com.kakarot.mall.user.mapper.UserMapper;
import com.kakarot.mall.user.service.UserService;
import com.kakarot.mall.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BizException("用户不存在");
        }

        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setPhone(user.getPhone());
        return vo;
    }
}
