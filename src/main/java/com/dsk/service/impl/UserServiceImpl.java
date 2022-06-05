package com.dsk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dsk.common.ErrorCode;
import com.dsk.contant.UserContant;
import com.dsk.domain.User;
import com.dsk.exception.BusinessException;
import com.dsk.mapper.UserMapper;
import com.dsk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-05-04 19:39:59
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final String SALT = "dsk";


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 检验非空
        if (StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"参数不能为空");
        }
        // 账户不能小于4位
        if (userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"账户不能小于4位");
        }
        // 密码不能小于8位
        if (userPassword.length()<8||checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"密码不能小于8位");
        }
        // 账号不能包含特殊字符
        String validPattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*()————|{}【】‘；：”“'。，、？\\\\]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"账号不能包含特殊字符");
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"密码和校验密码相同");
        }
        // 验证用户账号是否在数据库存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        long count = this.count(queryWrapper);
        if (count>0){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"账号已存在");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes(StandardCharsets.UTF_8));
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptPassword);
        this.save(user);
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 检验非空
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        // 账户不能小于4位
        if (userAccount.length()<4){
            return null;
        }
        // 密码不能小于8位
        if (userPassword.length()<8){
            return null;
        }
        // 账号不能包含特殊字符
        String validPattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*()————|{}【】‘；：”“'。，、？\\\\]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        matcher.find();

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes(StandardCharsets.UTF_8));
        // 验证用户账号密码是否匹配
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("password",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user==null){
            log.info("user login failed");
        }
        User safeUser = this.getSafeUser(user);
        // 记录登录态
        request.getSession().setAttribute(UserContant.USER_LOGIN_STATE,safeUser);
        return safeUser;
    }

    @Override
    public Integer logout(HttpServletRequest request) {
         request.getSession().removeAttribute(UserContant.USER_LOGIN_STATE);
         return 1;
    }

    // 数据脱密
    @Override
    public User getSafeUser(User orginUser){
        if (orginUser==null){
            return null;
        }
        User safeUser = new User();
        safeUser.setId(orginUser.getId());
        safeUser.setUsername(orginUser.getUsername());
        safeUser.setUserAccount(orginUser.getUserAccount());
        safeUser.setUserRole(orginUser.getUserRole());
        safeUser.setAvatarUrl(orginUser.getAvatarUrl());
        safeUser.setGender(orginUser.getGender());
        safeUser.setPhone(orginUser.getPhone());
        safeUser.setEmail(orginUser.getEmail());
        safeUser.setFlagStatus(orginUser.getFlagStatus());
        safeUser.setCreateTime(orginUser.getCreateTime());
        return safeUser;
    }
}




