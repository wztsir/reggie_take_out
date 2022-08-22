package com.wzt.reggie.common;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mybatis提供的
 * 公共字段填充
 * 直接参考自动填充的文档
 *
 * 其他表的自动填充也是可行的，不需要其他的变动
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {

//   问题
//    MyMetaObjectHandler类中是不能获得HttpSession对象的，
//    所以我们需要通过其他方式来获取登录用户id。
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
//        用户id的获取需要更多的手段，从session中过得当前用户的id
        metaObject.setValue("createUser",  BaseContext.getThreadLocalId());
        metaObject.setValue("updateUser",  BaseContext.getThreadLocalId());

    }

//    执行更新操作的时候会自动跳转到这个方法

    @Override
    public void updateFill(MetaObject metaObject) {

        metaObject.setValue("updateTime",LocalDateTime.now());
//        用户id的获取需要更多的手段
//        long id =Thread.currentThread().getId();
//        log.info("fill线程ID为：{}",id);
        metaObject.setValue("updateUser", BaseContext.getThreadLocalId());
    }
}
