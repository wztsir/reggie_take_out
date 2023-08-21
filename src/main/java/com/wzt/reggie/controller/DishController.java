package com.wzt.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzt.reggie.common.R;
import com.wzt.reggie.dto.DishDto;
import com.wzt.reggie.entity.Category;
import com.wzt.reggie.entity.Dish;
import com.wzt.reggie.entity.DishFlavor;
import com.wzt.reggie.service.CategoryService;
import com.wzt.reggie.service.DishFlavorService;
import com.wzt.reggie.service.DishService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 菜品的新增
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    /**
     * 菜品的分页查询
     *
     * @param page
     * @param pageSize
     * @param name     可以自主搜索菜品
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
//       分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(!StringUtils.isEmpty(name), Dish::getName, name);
//        按照时间降序排序
        queryWrapper.orderByDesc(Dish::getCategoryId).orderByAsc(Dish::getPrice);

//        分页查询
        dishService.page(pageInfo, queryWrapper);
//        前端数据绑定的
//        prop="categoryName"
//        label="菜品分类"
//        要的不是categoryId
//        要进行多表查询

//      拷贝除了records的数据，比如页数大小，等等无关乎内容的数据
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

//      在dishDto中新增categoryName数据
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }


            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    /**
     * 修改；回显数据 /根据Id查询菜品信息与对应的口味信息
     * 数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {

//        错误的访问数据库，还要显示菜品的口味
//        DishDto dishDto= (DishDto) dishService.getById(id);
//        System.out.println("nima");

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 前端传入dish.id查询到对应的菜品有几道菜
     * 前端已经保存菜品分类信息
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件，查询状态为1的（起售状态）
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
//        可能为空
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());

        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getPrice);

        List<Dish> list = dishService.list(lambdaQueryWrapper);


//      客户端新增的功能,加入口味
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            //根据id查         category分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
//             根据dishId查菜品的flavor
            //当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, dishId);
            //SQL: select* from dishflavor where dish_id=?;
            List<DishFlavor> dishFlavorlist = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorlist);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }


    /**
     * 修改菜品，需要更新两个表
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {

        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**
     * 停售与启售菜品
     *
     * @param status
     * @param ids
     * @return
     */
    //http://localhost:8080/dish/status
    // /0?ids=1535902809672359937,1413384757047271425
    @PostMapping("/status/{status}")
    public R<String> sale(@PathVariable int status, String[] ids) {
        for (String id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return R.success("修改成功");
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String[] ids) {
        for (String id : ids) {
            dishService.removeById(id);
        }
        return R.success("删除成功");
    }

}
