package com.sinotech.system.provincePrice.controller;

import com.github.pagehelper.PageHelper;
import com.sinotech.common.aop.ControllerHelper;
import com.sinotech.common.consts.ErrorCodes;
import com.sinotech.common.model.ExportModel;
import com.sinotech.common.model.PageQuery;
import com.sinotech.common.model.ResultModel;
import com.sinotech.common.utils.PoiExcelUtils;
import com.sinotech.common.utils.StrUtils;
import com.sinotech.system.provincePrice.model.ProvinceStageScheme;
import com.sinotech.system.provincePrice.model.ProvinceStageSchemeVO;
import com.sinotech.system.provincePrice.service.ProvinceStageSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ProvinceStageSchemeController
 * @Description 省网全段运费方案
 * @Author szq
 * @Date 2022/3/16 9:35
 * @Version 1.0
 */
@RestController
public class ProvinceStageSchemeController {

    @Autowired
    private ProvinceStageSchemeService provinceStageSchemeService;

    /**
     * @Description: 新增
     * @param:[provinceStageScheme, jsonStr]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @PostMapping(value = "/provinceStageScheme/addProvinceStageScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "新增", required = {"provinceStageScheme.schemeName|方案名称",
            "provinceStageScheme.schemeNo|方案编号", "provinceStageScheme.companyId|所属省区id"
            , "provinceStageScheme.validTime|生效日期",
            "provinceStageScheme.invalidTime|失效日期", "jsonStr|重量区间"})
    public ResultModel addProvinceStageScheme(ProvinceStageScheme provinceStageScheme, String jsonStr) {
        provinceStageSchemeService.addProvinceStageScheme(provinceStageScheme, jsonStr);
        return ResultModel.success("新增成功！");
    }

    /**
     * @Description: 修改
     * @param:[provinceStageScheme, jsonStr]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @PostMapping(value = "/provinceStageScheme/editProvinceStageScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "修改", required = {"provinceStageScheme.schemeId|方案id", "provinceStageScheme.schemeName|方案名称",
            "provinceStageScheme.schemeNo|方案编号", "provinceStageScheme.companyId|所属省区id"
            , "provinceStageScheme.validTime|生效日期",
            "provinceStageScheme.invalidTime|失效日期", "jsonStr|重量区间"})
    public ResultModel editProvinceStageScheme(ProvinceStageScheme provinceStageScheme, String jsonStr) {
        provinceStageSchemeService.editProvinceStageScheme(provinceStageScheme, jsonStr);
        return ResultModel.success("修改成功！");
    }

    /**
     * @Description: 删除
     * @param:[schemeId]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @PostMapping(value = "/provinceStageScheme/delProvinceStageScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "删除", required = {"schemeId|方案id"})
    public ResultModel delProvinceStageScheme(String schemeId) {
        provinceStageSchemeService.deleteProvinceStageScheme(schemeId);
        return ResultModel.success("删除成功！");
    }

    /**
     * @Description: 查询单个方案
     * @param:[schemeId]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @PostMapping(value = "/provinceStageScheme/selectProvinceStageScheme", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "查询", required = {"schemeId|方案id"})
    public ResultModel selectProvinceStageScheme(String schemeId) {
        ProvinceStageSchemeVO provinceStageSchemeVO = provinceStageSchemeService.selectProvinceStageScheme(schemeId);
        return ResultModel.success("查询成功！", provinceStageSchemeVO);
    }

    /**
     * @Description: 查询列表
     * @param:[param, pageQuery]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @PostMapping(value = "/provinceStageScheme/selectProvinceStageSchemeList", produces = "application/json;charset=UTF-8")
    @ControllerHelper(description = "查询列表")
    public ResultModel selectProvinceStageSchemeList(ProvinceStageSchemeVO param, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery);
        List<ProvinceStageSchemeVO> list = provinceStageSchemeService.selectProvinceStageSchemeList(param);
        return ResultModel.pageSuccessWitchTranslate("查询成功！", list);
    }

    /**
     * @Description: 批量修改生效时间
     * @param:[schemeIds, validTime]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @RequestMapping(value = "/provinceStageScheme/updateProvinceStageSchemeValidTimeBySchemeIds", produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ControllerHelper(description = "批量修改生效时间")
    public ResultModel updateProvinceStageSchemeValidTimeBySchemeIds(String schemeIds, Date validTime) {
        provinceStageSchemeService.updateProvinceStageSchemeValidTimeBySchemeIds(schemeIds, validTime);
        return ResultModel.success("修改成功");
    }

    /**
     * @Description: 批量修改失效时间
     * @param:[schemeIds, invalidTime]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @RequestMapping(value = "/provinceStageScheme/updateProvinceStageSchemeInvalidTimeBySchemeIds", produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ControllerHelper(description = "批量修改失效时间")
    public ResultModel updateProvinceStageSchemeInvalidTimeBySchemeIds(String schemeIds, Date invalidTime) {
        provinceStageSchemeService.updateProvinceStageSchemeInvalidTimeBySchemeIds(schemeIds, invalidTime);
        return ResultModel.success("修改成功");
    }

    /**
     * @Description: 导出列表
     * @param:[param, exportModel]
     * @return: org.springframework.http.ResponseEntity
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @GetMapping(value = "/provinceStageScheme/exportSelectProvinceStageSchemeList")
    @ControllerHelper(description = "导出列表")
    public ResponseEntity exportSelectProvinceStageSchemeList(ProvinceStageSchemeVO param, ExportModel exportModel) {
        ResponseEntity responseEntity = provinceStageSchemeService.exportProvinceStageScheme(param);
        return responseEntity;
    }

    /**
     * @Description: 导入
     * @param:[file]
     * @return: com.sinotech.common.model.ResultModel
     * @Author: sizhenqiang
     * @date: 2022/3/18
     **/
    @PostMapping(value = "/provinceStageScheme/importProvinceStageScheme", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ControllerHelper(description = "导入")
    public ResultModel importProvinceStageScheme(MultipartFile file) {
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
        provinceStageSchemeService.importProvinceStageScheme(fileName, file);
        return ResultModel.success("导入成功" + fileName);
    }
}
