package com.macro.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @description 资源服务器
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    public static final String ROLE_ADMIN = "ADMIN";
    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("配置OAuth 资源配置");
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                //请求权限配置
                .authorizeRequests()
                //下边的路径放行,不需要经过认证
                .antMatchers("/user/**").permitAll()
                //OPTIONS请求不需要鉴权
                .antMatchers(HttpMethod.OPTIONS, "/user/**").permitAll()
                //用户的增删改接口只允许管理员访问
                .antMatchers(HttpMethod.POST, "/user/**").hasAnyAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT, "/user/**").hasAnyAuthority(ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE, "/user/**").hasAnyAuthority(ROLE_ADMIN);
                //其余接口没有角色限制，但需要经过网关远程认证，只要携带token就可以放行
    }
}
