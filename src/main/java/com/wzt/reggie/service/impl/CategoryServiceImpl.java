package com.wzt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.common.CustomException;
import com.wzt.reggie.entity.Category;
import com.wzt.reggie.entity.Dish;
import com.wzt.reggie.entity.Setmeal;
import com.wzt.reggie.mapper.CategoryMapper;
import com.wzt.reggie.service.CategoryService;
import com.wzt.reggie.service.DishService;
import com.wzt.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 自写根据Id删除分类的逻辑代码，不依赖mybatisplus的框架
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
//      查询分类是否关联了菜品，dish
//      条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联菜品，不能删除");
        }
        //      查询分类是否关联了菜品，dish
//      条件构造器
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);

        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联菜品，不能删除");
        }

//      正常删除,调用 mybatisplus 框架
        super.removeById(id);
    }
}
