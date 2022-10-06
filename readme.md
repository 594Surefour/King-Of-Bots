# King Of Bots

## [1]项目简介

![截屏2022-09-17 10.44.47](./assets/截屏2022-09-17 10.44.47.png)

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

1、把所有游戏对象都存下

<img src="assets/截屏2022-09-29 15.42.13.png" alt="截屏2022-09-29 15.42.13" style="zoom:50%;" />

在components下定义 PlayGround.vue GameMap.vue

<img src="assets/截屏2022-09-29 15.44.26.png" alt="截屏2022-09-29 15.44.26" style="zoom:50%;" />

2、写成迭代函数 每一帧都执行

定义在基类GameObject中

```javascript
const GAME_OBJECT = [];

export class GameObject{
    constructor() {
        GAME_OBJECT.push(this);
        this.has_called_start = false;
        this.timedelta = 0;
    }

    start() {

    }

    update() { 

    }

    on_destory() {

    }

    destory() {
        this.on_destory();

        for(let i in GAME_OBJECT){
            const obj = GAME_OBJECT[i];
            if(obj === this){
                GAME_OBJECT.splice(i);
                break; 
            }
        }
    }
}

let last_timestep;
const step = timestamp => {
    for(let obj of GAME_OBJECT){
        if(!obj.has_called_start){
            obj.has_called_start = true;
            obj.start();
        }
        else{
            obj.timedelta = timestamp - last_timestep;
            obj.update();
        }
    }
    last_timestep = timestamp;
    requestAnimationFrame(step);
}
requestAnimationFrame(step);
```

3、相对距离 + 剧中

```javascript
<template>
    <div class="playground">
        <GameMap />
    </div>
</template>

<script>
    import GameMap  from "./GameMap.vue";
    export default{
        components: {
            GameMap,
        }
    }
</script>

<style scoped>
    div.playground {
        width: 60vw;
        height: 70vh;
        margin: 50px auto;
    }
</style>
```

4、游戏画到canvas画布里 + 创建游戏对象，调整css属性

```javascript
<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas"></canvas>
    </div>
</template>


<script>
    import { GameMap } from "@/assets/scripts/GameMap";
    import { ref, onMounted } from "vue";

    export default{
        setup() {
            let parent = ref(null);
            let canvas = ref(null);

            onMounted(() => {
                new GameMap(canvas.value.getContext('2d'), parent.value)
            });

            return {
                parent,
                canvas,
            }
        }
    }
</script>


<style scoped>
    div.gamemap {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
```

5、处理格子+ canvas坐标系

```javascript
//调整对战区域为符合GameMap的最小正方形
update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows) );
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        this.render();
    }

    render() {
      //小矩形颜色
        const color_even = "#AAD751", color_odd = "#A2D149";
        for(let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if((r+c) % 2 == 0){
                    this.ctx.fillStyle= color_even;
                }
                else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
```

6、设置 围墙 and 障碍物 + 轴线对称

为了消除浮点数的影响，调整update_size()函数

```javascript
update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows) );
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
}
```

创建围墙函数

```javascript
		create_walls() {
        const g = [];
        for(let r = 0; r < this.rows; r++){
            g[r] = [];
            for(let c = 0; c < this.cols; c++)
                g[r][c] = false;
        }
        //轴对称设置 随机障碍物
        for(let i = 0; i < this.innner_wall_count; i++){
            for(let j = 0; j < 1000; j++){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[c][r])
                    continue;
                if((r == this.rows-2 && c == 1) || (r == 1 && c == this.cols - 2))
                    continue;
                g[r][c] = g[c][r] = true;
                break;
            }
        }
				
  			//设置四周墙体
        for(let r = 0; r < this.rows; r++)
            g[r][0] = g[r][this.cols-1] = true;
        for(let c = 0; c < this.cols; c++)
            g[0][c] = g[this.rows-1][c] = true;

        //将内部随机墙体 + 四周墙体 展示出来
        for(let r = 0; r < this.rows; r++){
            for(let c = 0; c < this.cols; c++){
                if(g[r][c])
                    this.walls.push(new Wall(r, c, this));
            }
        }
        return true;
    }
```

7、保证连通 使用 Flood-Fill 算法

```javascript
//在GameMap.js中实现函数
	check_connectivity(g, sx, sy, tx, ty) {
        if(sx == tx && sy == ty)
            return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i++){
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty))
                return true;
        }
        return false;
    }

//在create_map()中添加连通性判断
const copy_g = JSON.parse(JSON.stringify(g));
if(!this.check_connectivity(copy_g, this.rows-2, 1, 1, this.cols-2))
		return false;
```

