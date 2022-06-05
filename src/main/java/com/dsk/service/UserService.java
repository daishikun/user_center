package com.dsk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dsk.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-05-04 19:39:59
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount,String userPassword,String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    Integer logout(HttpServletRequest request);
    // 数据脱密
    User getSafeUser(User orginUser);
}
