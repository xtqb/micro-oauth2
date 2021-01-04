package com.macro.cloud.service;

import cn.hutool.core.collection.CollUtil;
import com.macro.cloud.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 资源与角色匹配关系管理业务类
 * Created by macro on 2020/6/19.
 * <p>
 * redis 预先存储资源访问权限
 *      白名单列的接口名，这里不用设置授权
 */
@Component
public class ResourceServiceImpl {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void initData() {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        resourceRolesMap.put("/api/hello", CollUtil.toList("ADMIN"));
        resourceRolesMap.put("/api/user/currentUser", CollUtil.toList("ADMIN", "TEST"));
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
        resourceRolesMap.clear();
    }
}
