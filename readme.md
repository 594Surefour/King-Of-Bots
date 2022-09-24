# King Of Bots

## [1]项目简介

![截屏2022-09-17 10.44.47](assets/截屏2022-09-17 10.44.47.png)

→项目包含的模块
	PK模块：匹配界面（微服务）、实况直播界面（WebSocket协议）
	对局列表模块：对局列表界面、对局录像界面
	排行榜模块：Bot排行榜界面
	用户中心模块：注册界面、登录界面、我的Bot界面、每个Bot的详情界面
→前后端分离模式
	SpringBoot实现后端
	Vue3实现Web端和AcApp端



## [2]环境配置

在SpringBoot中解决跨域问题
添加配置类：CorsConfig

```java
package com.kob.backend.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CorsConfig implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String origin = request.getHeader("Origin");
        if(origin!=null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        String headers = request.getHeader("Access-Control-Request-Headers");
        if(headers!=null) {
            response.setHeader("Access-Control-Allow-Headers", headers);
            response.setHeader("Access-Control-Expose-Headers", headers);
        }

        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {
    }
}
```



## [3]创建菜单与游戏界面





## [4]MySql数据库与注册登录



## [5]创建个人中心模块



## [6]实现微服务：匹配系统



## [7]实现微服务：Bot代码的执行



## [8]创建对战列表与排行榜



## [9]项目上线



## [10]App端



