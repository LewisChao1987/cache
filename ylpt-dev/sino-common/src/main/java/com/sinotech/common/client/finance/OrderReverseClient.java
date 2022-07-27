package com.sinotech.common.client.finance;

import java.util.List;

/**
 * @Author: xubing
 * @Date: 2022/3/16
 */
public interface OrderReverseClient {

    int selectAlreadyAppliedOrderReverseByOrderIds(List<String> orderIdList);
}
