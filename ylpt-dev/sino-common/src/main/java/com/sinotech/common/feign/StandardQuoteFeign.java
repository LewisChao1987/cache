package com.sinotech.common.feign;

import com.sinotech.system.basic.model.StandardFreightDTO;
import com.sinotech.system.price.model.StandardQuoteResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author ly
 * @title: StandardQuoteFeign
 * @projectName supply-chain
 * @description: TODO
 * @date 2022/2/1815:54
 */
@FeignClient(value = "${serverName.tms-quote}")
public interface StandardQuoteFeign {
    
    /**
     * 调用微服务标准运费接口
     * @param orderNo
     * @param billDeptId
     * @param discDeptId
     * @param destDeptId
     * @param itemJson
     * @param productType
     * @param serviceLevel
     * @param businessType
     * @param truckType
     * @param destinations
     * @param forDelivery
     * @param discProvince
     * @param discCity
     * @param discDistrict
     * @param discTown
     * @param isTruckTypeHand
     * @param orderType
     * @param isThrowError
     * @param freightDTO
     * @param businessAttributes
     * @param isNewOrder
     * @param orderRoute
     * @param billCompanyId
     * @param isSpecial
     * @param actualDeliveryDistance
     * @param contractNo
     * @param consigneeMobile
     * @param modeOfDelivery
     * @author ly
     * @date 2022/2/21 9:26
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    @PostMapping("/standardQuote/standardFreight")
    StandardQuoteResultInfo standardFreight(@RequestParam("orderNo") String orderNo,
                                            @RequestParam("billDeptId") String billDeptId,
                                            @RequestParam("discDeptId") String discDeptId,
                                            @RequestParam("destDeptId") String destDeptId,
                                            @RequestParam("itemJson") String itemJson,
                                            @RequestParam("productType") String productType,
                                            @RequestParam("serviceLevel") String serviceLevel,
                                            @RequestParam("businessType") String businessType,
                                            @RequestParam("truckType") String truckType,
                                            @RequestParam("destinations") String destinations,
                                            @RequestParam("forDelivery") String forDelivery,
                                            @RequestParam("discProvince") String discProvince,
                                            @RequestParam("discCity") String discCity,
                                            @RequestParam("discDistrict") String discDistrict,
                                            @RequestParam("discTown") String discTown,
                                            @RequestParam("isTruckTypeHand") String isTruckTypeHand,
                                            @RequestParam("orderType") String orderType,
                                            @RequestParam("isThrowError") boolean isThrowError,
                                            @RequestParam("businessAttributes") String businessAttributes,
                                            @RequestParam("isNewOrder") String isNewOrder,
                                            @RequestParam("orderRoute") String orderRoute,
                                            @RequestParam("billCompanyId") String billCompanyId,
                                            @RequestParam("isSpecial") String isSpecial,
                                            @RequestParam("actualDeliveryDistance") Double actualDeliveryDistance,
                                            @RequestParam("contractNo") String contractNo,
                                            @RequestParam("consigneeMobile") String consigneeMobile,
                                            @RequestParam("modeOfDelivery") String modeOfDelivery,
                                            @RequestParam("employee") String employee,
                                            @RequestBody StandardFreightDTO freightDTo);

}
