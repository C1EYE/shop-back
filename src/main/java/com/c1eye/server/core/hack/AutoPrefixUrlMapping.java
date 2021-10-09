package com.c1eye.server.core.hack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * author c1eye
 * time 2021/9/28 16:50
 */

public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {

    @Value("${c1eye.api-package}")
    private String apiPackagePath;

    @Override
    protected RequestMappingInfo getMappingForMethod(
            Method method, Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        if (mappingInfo != null) {
            String prefix = this.getPrefix(handlerType);
            return RequestMappingInfo.paths(prefix).build().combine(mappingInfo);
        }
        return mappingInfo;
    }

    private String getPrefix(Class<?> handlerType) {
        String packageName = handlerType.getPackage().getName();
        String dotPath = packageName.replaceAll(apiPackagePath, "");
        return dotPath.replace(".", "/");
    }
}
