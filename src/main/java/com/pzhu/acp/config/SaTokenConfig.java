package com.pzhu.acp.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: gali
 * @Date: 2023-04-13 22:10
 * @Description:
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 指定一条 match 规则
            SaRouter
                    .match("/**")// 拦截的 path 列表，可以写多个 */
                    .notMatch(
                            "/user/login",
                            "/user/register",
                            "/user/getEmailCode",
                            "/user/adminLogin",
                            "/user/current",
                            "/front/index/*",
                            "/answer/getAnswerByPageOrParam",
                            "/banner/getBannerByPage",
                            "/captcha/*",
                            "/college/ShowAllCollegeByPage",
                            "/comment/getCommentByPageOrParam",
                            "/discuss/getDiscussByPageOrParam",
                            "/discuss/getDiscussInfoById/*",
                            "/dynamic/getDynamicByPageOrParam",
                            "/dynamic/getDynamicById/*",
                            "/notice/getNoticeByPage",
                            "/origin/GetOriginByQueryAndPage",
                            "/originUser/getOriginUsers",
                            "/push/getPushInfoByPageOrParam",
                            "/question/getQuestionByPage",
                            "/question/getQuestionInfoById/*",
                            "/region/getRegionTreeInfo",
                            "/tag/showAllTag")// 排除掉的 path 列表，可以写多个
                    .check(r -> StpUtil.isLogin());        // 要执行的校验动作，可以写完整的 lambda 表达式
            // 根据路由划分模块，不同模块不同鉴权
        })).addPathPatterns("/**");

    }
}

