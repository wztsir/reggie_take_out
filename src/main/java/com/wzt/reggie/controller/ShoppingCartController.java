package com.wzt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzt.reggie.common.BaseContext;
import com.wzt.reggie.common.R;
import com.wzt.reggie.entity.ShoppingCart;
import com.wzt.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: ShoppingCartController
 * @Description: 购物车
 * @Author: wzt
 * @Create: 2022-08-15 19:14
 **/
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 点击 [加入购物车] 或者 [+] 按钮，页面发送ajax请求，
     * 请求服务端，将菜品或者套餐添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        Long localId = BaseContext.getThreadLocalId();
        shoppingCart.setUserId(localId);

        //查询当前菜品或者套餐是否已经在购物车当中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, localId);

        if (dishId != null) {
            //添加到购物车的为菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的为套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        //SQL:select *from shopping_cart where user_id=? and dish_id/setmeal_id =?

        ShoppingCart cartServiceone = shoppingCartService.getOne(queryWrapper);

        if (cartServiceone != null) {
            //如果已经存在，则在原来的基础上加一
            Integer number = cartServiceone.getNumber();
            cartServiceone.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceone);
        } else {
            //如果不存在，则添加到购物车中，默认为一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceone = shoppingCart;
        }
        return R.success(cartServiceone);

    }

    /**
     * 减少菜品的数量，- 按钮
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long setmealId = shoppingCart.getSetmealId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocalId());

        if (setmealId!=null){
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }else {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        Integer number = one.getNumber();
        if(number==1){
            shoppingCartService.remove(queryWrapper);
            one.setNumber(0);
        }else {
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }

        return R.success(one);
    }


    /**
     * 登入index.html时会自动调用，如果未响应，展示菜品也会失效
     *点击购物车图标，页面发送ajax请求，
     * 请求服务端查询购物车中的菜品和套餐
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocalId());
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocalId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }



}
