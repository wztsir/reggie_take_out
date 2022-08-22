package com.wzt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.common.R;
import com.wzt.reggie.dto.DishDto;
import com.wzt.reggie.entity.Dish;
import com.wzt.reggie.entity.DishFlavor;
import com.wzt.reggie.mapper.DishMapper;
import com.wzt.reggie.service.DishFlavorService;
import com.wzt.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService flavorService;


    /**
     * 添加两个表
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishid = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishid);

            return item;
        }).collect(Collectors.toList());
        //dishFlavorService.saveBatch(dishDto.getFlavors());
        //保存菜品口味到菜品数据表dish_flavor
        flavorService.saveBatch(flavors);
    }



    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
//      菜品的基本信息
        Dish dish =this.getById(id);

        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

//      加入菜品的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);

        List<DishFlavor> list=flavorService.list(queryWrapper);
        dishDto.setFlavors(list);

        return dishDto;


    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
//        基本的表
        this.updateById(dishDto);
//         dish_flavor先删除后更新
//          数据的数量可能都不一致，通过set数据更麻烦
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        flavorService.remove(queryWrapper);

//        后更新
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        flavorService.saveBatch(flavors);

    }


}
