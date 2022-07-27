package com.sinotech.system.basic.controller;

import com.github.pagehelper.PageInfo;
import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.common.model.ResultModel;
import com.sinotech.system.basic.model.DataColumnDtl;
import com.sinotech.system.basic.service.DataColumnDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据权限字段配置操作接口
 * @author ly
 * @title: DataColumnDefineCloudController
 * @projectName supply-chain
 * @description: TODO
 * @date 2022/3/310:40
 */
@RequestMapping(value = "/cloud")
@Controller
public class DataColumnDefineCloudController {

    @Autowired
    private DataColumnDefineService dataColumnDefineService;

    /**
     * 通过数据表CODE和模块CODE查询集合
     * @Date: 2018/7/7
     * @author: duxudong
     * @param tableCode 数据表名
     * @param moduleCode 模块CODE
     * @return 数据权限字段明细集合
     */
    @RequestMapping(value = "/dataColumnDefine/selectDataColumnDtlList" , produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @ControllerHelper(description = "通过数据表名查询集合")
    public List<DataColumnDtl> selectDataColumnDtlList(String tableCode, String moduleCode){
        return dataColumnDefineService.selectDataColumnDtlList(tableCode, moduleCode);
    }
    
    /**
     * 通过配置类型查询集合
     * @param fromType
     * @author ly
     * @date 2022/3/3 10:48
     * @return java.util.List<com.sinotech.system.basic.model.DataColumnDtl>
     */
    @RequestMapping(value = "/dataColumnDefine/selectDataColumnDtlListByFromType" , produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @ControllerHelper(description = "通过配置类型查询集合")
    public List<DataColumnDtl> selectDataColumnDtlListByFromType(String fromType){
        return dataColumnDefineService.selectDataColumnDtlListByFromType(fromType);
    }
}
