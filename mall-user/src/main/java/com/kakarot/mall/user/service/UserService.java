package com.kakarot.mall.user.service;

import com.kakarot.mall.user.vo.UserVO;

public interface UserService {

    UserVO getCurrentUser(Long userId);
}
