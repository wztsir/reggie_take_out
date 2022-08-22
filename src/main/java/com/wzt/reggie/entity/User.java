package com.wzt.reggie.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {
//用来进行数据的序列化与反序列化
//    序列化：对象变成流，字节进行传输与存储时的状态
//    反序列化：对象，展示的状态
    private static final long serialVersionUID = 1L;

    private Long id;


    //姓名
    private String name;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;


    //状态 0:禁用，1:正常
    private Integer status;
}
