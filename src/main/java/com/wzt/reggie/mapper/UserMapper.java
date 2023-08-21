package com.wzt.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzt.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserMapper
 * @Description: 用户登入
 * @Author: wzt
 * @Create: 2022-08-14 19:54
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {


}
