package com.sinotech.system.basic.controller;

import com.alibaba.fastjson.JSONArray;
import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.system.basic.model.Department;
import com.sinotech.system.basic.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author ly
 * @title: DepartmentCloudController
 * @projectName supply-chain
 * @description: TODO
 * @date 2022/3/310:55
 */

@RequestMapping(value = "/cloud")
@Controller
public class DepartmentCloudController {

    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 查询当前部门信息
     * @param deptId
     * @author ly
     * @date 2022/3/3 10:56
     * @return com.sinotech.common.model.ResultModel
     */
    @PostMapping(value = "/department/selectDepartmentInfoById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description="查询当前部门信息")
    public Department selectDepartmentInfoById(String deptId){
        return departmentService.selectDepartmentById(deptId);
    }


    /**
     * 根据部门ID查询部门集合
     * @author wenbin.song
     * @creatTime 2018/5/10
     * @param deptId 部门ID
     * @return listdepartment 部门Lsit集合信息
     */
    @PostMapping(value = "/department/selectListDepartmentById", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description="按照ID查询分页显示部门",required = {"deptId|部门ID","pageQuery|分页查询"})
    public List<Department> selectListDepartmentById(String deptId){
        return departmentService.selectlListDepartmentById(deptId);
    }

    /**
     * 根据传入参数 获取距离
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @param scale
     * @param coordinatesType
     * @author ly
     * @date 2022/2/17 16:03
     * @return java.lang.Double
     */
    @PostMapping(value = "/department/getDistance", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description = "根据传入参数获取距离")
    public Double getDistance(double lat1, double lng1, double lat2, double lng2, int scale, Boolean coordinatesType){
        return departmentService.getDistance(lat1,lng1,lat2,lng2,scale,coordinatesType);
    }

    /**
     * 调用百度高德api结算距离
     * @param origins
     * @param destinations
     * @param coordinatesType
     * @param deptLocationList
     * @author ly
     * @date 2022/2/17 16:06
     * @return com.alibaba.fastjson.JSONArray
     */
    @PostMapping(value = "/department/routematrix", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description = "调用百度高德api结算距离")
    public JSONArray routematrix(String origins, String destinations, Boolean coordinatesType, @RequestBody List<Department> deptLocationList){
        return departmentService.routematrix(origins,destinations,coordinatesType,deptLocationList);
    }


    /**
     * 获取地图切换系统参数
     * @author ly
     * @date 2022/2/17 16:13
     * @return java.lang.Boolean
     */
    @PostMapping(value = "/department/getMapSwitchingParameters", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description = "调用百度高德api结算距离")
    public Boolean getMapSwitchingParameters(){
        return departmentService.getMapSwitchingParameters();
    }



    /**
     * 根据权限级别查询部门信息
     * @param organizeRefIds
     * @param condition
     * @author ly
     * @date 2022/2/17 16:15
     * @return java.util.List<com.sinotech.system.basic.model.Department>
     */
    @PostMapping(value = "/department/selectDepartmentByDpartmentConditions", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description = "调用百度高德api结算距离")
    public List<Department> selectDepartmentByDpartmentConditions(@RequestBody List<String> organizeRefIds, String condition){
        return departmentService.selectDepartmentByDpartmentConditions(organizeRefIds,condition);
    }

    /**
     * 查询出区域下所有的部门
     * @param areaIdList
     * @author ly
     * @date 2022/2/17 16:16
     * @return java.util.List<com.sinotech.system.basic.model.Department>
     */
    @PostMapping(value = "/department/selectDepartmentByArea", produces = "application/json;charset=UTF-8")
    @ResponseBody
    @ControllerHelper(description = "调用百度高德api结算距离")
    public List<Department> selectDepartmentByArea(@RequestBody List<String> areaIdList){
        return departmentService.selectDepartmentByArea(areaIdList);
    }


}
