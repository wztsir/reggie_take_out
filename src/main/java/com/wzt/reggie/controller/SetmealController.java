package com.wzt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzt.reggie.common.R;
import com.wzt.reggie.dto.SetmealDto;
import com.wzt.reggie.entity.Category;
import com.wzt.reggie.entity.Setmeal;
import com.wzt.reggie.service.CategoryService;
import com.wzt.reggie.service.SetmealDishService;
import com.wzt.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SetmealController
 * @Description: 套餐模块
 * @Author: wzt
 * @Create: 2022-08-13 17:10
 **/

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;


    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 同时操作两个表
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {

        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }


    /**
     * 删除单个或者批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String[] ids) {
        int index = 0;
        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            if (setmeal.getStatus() != 1) {
                setmealService.removeById(id);
            } else {
                index++;
            }
        }
        if (index > 0 && index == ids.length) {
            return R.error("选中的套餐均为启售状态，不能删除");
        } else {
            return R.success("删除成功");
        }
    }

    /**
     * 停售或者启售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, String[] ids) {
        for (String id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }

        return R.success("修改成功");
    }


    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }

    /**
     * 分页查询套餐信息，类似于员工查询
     * 并且根据套餐id查询出套餐名  categoryId->categoryName
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);

        Page<SetmealDto> pageDtoInfo = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(!StringUtils.isEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, pageDtoInfo, "records");

        List<Setmeal> records = pageInfo.getRecords();
//根据categoryId查找对应的分类名称
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        pageDtoInfo.setRecords(list);

        return R.success(pageDtoInfo);
    }

    /**
     * 修改分类的第一步，显示数据
     * 特殊的，根据dish_id 展示meal_dish
     * 操作两个表
     *
     * @param id dish_id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }


    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
