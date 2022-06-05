package com.dsk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dsk.common.BaseResponse;
import com.dsk.exception.BusinessException;
import com.dsk.common.ErrorCode;
import com.dsk.common.ResultUtil;
import com.dsk.contant.UserContant;
import com.dsk.domain.User;
import com.dsk.request.UserLoginRequest;
import com.dsk.request.UserRegisterRequest;
import com.dsk.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "UserController")
@RestController
@RequestMapping("/user")
public class UserController {




    @Resource
    private UserService userService;



    @ApiOperation(value = "用户注册", notes = "用户的注册")
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (StringUtils.isAnyBlank(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword(),
                userRegisterRequest.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }
        long userId = userService.userRegister(userRegisterRequest.getUserAccount(), userRegisterRequest.getUserPassword(),
                userRegisterRequest.getCheckPassword());
        return ResultUtil.success(userId);
    }


    @ApiOperation(value = "用户登录", notes = "用户的登录")
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (StringUtils.isAnyBlank(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }
        User user = userService.userLogin(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword(), request);
        return ResultUtil.success(user);
    }


    @ApiOperation(value = "退出登录", notes = "用户退出登录系统")
    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }
        Integer logout = userService.logout(request);
        return ResultUtil.success(logout);
    }

    @ApiOperation(value = "获取用户当前信息", notes = "获取用户当前信息")
    @GetMapping("/current")
    public BaseResponse<User> current(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserContant.USER_LOGIN_STATE);
        if (user == null) {
            // throw new BusinessException(ErrorCode.NULL_ERROR);
            return null;
        }
        // todo 封号的不能查询出来
        User newUser = userService.getById(user.getId());
        User safeUser = userService.getSafeUser(newUser);
        return ResultUtil.success(safeUser);
    }


    @ApiOperation(value = "用户查询", notes = "通过用户名查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "String")
    })
    @GetMapping("/serach")
    public BaseResponse<List<User>> serachUsers(String username, HttpServletRequest request) {
        // 鉴权
        if (!this.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        // todo
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        List<User> userList = userService.list(queryWrapper);
        List<User> users = userList.stream().map(user -> userService.getSafeUser(user)).collect(Collectors.toList());
        return ResultUtil.success(users);
    }


    @ApiOperation(value = "删除用户", notes = "通过用户ID删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        // 鉴权
        if (!this.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }
        boolean flag = userService.removeById(id);
        return ResultUtil.success(flag);
    }

    private boolean isAdmin(HttpServletRequest request) {
        // 鉴权
        User user = (User) request.getSession().getAttribute(UserContant.USER_LOGIN_STATE);
        return user != null && user.getUserRole() == UserContant.ADMIN_ROLE;
    }

}
