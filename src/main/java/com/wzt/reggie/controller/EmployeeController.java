package com.wzt.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzt.reggie.common.R;
import com.wzt.reggie.entity.Employee;
import com.wzt.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

//Slf4j日志所用的注解
@Slf4j
@RestController
//所有的请求都含有employee
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 后台登入功能
     *
     * @param request
     * @param employee
     * @return
     */
    // 前端传入的json数据，属性名称要和对象的属性一致
    //  前端是表单，post提交
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

//      1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

//      2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

//      lambda语法
//      对象::非静态方法
//      实现调用getUsername方法
//      作用：舍弃硬编码
//      大致原理：lambda语法，并且通过序列化，Employee::getUsername 得到 'username' 字段

        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
//      3、没有查询到返回登入失败
        if (emp == null) {
            return R.error("登入失败");
        }
//      4、密码比对，不一致返回登入失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登入失败");
        }
//      5、查看员工的状态是否为禁用
        if (emp.getStatus() == 0) {
            return R.error("账户已禁用");
        }

//      6、登入成功，将个人数据存入session,就是变量HttpServletRequest request 的作用
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 登出功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
//        清理session中能保存的当前登入的员工id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");

    }

    /**
     * 新增员工
     *
     * @param request  获取session对象
     * @param employee 从前端获取数据
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
//      补全信息
//username=fafs, name=wzt, password=null, phone=17807942982,
// sex=1, idNumber=362531200207020618, status=null, (有默认值)
// createTime=null, updateTime=null, createUser=null, updateUser=null)
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//       使用自动填充
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 禁用功能：传输的是id与status，直接用employ承接即可
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
//        HttpServletRequest request
//        Long empId=(Long) request.getSession().getAttribute("employee");
//        自动填充
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());
//      根据id找到数据并且修改数据，但是传入的参数仍是employee

//        long id =Thread.currentThread().getId();
//        log.info("controller线程ID为：{}",id);

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }


//    分页功能，前端发送分页
//   employee/page?page=1&pageSize=10

    /**
     * 分页功能，兼查询功能，查询的条目是name(字符串)
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
//        直接从路径获得数据，不是rest风格的请求
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        //condition为boolean类型，返回true，则添加条件，返回false则不添加条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件，更新时间
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询,将查询结果封装入pageInfo
        employeeService.page(pageInfo, queryWrapper);

        log.info("查询员工数据为：{}", pageInfo);
        return R.success(pageInfo);
    }

    /**
     * 修改员工信息，必要的先展示员工信息
     *
     * @param id
     * @return
     */
//  rest风格开发，信息放在路径上
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
//      前端页面可以在 page 响应中查看，长整型的数据在展示的时候会丢失精度
        log.info("根据id: {}查询员工信息...", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) return R.success(employee);
        return R.error("没有查询到对应员工信息");
    }

}
