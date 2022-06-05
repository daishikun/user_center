package com.dsk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dsk.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author Administrator
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2022-05-04 19:39:59
* @Entity generator.domain.User
*/
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}




