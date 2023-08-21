package com.wzt.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzt.reggie.dto.SetmealDto;
import com.wzt.reggie.entity.Setmeal;
import com.wzt.reggie.entity.SetmealDish;
import com.wzt.reggie.mapper.SetmealMapper;
import com.wzt.reggie.service.SetmealDishService;
import com.wzt.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
//       操作setmeal，新建套餐名
//        细节的setmealDto extends setmeal ,所以参数可以接纳setmealDto
        this.save(setmealDto);

//      前端数据中并没有套餐名id,需要加入
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

//        操作setmeal_dish,新建对应套餐的每一个菜品
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto=new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        细节的，类名是大写的
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);

        setmealDto.setSetmealDishes(list);
        return setmealDto;

    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
//      更新setmeal表
        this.updateById(setmealDto);
//      更新setmeal_dish delete
//        根据setmeal_id 删除，删除多个
//        细节的犯错，不是通过主码id删除数据，而是根据setmeal_id删除数据
//        setmealDishService.removeById(setmealDto.getId());
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> SetmealDishs = setmealDto.getSetmealDishes();
//      细节的，流处理并不改变原来有的数据
        SetmealDishs=SetmealDishs.stream().map((item)->{
         item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(SetmealDishs);

    }
}
