package com.wzt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.wzt.reggie.common.BaseContext;
import com.wzt.reggie.common.R;
import com.wzt.reggie.entity.AddressBook;
import com.wzt.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName: AddressBookController
 * @Description: 地址簿
 * @Author: wzt
 * @Create: 2022-08-15 14:44
 **/
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getThreadLocalId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, BaseContext.getThreadLocalId());

//      先将已经存在的defalult设置为0
        //SQL:update address_book set is_default = 0 where user_id = ?
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);

//        设置当前的default
        //SQL:update address_book set is_default = 1 where id = ?
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    @PutMapping
    public R<AddressBook> set(@RequestBody AddressBook addressBook){
//        先删除后新增
        addressBookService.removeById(addressBook.getId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }


    /**
     * 展示所有地址
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {

//       线程的ThreadLocal的范围，多线程的知识
        addressBook.setUserId(BaseContext.getThreadLocalId());
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }


    /**
     * 修改地址，先回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 支付订单的页面需要
     *  获取默认地址
     * @return
     */
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getThreadLocalId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }



}
