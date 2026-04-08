package com.kakarot.mall.user.service.impl;

import com.kakarot.mall.common.exception.BizException;
import com.kakarot.mall.infra.context.LoginUser;
import com.kakarot.mall.infra.jwt.JwtUtil;
import com.kakarot.mall.user.dto.WxLoginDTO;
import com.kakarot.mall.user.entity.User;
import com.kakarot.mall.user.mapper.UserMapper;
import com.kakarot.mall.user.service.AuthService;
import com.kakarot.mall.user.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public LoginVO wxLogin(WxLoginDTO dto) {
        // MVP 阶段 mock：直接用 code 作为 openId
        String openId = "mock_" + dto.getCode();
        log.info("wxLogin mock, code={}, openId={}", dto.getCode(), openId);

        User user = userMapper.selectByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setNickName("微信用户");
            user.setStatus(1);
            userMapper.insert(user);
            log.info("新用户自动注册, userId={}", user.getId());
        }

        if (user.getStatus() != 1) {
            throw new BizException("账号已被禁用");
        }

        LoginUser loginUser = new LoginUser(user.getId(), user.getOpenId(), user.getNickName());
        String token = jwtUtil.generateToken(loginUser);

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(user.getAvatarUrl());
        return vo;
    }
}
