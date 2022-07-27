package com.sinotech.common.feign;

import com.sinotech.common.feign.fallback.DemoaFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 郭训民
 * @description: TODO
 * @title: DemoaFeign
 * @projectName ylpt-dev-ali-cloud
 * @date 2022/2/17 18:59
 */
@FeignClient(value = "demoa-service", fallback = DemoaFeignFallback.class)
public interface DemoaFeign {

    @GetMapping("/user/{id}")
    String getUser(@PathVariable("id") Integer userId);
}
