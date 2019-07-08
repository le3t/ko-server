package org.ko.sigma.rest.system.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.ko.sigma.core.support.Response;
import org.ko.sigma.core.type.SystemCode;
import org.ko.sigma.data.entity.User;
import org.ko.sigma.rest.system.service.SystemService;
import org.ko.sigma.rest.user.dto.UserDTO;
import org.ko.sigma.rest.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@RestController
@Api(tags = "系统操作相关")
@Validated
public class SystemController {

    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired private SystemService systemService;

    @Autowired private UserService userService;

    @Autowired private ProviderSignInUtils providerSignInUtils;

    /**
     * 请求的缓存
     */
    private RequestCache requestCache = new HttpSessionRequestCache();

    @PostMapping("login")
    @ApiOperation("账号密码登录")
    public Response login (
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("用户口令") @RequestParam String password) {
        return Response.of(true);
    }


    @GetMapping("logout")
    @ApiOperation("登出系统")
    public Response<Long> logout () {
        return new Response<>(1L);
    }

    @PostMapping("register")
    @ApiOperation("注册新用户")
    public Response<UserDTO> register (@ApiParam("用户信息对象") @RequestBody UserDTO userDTO, HttpServletRequest request) {
        System.out.println("注册用户");
        Long userId = userService.createUser(map(userDTO));
        if (userId != null) {
            String username = userDTO.getUsername();
            providerSignInUtils.doPostSignUp(username, new ServletWebRequest(request));
        }
        return new Response<>(userDTO);
    }

    @GetMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ApiOperation("需要权限验证的引导接口")
    public Response<String> requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /**
         * 拿到引发跳转的请求
         */
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (Objects.nonNull(savedRequest)) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的URL是: {}", targetUrl);
        }
        return Response.of(SystemCode.REQUIRE_AUTHENTICATION);
    }

    @GetMapping("session/invalid")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Response sessionInvalid () {
        return Response.of("session失效");
    }


    /**
     * UserDTO mapTo User
     * @param userDTO
     * @return
     */
    private User map (UserDTO userDTO) {
        User user = new User();
        if (userDTO != null) {
            BeanUtils.copyProperties(userDTO, user);
        }
        return user;
    }

}
