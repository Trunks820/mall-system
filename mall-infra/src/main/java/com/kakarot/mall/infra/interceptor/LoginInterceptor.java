package com.kakarot.mall.infra.interceptor;

import com.kakarot.mall.common.exception.BizException;
import com.kakarot.mall.common.result.ResultCode;
import com.kakarot.mall.infra.context.LoginUser;
import com.kakarot.mall.infra.context.UserContext;
import com.kakarot.mall.infra.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private static final String HEADER_TOKEN = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(HEADER_TOKEN);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            throw new BizException(ResultCode.UNAUTHORIZED);
        }

        String token = header.substring(TOKEN_PREFIX.length());
        if (jwtUtil.isTokenExpired(token)) {
            throw new BizException(ResultCode.UNAUTHORIZED);
        }

        try {
            Claims claims = jwtUtil.parseToken(token);
            LoginUser loginUser = new LoginUser();
            loginUser.setUserId(Long.parseLong(claims.getSubject()));
            loginUser.setOpenId(claims.get("openId", String.class));
            loginUser.setNickname(claims.get("nickname", String.class));
            UserContext.set(loginUser);
            return true;
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Token解析失败: {}", e.getMessage());
            throw new BizException(ResultCode.UNAUTHORIZED);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        UserContext.remove();
    }
}
