package org.ko.prototype.rest.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.ko.prototype.core.support.Response;
import org.ko.prototype.core.type.SystemCode;
import org.ko.prototype.data.master.domain.AdminUser;
import org.ko.prototype.rest.user.condition.AdminUserQueryCondition;
import org.ko.prototype.rest.user.dto.AdminUserDTO;
import org.ko.prototype.rest.user.service.AdminUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@Api("用户模块")
@Validated
public class AdminUserController {

    @Autowired private AdminUserService adminUserService;

    @GetMapping
    @ApiOperation("查询用户列表")
    public Response<List<AdminUserDTO>> queryUserList(AdminUserQueryCondition condition) {
        //1. 查询用户列表数据
        List<AdminUser> adminUsers = adminUserService.queryUserList(condition);

        //2. 如果不为空
        if (CollectionUtils.isNotEmpty(adminUsers)) {
            List<AdminUserDTO> adminUserDTOS = adminUsers.stream().map(this::mapAdminUser).collect(Collectors.toList());
            return new Response<>(adminUserDTOS);
        }
        return new Response<>(SystemCode.EMPTY_DATA);
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("通过ID获取用户详细")
    public Response<AdminUserDTO> queryUserInfo (@ApiParam("用户ID") @PathVariable Long id) {
        AdminUser adminUser = adminUserService.queryUserInfo(id);
        if (Objects.nonNull(adminUser)) {
            return new Response<>(this.mapAdminUser(adminUser));
        }
        return new Response<>(SystemCode.EMPTY_DATA);
    }

    @PostMapping
    @ApiOperation("新建用户")
    public Response<Long> createUser (@RequestBody AdminUserDTO adminUserDTO) {
        Long adminUserId = adminUserService.createUser(mapAdminUser(adminUserDTO));
        return new Response<>(adminUserId);
    }

    /**
     * AdminUser mapTo AdminUserDTO
     * @param adminUser
     * @return
     */
    private AdminUserDTO mapAdminUser (AdminUser adminUser) {
        AdminUserDTO adminUserDTO = new AdminUserDTO();
        BeanUtils.copyProperties(adminUser, adminUserDTO);
        return adminUserDTO;
    }

    /**
     * AdminUserDTO mapTo AdminUser
     * @param adminUserDTO
     * @return
     */
    private AdminUser mapAdminUser (AdminUserDTO adminUserDTO) {
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(adminUserDTO, adminUser);
        return adminUser;
    }


}
