package com.wzt.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.wzt.reggie.common.BaseContext;
import com.wzt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebFault;
import java.io.IOException;
import java.util.logging.LogRecord;

import static com.wzt.reggie.common.BaseContext.*;
import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;



@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //    路径匹配器，支持通配符，比较路径是否一致
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//      方便使用提供的API，要类型转换
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        1、获取本次请求的URI
        String uri = request.getRequestURI();
        log.info("拦截到请求：{}", request.getRequestURI());//URI 是长度短的
//        filterChain.doFilter(request,response);
        //不需要处理的请求路径
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",//网页后端的页面

                "/front/**",//客户端的页面
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登入


        };
//        2、判断本次请求是否在放行列表里
        boolean check = check(urls, uri);
//        3、不需要处理，直接放行
        if (check) {
            filterChain.doFilter(request, response);
            log.info("本次请求{}不需要处理", uri);
            return;
        }
//        4、判断登入状态，如果已经登入，直接可以放行的
//        session 关闭浏览器，再打开，则浏览器中的jsessionid会被浏览器自动清理
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");

//          线程
            setThreadLocalId(empId);

//          测试
//            long id =Thread.currentThread().getId();
//            log.info("线程ID为：{}",id);

            filterChain.doFilter(request, response);
            return;
        }

//        4.2判断客户端的登入状态，如果已经登入了则可以放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登入，用户id为:{}", request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
//         在线程空间中保存数据
            setThreadLocalId(userId);

            filterChain.doFilter(request, response);
            return;
        }



//        5、会话中没有数据，要通过输出流的方式向客户端页面相应数据 r.error中code默认是0
//        前端拿到没有登入的数据，回跳到登入页面
        log.info("用户未登录");
//      controller注解开发，会转成Jason数据传输
        response.getWriter().write(com.alibaba.fastjson.JSON.toJSONString(R.error("NOTLOGIN")));
    }


    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
