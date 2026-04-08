package com.kakarot.mall.user.service;

import com.kakarot.mall.user.dto.WxLoginDTO;
import com.kakarot.mall.user.vo.LoginVO;

public interface AuthService {

    LoginVO wxLogin(WxLoginDTO dto);
}
