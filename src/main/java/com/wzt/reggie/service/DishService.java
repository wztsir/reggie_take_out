package com.wzt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzt.reggie.dto.DishDto;
import com.wzt.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
