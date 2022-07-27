package com.sinotech.system.price.controller;

import com.github.pagehelper.PageHelper;
import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.common.consts.ErrorCodes;
import com.sinotech.common.model.ExportModel;
import com.sinotech.common.model.PageQuery;
import com.sinotech.common.model.ResultModel;
import com.sinotech.common.utils.PoiExcelUtils;
import com.sinotech.common.utils.StrUtils;
import com.sinotech.system.price.model.BunkerScheme;
import com.sinotech.system.price.model.BunkerSchemeVO;
import com.sinotech.system.price.service.BunkerSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @ClassName BunkerSchemeController
 * @Description 燃油附加费
 * @Author szq
 * @Date 2022/3/18 9:37
 * @Version 1.0
 */
@RestController
public class BunkerSchemeController {
    @Autowired
    private BunkerSchemeService bunkerSchemeService;
    
    /**
     *@Description: 新增
     *@param:[bunkerScheme, jsonStr]
     *@return: com.sinotech.common.model.ResultModel
     *@Author: sizhenqiang
     *@date: 2022/3/18
     **/
    @PostMapping(value = "/bunkerScheme/addBunkerScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "新增", required = {"bunkerScheme.schemeName|方案名称",
            "bunkerScheme.schemeNo|方案编号", "bunkerScheme.companyId|公司id",
            "bunkerScheme.schemeMode|国网类型","bunkerScheme.relationDeptId|所属分拨id","bunkerScheme.relationDeptName|所属分拨名称",
            "jsonStr|重量区间"})
    public ResultModel addBunkerScheme(BunkerScheme bunkerScheme, String jsonStr) {
        bunkerSchemeService.addBunkerScheme(bunkerScheme, jsonStr);
        return ResultModel.success("新增成功！");
    }

   /**
    *@Description: 修改
    *@param:[BunkerScheme, jsonStr]
    *@return: com.sinotech.common.model.ResultModel
    *@Author: sizhenqiang
    *@date: 2022/3/18
    **/
    @PostMapping(value = "/bunkerScheme/editBunkerScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "修改", required = {"bunkerScheme.schemeId|方案id",
            "bunkerScheme.schemeName|方案名称",
            "bunkerScheme.schemeNo|方案编号", "bunkerScheme.companyId|所属省区id",
            "bunkerScheme.schemeMode|国网类型","bunkerScheme.relationDeptId|所属分拨","bunkerScheme.relationDeptName|所属分拨名称",
            "jsonStr|重量区间"})
    public ResultModel editBunkerScheme(BunkerScheme bunkerScheme, String jsonStr) {
        bunkerSchemeService.editBunkerScheme(bunkerScheme, jsonStr);
        return ResultModel.success("修改成功！");
    }

    /**
     *@Description: 删除
     *@param:[schemeId]
     *@return: com.sinotech.common.model.ResultModel
     *@Author: sizhenqiang
     *@date: 2022/3/18
     **/
    @PostMapping(value = "/bunkerScheme/delBunkerScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "删除", required = {"schemeId|方案id"})
    public ResultModel delBunkerScheme(String schemeId) {
        bunkerSchemeService.deleteBunkerScheme(schemeId);
        return ResultModel.success("删除成功！");
    }

    /**
     *@Description: 查询单个方案
     *@param:[schemeId]
     *@return: com.sinotech.common.model.ResultModel
     *@Author: sizhenqiang
     *@date: 2022/3/18
     **/
    @PostMapping(value = "/bunkerScheme/selectBunkerScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "查询", required = {"schemeId|方案id"})
    public ResultModel selectBunkerScheme(String schemeId) {
        BunkerSchemeVO BunkerScheme = bunkerSchemeService.selectBunkerScheme(schemeId);
        return ResultModel.success("查询成功！", BunkerScheme);
    }

    /**
     * 查询列表
     * @Author liuyongli
     * @Date 2020/12/9
     * @param: param
     * @param: pageQuery
     */
    @PostMapping(value = "/bunkerScheme/selectBunkerSchemeList", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "查询列表")
    public ResultModel selectBunkerSchemeList(BunkerSchemeVO param, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery);
        List<BunkerSchemeVO> centerPublishHdrList = bunkerSchemeService.selectBunkerSchemeList(param);
        return ResultModel.pageSuccessWitchTranslate("查询成功！", centerPublishHdrList);
    }

    /**
     *@Description: 批量修改生效时间
     *@param:[schemeIds, validTime]
     *@return: com.sinotech.common.model.ResultModel
     *@Author: sizhenqiang
     *@date: 2022/3/18
     **/
    @RequestMapping(value = "/bunkerScheme/updateBunkerSchemeValidTimeBySchemeIds", produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ControllerHelper(description = "批量修改生效时间")
    public ResultModel updateBunkerSchemeValidTimeBySchemeIds(String schemeIds, Date validTime) {
        bunkerSchemeService.updateBunkerSchemeValidTimeBySchemeIds(schemeIds,validTime);
        return ResultModel.success("修改成功");
    }

    /**
     *@Description: 批量修改失效时间
     *@param:[schemeIds, invalidTime]
     *@return: com.sinotech.common.model.ResultModel
     *@Author: sizhenqiang
     *@date: 2022/3/18
     **/
    @RequestMapping(value = "/bunkerScheme/updateBunkerSchemeInvalidTimeBySchemeIds", produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ControllerHelper(description = "批量修改失效时间")
    public ResultModel updateBunkerSchemeInvalidTimeBySchemeIds(String schemeIds, Date invalidTime) {
        bunkerSchemeService.updateBunkerSchemeInvalidTimeBySchemeIds(schemeIds,invalidTime);
        return ResultModel.success("修改成功");
    }

    /**
     *@Description: 导出列表
     *@param:[param, exportModel]
     *@return: org.springframework.http.ResponseEntity
     *@Author: sizhenqiang
     *@date: 2022/3/18
     **/
    @GetMapping(value = "/bunkerScheme/exportSelectBunkerSchemeList")
    @ControllerHelper(description = "导出列表")
    public ResponseEntity exportSelectBunkerSchemeList(BunkerSchemeVO param, ExportModel exportModel) {
        ResponseEntity responseEntity = bunkerSchemeService.exportBunkerScheme(param);
        return responseEntity;
    }

    /**
     * 导入
     * @Author liuyongli
     * @Date 2020/12/16
     * @param: file
     */
    @PostMapping(value = "/bunkerScheme/importBunkerScheme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ControllerHelper(description = "导入")
    public ResultModel importBunkerScheme(MultipartFile file) {
        //判断文件是否为空
        if (StrUtils.isNull(file)) {
            return ResultModel.fail("文件不能为空", ErrorCodes.FILE_FORMAT_ERROR);
        }

        //获取文件名
        String fileName = file.getOriginalFilename();
        //验证文件是否合格
        if (!PoiExcelUtils.validateExcel(fileName)) {
            return ResultModel.fail("文件必须是Excel文件", ErrorCodes.FILE_FORMAT_ERROR);
        }

        //判断文件内容是否为空
        long size = file.getSize();
        if (StrUtils.isEmpty(fileName) || size == 0) {
            return ResultModel.fail("文件内容不能为空", ErrorCodes.FILE_FORMAT_ERROR);
        }
        bunkerSchemeService.importBunkerScheme(fileName, file);
        return ResultModel.success("导入成功" + fileName);
    }
}
