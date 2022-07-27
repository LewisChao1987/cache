package com.sinotech.system.basic.controller;

import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.common.model.ResultModel;
import com.sinotech.system.basic.model.Company;
import com.sinotech.system.basic.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 公司信息微服务接口调用
 * @author ly
 * @title: CompanyCloudController
 * @projectName supply-chain
 * @description: TODO
 * @date 2022/3/310:06
 */
@Controller
@RequestMapping(value = "/cloud")
public class CompanyCloudController {

    @Autowired
    private CompanyService companyService;

    /**
     * 根据ID查询公司
     * @param companyId
     * @author ly
     * @date 2022/3/3 10:21
     * @return com.sinotech.system.basic.model.Company
     */
    //@RequestMapping(value = "/company/selectCompanyListById",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    //@ResponseBody
    //@ControllerHelper(description="根据条件查询公司")
    //public Company selectCompanyById(String companyId){
    //    Company company = companyService.selectCompanyById(companyId);
    //    company.setPayPassword(null);
    //    return company;
    //}
    //
    ///**
    // * 根据AppId获取公司信息
    // * @author ly
    // * @date 2022/2/16 17:55
    // * @return com.sinotech.common.model.ResultModel
    // */
    //@RequestMapping(value = "/company/selectCompanyByAppId",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    //@ResponseBody
    //@ControllerHelper(description="查询所有公司")
    //public Company selectCompanyByAppId(String appId){
    //    return companyService.selectCompanyByAppId(appId);
    //}
}
