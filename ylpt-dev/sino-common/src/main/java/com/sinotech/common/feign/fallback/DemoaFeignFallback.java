package com.sinotech.common.feign.fallback;

import com.sinotech.common.feign.DemoaFeign;

/**
 * @author 郭训民
 * @description: TODO
 * @title: DemoaFeignFallback
 * @projectName ylpt-dev-ali-cloud
 * @date 2022/2/17 19:02
 */
public class DemoaFeignFallback implements DemoaFeign {
    @Override
    public String getUser(Integer userId) {
        return "DemoaFeignFallback.getUser";
    }
}
