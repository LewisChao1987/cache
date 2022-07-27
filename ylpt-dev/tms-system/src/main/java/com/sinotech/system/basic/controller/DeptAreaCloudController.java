package com.sinotech.system.basic.controller;

import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.common.consts.TableName;
import com.sinotech.common.helper.DataPermissionHelper;
import com.sinotech.common.model.ResultModel;
import com.sinotech.system.basic.model.DeptArea;
import com.sinotech.system.basic.service.DeptAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ly
 * @title: DeptAreaCloudController
 * @projectName supply-chain
 * @description: TODO
 * @date 2022/3/311:25
 */
@RequestMapping(value = "/cloud")
@Controller
public class DeptAreaCloudController {

    @Autowired
    private DeptAreaService deptAreaService;

    /**
     * 查询所有网点区域信息
     * @Author liuyiming
     * @Date 2018/7/11
     * @param:
     * @return com.sinotech.common.model.ResultModel
     */
    @RequestMapping(value = "/deptArea/selectAllDeptArea", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    @ControllerHelper(description="查询所有网点区域信息")
    public List<DeptArea> selectAllDeptArea(){
        return deptAreaService.selectAllDeptArea();
    }
}
