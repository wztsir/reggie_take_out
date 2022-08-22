package com.wzt.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.entity.User;
import com.wzt.reggie.mapper.UserMapper;
import com.wzt.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: UserServiceImpl
 * @Description: 客户端的登入
 * @Author: wzt
 * @Create: 2022-08-14 19:56
 **/
//细节的，同时有继承与实现，先写继承，再写是实现，两者有先后，继承来的东西可以覆盖掉接口中的代码
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User>  implements UserService{
}
