package com.macro.cloud.config;

import cn.hutool.core.util.ArrayUtil;
import com.macro.cloud.authorization.AuthorizationManager;
import com.macro.cloud.component.RestAuthenticationEntryPoint;
import com.macro.cloud.component.RestfulAccessDeniedHandler;
import com.macro.cloud.constant.AuthConstant;
import com.macro.cloud.filter.IgnoreUrlsRemoveJwtFilter;
import com.macro.cloud.log.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 资源服务器配置
 * Created by macro on 2020/6/19.
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    @Autowired
    private LoggerUtils loggerUtils;
    @Autowired
    private AuthorizationManager authorizationManager;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler; // 未授权
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint; // 授权失败
    @Autowired
    private IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;

    public ResourceServerConfig() {
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.oauth2ResourceServer().jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        //自定义处理JWT请求头过期或签名错误的结果
        http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
        //对白名单路径，直接移除JWT请求头
//        http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION); // 添加过滤器 过滤白名单
        http.authorizeExchange()
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()// 这些接口不需要进行进行权限管理器校验
                .anyExchange().access(authorizationManager)//鉴权管理器配置 其他需要验证
                .and().exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)// 拦截未授权的，处理的消息
                .authenticationEntryPoint(restAuthenticationEntryPoint)// 拦截授权失败
                .and().csrf().disable(); // session 失效
        return http.build();
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        loggerUtils.info("jwtAuthenticationConverter --- 授权转换 --------------------------------");
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX); //前缀
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    /**
     * 获取权限鉴别对象
     *
     * @return
     */
//    @Bean
//    public ResourceServerTokenServices tokenServices() {
//        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
//        //认证时资源服务器就相当于客户端，需要向认证服务器声明自己的信息是否匹配
//        remoteTokenServices.setClientId("client-app");
//        remoteTokenServices.setClientSecret("123456");
//        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:9401/rsa/publicKey");
//        return remoteTokenServices;
//    }
}
