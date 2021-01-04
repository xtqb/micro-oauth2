package com.macro.cloud.authorization;

import cn.hutool.core.convert.Convert;
import com.macro.cloud.constant.AuthConstant;
import com.macro.cloud.constant.RedisConstant;
import com.macro.cloud.log.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 * Created by xtqb on 2020/6/19.
 * 从redis获取该用户的访问权限 数据以键值对的形式存在， key ： 为访问路径  权限为ADMMIN USER TEST等字符串的形式存在
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private LoggerUtils loggerUtils;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        // 获取访问接口，判断是否有权限，截取token，判断token是否已经失效
        loggerUtils.info("AuthorizationManager : 检查接口是否允许访问 -----------------------------");
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        Object obj = redisTemplate.opsForHash().get(RedisConstant.RESOURCE_ROLES_MAP, uri.getPath());
        List<String> authorities = Convert.toList(String.class, obj);
        authorities = authorities.stream().map(auth -> auth = AuthConstant.AUTHORITY_PREFIX + auth).collect(Collectors.toList());
        for(String auth : authorities) {
            loggerUtils.info("auth : " + auth);
        }

        //认证通过且角色匹配通过，可访问当前接口
        // 是否 认证否有权限 是否包含这个权限 是否为空  defaultIfEmpty 如果没有任何数据完成此序列，则提供默认的唯一值
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
