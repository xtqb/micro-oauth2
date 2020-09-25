package com.macro.cloud.component;

import com.macro.cloud.domain.SecurityUser;
import com.macro.cloud.log.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JWT内容增强器
 * Created by macro on 2020/6/19.
 *  TokenEnhancer：在AuthorizationServerTokenServices 实现存储访问令牌之前增强访问令牌的策略。
 * 下面是自定义TokenEnhancer的代码
 *
 * 如果你想往JWT中添加自定义信息的话，比如说登录用户的ID，可以自己实现TokenEnhancer接口；
 *
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Autowired
    private LoggerUtils loggerUtils;
    @Override                // 获取授权成功
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        loggerUtils.info("access_type : " + accessToken.getTokenType() + "\n , " + accessToken.getValue() + "\n , " + accessToken.getRefreshToken());
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        loggerUtils.info("principal :  ---------------------------  " + securityUser);
        Map<String, Object> info = new HashMap<>();
        //把用户ID设置到JWT中
        info.put("id", securityUser.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
