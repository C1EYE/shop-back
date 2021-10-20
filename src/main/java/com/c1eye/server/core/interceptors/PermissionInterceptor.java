package com.c1eye.server.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.c1eye.server.core.LocalUser;
import com.c1eye.server.exception.http.ForbiddenException;
import com.c1eye.server.exception.http.UnAuthenticatedException;
import com.c1eye.server.model.User;
import com.c1eye.server.service.UserService;
import com.c1eye.server.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/16 16:15
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {
    public PermissionInterceptor() {
        super();
    }

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<ScopeLevel> scopeLevel = getScopeLevel(handler);
        //不设置访问等级，都可以访问
        if (!scopeLevel.isPresent()) {
            return true;
        }
        String bearerToken = request.getHeader("Authorization");
        //空
        if (StringUtils.isEmpty(bearerToken)) {
            throw new UnAuthenticatedException(10004);
        }
        //不规范
        if (!bearerToken.startsWith("Bearer")) {
            throw new UnAuthenticatedException(10004);
        }
        //参数不对
        String[] tokens = bearerToken.split(" ");
        if (tokens.length != 2) {
            throw new UnAuthenticatedException(10004);
        }
        //校验token
        String token = tokens[1];
        Optional<Map<String, Claim>> optionalMap = JwtToken.getClaims(token);
        Map<String, Claim> map = optionalMap.orElseThrow(() -> new UnAuthenticatedException(10004));
        //验证权限
        boolean valid = hasPermission(scopeLevel.get(), map);
        if(valid){
            this.setToThreadLocal(map);
        }
        return valid;
    }

    private boolean hasPermission(ScopeLevel scopeLevel, Map<String, Claim> map) {
        int level = scopeLevel.value();
        Integer scope = map.get("scope").asInt();
        if (level > scope) {
            throw new ForbiddenException(10005);
        }
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws
                                                                                                                 Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalUser.clear();
        super.afterCompletion(request, response, handler, ex);
    }

    private Optional<ScopeLevel> getScopeLevel(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ScopeLevel scopeLevel = handlerMethod.getMethodAnnotation(ScopeLevel.class);
            if (scopeLevel == null) {
                return Optional.empty();
            }
            return Optional.of(scopeLevel);
        }
        return Optional.empty();
    }

    private void setToThreadLocal(Map<String,Claim> map){
        Long uid = map.get("uid").asLong();
        Integer scope = map.get("scope").asInt();
        User user = userService.getUserById(uid);
        LocalUser.set(user,scope);

    }
}
