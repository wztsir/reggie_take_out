package com.wzt.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
//捕获这些类产生的异常
@ControllerAdvice(annotations ={RestController.class, Controller.class})
@ResponseBody
//类型 类注解或方法注解
//位置 SpringMVC控制器类或方法定义上方
//作用 设置当前控制器方法响应内容为当前返回值，无需解析
public class GlobalExceptionHandler {

//  注解,自动注入
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error("已经生效"+ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");//按空格将数据分割
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
//        return R.error("数据已存在，请修改账号名");
    }


//  分类管理中存在关联，将抛出异常
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){

        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }



}
