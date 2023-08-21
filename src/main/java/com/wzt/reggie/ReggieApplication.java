package com.wzt.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan//扫描，开始进行静态资源映射
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功...");
    }
//  1、创建Maven项目，并且检查maven路径、runner、jdk
//  2、导入pom文件
//  3、编写yml
//  4、springBoot的启动类
//  5、放置静态页面（开发静态页面），放置在static目录下，如果不放置在static目录下，可以通过设置配置类放行
//  员工登入:
//  6、实现登入的后端功能，熟悉一下前端页面，明白要接受与返回的数据。搭好从mapper、service、controller的架子
//  7、前后端协议，添加通用返回结果类
//  8、重点实现controller,编写完成后，进行测试，添加断点，进入request.js超时时间添加两个0，扩大100倍
//      细节的:存在缓存，更改失败，需要清理浏览器记录
//   员工退出:
//  9、过滤器，必须要先认证才可以登入   细节的要在启动类上加@ServletComponentScan扫描
//  10、新增员工信息 熟悉前端的页面（比较困难），拿到前端的数据，并且补全对应的数据，再交给数据层
//  11、统一异常处理，添加重复username（unique）的数据，会报错
//  12、员工信息的分页查询（首先添加配置插件，添加分页查询的controller(熟练mybatisplus的操作)）
//  13、根据id修改员工信息，首先查询根据id信息，但是，js数据处理long型数据会丢失精度，即传给后端数据丢失后两位数据
//      解决方法：配置消息转换器

//  14、公共字段填充（直接参考baomidou的官方文档）
//    14.1 entity
//    14.2  common->MyMetaObjecthandler 元数据处理器
//    14.3 利用线程，获取当前创建者的id,客户端没发送一次http请求都会分配一个新的线程处理
//    filter 、controller、MyMetaObjecthandler
//    转发的过程：先是转发到controller进行，执行语句employeeService.updateById(employee);会到MyMetaObjecthandler自动填充
//    线程是一样的
//  15、新增分类
//    初始化：Mapper接口； 业务层接口，业务层实现类；控制层骨架
//    查看前端页面，明白前端的请求格式与需求的响应格式
//  16、分类信息分页查询
//在开发代码之前,需要梳理一下整个程序的执行过程
//     16.1、页面发送可ax请求,将分页查询参数(page、 pagesize)提交到服务端
//     16.2、服务端[ ontroller收页面提交的数据井调用 Service?查询数据
//     16.3、 Servicei调用 Mapper操作数据库,查询分页数据
//     16.4、 Controller!将查询到的分页数据响应给页面
//     16.5、页面接收到分页数据并通过 Elementule的 Tablea组件展示到页面上
//  17、删除分类
//     做检查分类是否关联其他菜品或者套餐
//     17.1CategoryServiceImpl中实现逻辑
//     17.2自定义异常
//     自定义异常类
//     将异常加入全局处理
//     （明白了，直接利用mybatisplus的增删改查，成功返回R.success;失败了大概率是数据库没有数据，由异常捕获，返回error）
//  18、修改分类
//    前端页面Vue已经数据回显，直接完成put函数


//  19、菜品的展示
//    文件的展示,文件的上传与下载
//    MultipartFile定义的file变量必须与name保持一致
//    name时el-ui动态生成的
//

//   20、新增菜品
//    搭起来架构
//    难点：多个表的填充
//    前端food/add.html
//     20.1分组的查询
//     20.2DishServiceImpl：细节的开启事务；@EnableTransactionManagement


//  21.信息的分页查询
//    难点：多表查询，将category的categoryName查询出来，并且和dish数据添加到一起



//    Mybatis-plus
//    不包含条件，即没有where是不需要包装器
//    增加  save savebatch
//    删除    remove(包装器)有eq ,removeById(根据主码删除，直接传入对象即可，自动比对主码id)
//    改： 一般涉及条件 eq  但是具体的要set什么数据需要借助包装器  LambdaUpdateWrapper
//    查  getOne,getById
//
}
