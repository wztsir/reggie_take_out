package com.wzt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzt.reggie.dto.SetmealDto;
import com.wzt.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
