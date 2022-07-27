package com.sinotech.common.config;

import com.google.common.collect.Maps;
import com.sinotech.common.model.ResultModel;
import com.sinotech.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author 郭训民
 * @description: 添加 openfeign响应拦截 处理, 获取 相应状态 200,但实际报错的信息
 * @title: FeignOkHttpClientConfig
 * @projectName ylpt-cloud
 * @date 2022/2/27 17:52
 */
@Slf4j
@Configuration
public class FeignOkHttpClientConfig {
    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {
        return new OkHttpClient.Builder().addInterceptor(new FeignOkHttpClientResponseInterceptor());
    }

    /**
     * okHttp响应拦截器
     */
    public static class FeignOkHttpClientResponseInterceptor implements Interceptor {


        /**
         * 返回 三种场景
         * 1: 正常返回
         * 2: 被包装后的 异常返回
         *      a:真实异常
         *      b:业务异常
         * @param chain
         * @return
         * @throws IOException
         */
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request originalRequest = chain.request();
            Response response = chain.proceed(originalRequest);

            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            // 包装异常响应 样例  {"msg":"java.lang.ArithmeticException: / by zero","code":"500","rows":[],"totalInfo":[]}
            // 真实异常响应 样例  {"timestamp":1646119162970,"status":500,"error":"Internal Server Error","message":"未知的终端类型","path":"/department/selectDepartmentById"}
            // {
            //	"timestamp":1646366933437,
            //	"status":404,
            //	"error":"Not Found",
            //	"message":"No message available",
            //	"path":"/cloud/selectEmployeeByEmpid"
            //}
            // 解析content，做你想做的事情！！
            if (200 == response.code()) {
                if (content.contains("\"code\":\"") && content.contains("\"msg\":")) {
                    Map<Object, Object> responseMap = Maps.newHashMapWithExpectedSize(4);
                    if (!content.contains("\"code\":\"200\"")) {
                        //返回了 被包装的业务异常
                        ResultModel resultModel = JsonUtils.parseObject(content, ResultModel.class);
                        responseMap.put("status", Integer.valueOf(resultModel.getCode()));
                        responseMap.put("message", resultModel.getMsg());
                    } else {
                        //返回了 被包装的 正常业务数据
                        log.error("远程调用禁用 resultModel 返回业务数据!");
                        responseMap.put("status", 500);
                        responseMap.put("message", "远程调用禁用 resultModel 返回业务数据!");
                    }
                    content = JsonUtils.toJsonString(responseMap);
                    return response.newBuilder()
                            .code(500)
                            .body(ResponseBody.create(mediaType, content))
                            .build();
                }
            }

            //生成新的response返回，网络请求的response如果取出之后，直接返回将会抛出异常
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    }
}
