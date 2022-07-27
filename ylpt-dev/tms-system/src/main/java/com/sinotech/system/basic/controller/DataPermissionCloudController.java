package com.sinotech.system.basic.controller;

import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.common.model.ResultModel;
import com.sinotech.system.basic.model.DataPermission;
import com.sinotech.system.basic.service.DataPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ly
 * @title: DataPermissionCloudController
 * @projectName supply-chain
 * @description: TODO
 * @date 2022/3/310:51
 */

@RequestMapping(value = "/cloud")
@Controller
public class DataPermissionCloudController {

    @Autowired
    private DataPermissionService dataPermissionService;

    /**
     * 查询员工数据权限
     * @param empId
     * @return
     */
    @RequestMapping(value = "/dataPermission/selectDataPermissionByEmpId",produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @ControllerHelper(description="查询员工数据权限",required = {"empId|员工id"})
    public List<DataPermission> selectDataPermissionByEmpId(String empId){
        return dataPermissionService.selectDataPermissionByEmpId(empId);
    }
}
