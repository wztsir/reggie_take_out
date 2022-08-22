package com.wzt.reggie.dto;

import com.wzt.reggie.entity.Dish;
import com.wzt.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 展现层数据与后端数据库数据不一致,多一层封装
 * 全称为Data Transfer object，即数据传输对象，一般用于展示层与服务层之间的数据传输。
 */
@Data
public class DishDto extends Dish {


    private List<DishFlavor> flavors=new ArrayList<>();

//  后面两个暂时没用上

    private String categoryName;

    private Integer copies;

}
