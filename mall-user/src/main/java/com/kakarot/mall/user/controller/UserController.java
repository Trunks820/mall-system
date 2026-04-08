package com.kakarot.mall.user.controller;

import com.kakarot.mall.common.result.Result;
import com.kakarot.mall.infra.context.UserContext;
import com.kakarot.mall.user.service.UserService;
import com.kakarot.mall.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    public Result<UserVO> current() {
        return Result.success(userService.getCurrentUser(UserContext.getUserId()));
    }
}
