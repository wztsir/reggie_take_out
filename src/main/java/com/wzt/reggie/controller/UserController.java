package com.wzt.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzt.reggie.common.R;
import com.wzt.reggie.entity.User;
import com.wzt.reggie.service.UserService;
import com.wzt.reggie.utils.SMSUtils;
import com.wzt.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @ClassName: UserController
 * @Description: 处理用户登入
 * @Author: wzt
 * @Create: 2022-08-14 20:26
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送验证码，前端数据phone，用user承接
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
//      获取手机号生成验证码
        String phone = user.getPhone();
        if (!StringUtils.isEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

//          阿里云的短信服务，过于复杂，当项目上线后再发送短信至客户手机
//            SMSUtils.sendMessage("wzt外卖", "", phone, code);


//           在session中保存code关键信息，方便两次访问服务器的通信
            session.setAttribute(phone, code);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("手机短信发送失败，请稍后重试");
    }

    /**
     * 客户端登入
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
//        检测传入的数据
        log.info("map:{}", map.toString());
//      获取前端的数据
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//session里的数据
        Object codeSession = session.getAttribute(phone);


        if (codeSession != null && codeSession.equals(code)) {
//            登入成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);

//            判断是否已经注册，无，则自动注册
            if (user == null) {
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
//              id怎么生成的
                userService.save(user);
            }

//            mybatis-plus默认使用的主键生成的策略是IdType.ID_WORKER
//            并且保存在user中
            log.info("user的id为:{}", user.getId());
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登入失败");
    }


    /**
     * 用户登出
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }


}