8.为了避免两条蛇在某时刻同时进入同一格子，将地图改为长方形后，设置障碍为中心对称

this.rows = 13; this.cols = 14;

```javascript
				//随机障碍物
        for(let i = 0; i < this.innner_wall_count; i++){
            for(let j = 0; j < 1000; j++){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c])
                    continue;
                if((r == this.rows-2 && c == 1) || (r == 1 && c == this.cols - 2))
                    continue;
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
                break;
            }
        }
```

------------------------------地图部分------------------------------

------------------------------蛇部分--------------------------------

8.创建蛇的类 snake.js



9.实现移动



10.优化蛇身



11.增加碰撞检测



12.添加蛇眼睛







## [4]MySql数据库与注册登录

#### (1)mysql命令行指令

​	(1-1)mysql服务的关闭与启动（默认开机自动启动，如果想手动操作，可以参考如下命令）
​		关闭：net stop mysql80
​		启动：net start mysql80
(1-2)mysql的常用操作
​	连接用户名为root，密码为123456的数据库服务：mysql -uroot -p123456
​	show databases;：列出所有数据库
​	create database kob;：创建数据库
​	drop database kob;：删除数据库
​	use kob;：使用数据库kob
​	show tables;：列出当前数据库的所有表
​	create table user(id int, username varchar(100))：创建名称为user的表,表中包含id和username两个属性。
​	drop table user;：删除表
​	insert into user values(1, 'yxc');：在表中插入数据
​	select * from user;：查询表中所有数据
​	delete from user where id = 2;：删除某行数据

#### (2)配置后端环境

(2-1)在pom.xml文件中添加依赖：
	Spring Boot Starter JDBC
	Project Lombok
	MySQL Connector/J
	mybatis-plus-boot-starter
	mybatis-plus-generator

(2-2)在application.properties中添加数据库配置：

```properties
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.url=jdbc:mysql://localhost:3306/kob?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

(2-3)SpringBoot中的常用模块
	pojo层：将数据库中的表对应成Java中的Class
	mapper层（也叫Dao层）：将pojo层的class中的操作，映射成sql语句
	service层：写具体的业务逻辑，组合使用mapper中的操作
	controller层：负责请求转发，接受页面过来的参数，传给Service处理，接到返回值，再传给页面

(2-4)spring-security

maven添加 spring-boot-starter-security，该模块自带 login\logout组件

​	发现访问controller对应的页面需要登录：

<img src="assets/截屏2022-10-06 11.03.48.png" alt="截屏2022-10-06 11.03.48" style="zoom: 33%;" />

​	此处默认用户名为 'user' 密码每次随机生成可在idea输出中看到 

![截屏2022-10-06 11.03.13](assets/截屏2022-10-06 11.03.13.png)

​	为可以使用自己数据库中的用户进行security登录，实现service.impl.UserDetailsServiceImpl类，继承自UserDetailsService接口，用来接入数据库信息，同时创建实现类 UserDetailsImpl

```java
package com.kob.service.Impl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null)
            throw new RuntimeException("用户不存在");

        return new UserDetailsImpl(user);
    }
}
```

```java
package com.kob.service.Impl.utils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

​	实现config.SecurityConfig类，用来实现用户密码的加密存储

```java
package com.kob.config;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

```

(2-5)jwt验证

不需要跨域、不需要在服务区端存储

​	pom.xml中添加3个依赖：
​		jjwt-api、jjwt-impl、jjwt-jackson

​	创建3个工具类：
​		实现utils.JwtUtil类，为jwt工具类，用来创建、解析jwt token
​		实现config.filter.JwtAuthenticationTokenFilter类，用来验证jwt token，如果验证成功，则将User信息注入上下文中
​		配置config.SecurityConfig类，放行登录、注册等接口

(2-6)编写API

实现/user/account/token/：验证用户名密码，验证成功后返回jwt token（令牌）
实现/user/account/info/：根据令牌返回用户信息
实现/user/account/register/：注册账号





## [5]创建个人中心模块

实现后端API
/user/bot/add/：创建一个Bot
/user/bot/remove/：删除一个Bot
/user/bot/update/：修改一个Bot
/user/bot/getlist/：查询Bot列表





## [6]实现微服务：匹配系统



## [7]实现微服务：Bot代码的执行



## [8]创建对战列表与排行榜



## [9]项目上线



## [10]App端



