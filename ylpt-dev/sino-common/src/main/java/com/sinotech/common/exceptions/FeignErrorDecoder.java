package com.sinotech.common.exceptions;

import com.alibaba.fastjson.JSON;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 自定义异常
 * @author ly
 * @title: FeignErrorDecoder
 * @projectName ylpt-cloud
 * @description: TODO
 * @date 2022/2/2318:03
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // 这里直接拿到我们抛出的异常信息
            String message = Util.toString(response.body().asReader());
            Map<String, Object> messageMap = JSON.parseObject(message, HashMap.class);
            return new BaseException(messageMap.get("message").toString(), (Integer) messageMap.get("status"));
        } catch (Exception ignored) {
            ignored.printStackTrace();
            log.error(ignored.getMessage(), ignored);
            return new BaseException(ignored.getMessage(), 500);
        }
    }
}
