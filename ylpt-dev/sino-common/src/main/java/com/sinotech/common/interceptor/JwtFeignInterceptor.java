package com.sinotech.common.interceptor;

import com.sinotech.common.enums.TerminalType;
import com.sinotech.common.utils.SessionUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author 郭训民
 * @description: fegin请求 添加 token
 * @title: JwtFeignInterceptor
 * @projectName Unknown
 * @date 2022/2/26 11:38
 */
@Component
public class JwtFeignInterceptor implements RequestInterceptor {
    /**
     * 容器初始化的时候 打印参数
     */
    @PostConstruct
    public void init() {
        System.err.println("JwtFeignInterceptor 配置初始化 ..................");
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("terminal", TerminalType.SERVICE.getValue());
        if (!template.headers().containsKey("Authorization")) {
            template.header("Authorization", SessionUtils.getToken());
        }
    }
}
