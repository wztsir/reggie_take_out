package com.wzt.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzt.reggie.common.R;
import com.wzt.reggie.entity.Category;
import com.wzt.reggie.entity.Employee;
import com.wzt.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


//  响应Ajax请求
//   数据库name唯一
//   前端只要一个R.code,泛型指定最简单的String
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page pageInfo = new Page(page, pageSize);
//        条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,queryWrapper);

        log.info("查询菜单数据为：{}",pageInfo);
        return R.success(pageInfo);
    }



//   前端数据传输 category?ids=1397844303408574465（黑盒）
//   形参应该为ids
    /**L
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping()
    public  R<String> delete(Long ids){
        log.info("删除分类，ids为{}",ids);

//      自实现的删除，检查是否由关联菜品
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
//      自动提取对象属性
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    /**
     * 获取菜品分类或者套餐分类
     * 细节的，客户端展示数据，没有type
     * 菜品分类 type:1
     * 套餐分类 type:2
     * @param category
     * @return
     */
// 前端的需要:this.dishList = res.data
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
//        条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        添加条件,细节的，当没有条件时，查询的是所有数据
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());

//      数据顺序
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return R.success(list);
    }


}
