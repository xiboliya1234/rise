package com.rise.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.rise.controller.LoginVueController;
import com.rise.model.userModel;
import com.rise.service.DzService;
import com.rise.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenUtils {

    private static UserService staticUserService;
    private static final Logger logger = LoggerFactory.getLogger(LoginVueController.class);

    @Resource
    private UserService UserService;

    @PostConstruct
    public void setUserService() {
        staticUserService = UserService;
    }

    /**
     * 生成token
     */
    public static String genToken(String adminId, String sign) {
        return JWT.create().withAudience(adminId) // 将 user id 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

    /**
     * 获取当前登录的用户信息
     */
    public static userModel getCurrentUser() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            token = request.getHeader("token");
            if (StrUtil.isBlank(token)) {
                token = request.getParameter("token");
            }
            if (StrUtil.isBlank(token)) {
                logger.error("获取当前登录的token失败， token: {}", token);
                return null;
            }
            // 解析token，获取用户的id
            String Id = JWT.decode(token).getAudience().get(0);
            logger.info("获取当前登录", JWT.decode(token).getAudience());
            logger.info("获取当前登录", staticUserService.findUserByID(Integer.valueOf(Id)));
            return staticUserService.findUserByID(Integer.valueOf(Id));
        } catch (Exception e) {
            logger.error("获取当前登录的管理员信息失败, token={}", token,  e);
            return null;
        }
    }
}

