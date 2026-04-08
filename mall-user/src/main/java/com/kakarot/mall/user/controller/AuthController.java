package com.kakarot.mall.user.controller;

import com.kakarot.mall.common.result.Result;
import com.kakarot.mall.user.dto.WxLoginDTO;
import com.kakarot.mall.user.service.AuthService;
import com.kakarot.mall.user.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/wxLogin")
    public Result<LoginVO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        return Result.success(authService.wxLogin(dto));
    }
}
