package com.sinotech.system.price.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.sinotech.common.client.system.CompanyClient;
import com.sinotech.common.client.system.DepartmentClient;
import com.sinotech.common.consts.DataConstants;
import com.sinotech.common.consts.Dicts;
import com.sinotech.common.exceptions.BusinessProhibitionException;
import com.sinotech.common.exceptions.IllegalParametersException;
import com.sinotech.common.utils.*;
import com.sinotech.system.basic.model.*;
import com.sinotech.system.basic.service.ImportExcelService;
import com.sinotech.system.basic.service.ItemTypeService;
import com.sinotech.system.price.mapper.BunkerSchemeDtlMapper;
import com.sinotech.system.price.mapper.BunkerSchemeMapper;
import com.sinotech.system.price.model.BunkerScheme;
import com.sinotech.system.price.model.BunkerSchemeDtl;
import com.sinotech.system.price.model.BunkerSchemeVO;
import com.sinotech.system.price.service.BunkerSchemeService;
import com.sinotech.system.provinceBasic.mapper.ProvinceAreaDeptHdrMgrMapper;
import com.sinotech.system.provinceBasic.model.ProvinceAreaDeptHdr;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName BunkerSchemeServiceImpl
 * @Description 燃油附加费
 * @Author szq
 * @Date 2022/3/17 15:03
 * @Version 1.0
 */
@Service
public class BunkerSchemeServiceImpl implements BunkerSchemeService {
    @Autowired
    private BunkerSchemeMapper bunkerSchemeMapper;
    @Autowired
    private BunkerSchemeDtlMapper bunkerSchemeDtlMapper;
    @Autowired
    private CompanyClient companyClient;
    @Autowired
    private DepartmentClient departmentClient;
    @Autowired
    private ImportExcelService importExcelService;
    @Autowired
    private ProvinceAreaDeptHdrMgrMapper provinceAreaDeptHdrMgrMapper;
    @Autowired
    private ItemTypeService itemTypeService;

    /**
     * @param bunkerScheme
     * @param jsonStr
     * @Description: 新增燃油附加费方案
     * @param: BunkerScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBunkerScheme(BunkerScheme bunkerScheme, String jsonStr) {
        Employee employee = SessionUtils.getCurrentUser();
        checkSchemeNoAndNameRule(bunkerScheme);
        getValidStatus(bunkerScheme.getValidTime(), bunkerScheme.getInvalidTime());

        if (StrUtils.isNull(bunkerScheme.getCompanyId())) {
            throw new IllegalParametersException("公司不能为空！");
        }
        Company company = companyClient.selectCompanyById(bunkerScheme.getCompanyId());
        if (StrUtils.isNull(company)) {
            throw new IllegalParametersException("公司未维护！");
        }
        if (!DataConstants.CONSTANT_STR_ONE.equals(company.getCompanyStatus())) {
            throw new IllegalParametersException(String.format("公司【%s】未启用 操作失败！", company.getCompanyName()));
        }
        List<BunkerScheme> checkSchemeNoList = bunkerSchemeMapper.selectListForUnique(bunkerScheme.getSchemeNo(), null);
        if (StrUtils.isNotNullList(checkSchemeNoList)) {
            throw new IllegalParametersException(String.format("方案编号【%s】已存在！", bunkerScheme.getSchemeNo()));
        }
        List<BunkerScheme> checkSchemeNameList = bunkerSchemeMapper.selectListForUnique(null, bunkerScheme.getSchemeName());
        if (StrUtils.isNotNullList(checkSchemeNameList)) {
            throw new IllegalParametersException(String.format("方案名称【%s】已存在！", bunkerScheme.getSchemeName()));
        }
        if (DateUtil.compareDateHm(new Date(), bunkerScheme.getInvalidTime())) {
            throw new IllegalParametersException("失效时间的修改必须大于当前时间！");
        }
        List<BunkerScheme> checkList = bunkerSchemeMapper.selectListForCheck(bunkerScheme.getCompanyId(), bunkerScheme.getSchemeMode(), bunkerScheme.getRelationDeptId());
        if (StrUtils.isNotNullList(checkList)) {
            checkList.forEach(item -> {
                if (checkValidTime(bunkerScheme, item)) {
                    throw new IllegalParametersException(String.format("与方案编号【%s】的有效期重合！", item.getSchemeNo()));
                }
            });
        }

        bunkerScheme.setSchemeId(StrUtils.getUUID());
        bunkerScheme.setTenantId(company.getTenantId());
        bunkerScheme.setCompanyName(company.getCompanyName());
        bunkerScheme.setInsUser(employee.getEmpName());
        bunkerScheme.setInsTime(DateUtil.getNowDateTime());

        if (StrUtils.isNotNull(jsonStr)) {
            List<BunkerSchemeDtl> bunkerSchemeDtls = JsonUtils.toList(jsonStr, BunkerSchemeDtl.class);
            // 检查区间是否重合
            checkCoincideSection(null, bunkerSchemeDtls);

            bunkerSchemeDtls.forEach(item -> {
                item.setSchemeDtlId(StrUtils.getUUID());
                item.setTenantId(employee.getTenantId());
                item.setCompanyId(employee.getCompanyId());
                item.setSchemeId(bunkerScheme.getSchemeId());
                item.setInsUser(employee.getEmpName());
                item.setInsTime(DateUtil.getNowDateTime());
            });
            bunkerSchemeDtlMapper.batchInsertSelective(bunkerSchemeDtls);
        }
        bunkerSchemeMapper.insertSelective(bunkerScheme);
    }

    /**
     * @Description:修改燃油附加费方案
     * @param:BunkerScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editBunkerScheme(BunkerScheme bunkerScheme, String jsonStr) {
        Employee employee = SessionUtils.getCurrentUser();
        checkSchemeNoAndNameRule(bunkerScheme);
        getValidStatus(bunkerScheme.getValidTime(), bunkerScheme.getInvalidTime());

        if (StrUtils.isNull(bunkerScheme.getCompanyId())) {
            throw new IllegalParametersException("报价省区不能为空！");
        }
        Company company = companyClient.selectCompanyById(bunkerScheme.getCompanyId());
        if (StrUtils.isNull(company)) {
            throw new IllegalParametersException("省区未维护！");
        }
        if (!DataConstants.CONSTANT_STR_ONE.equals(company.getCompanyStatus())) {
            throw new IllegalParametersException(String.format("省区【%s】未启用 操作失败！", company.getCompanyName()));
        }
        BunkerScheme bunkerSchemeOld = bunkerSchemeMapper.selectByPrimaryKey(bunkerScheme.getSchemeId());
        if (bunkerSchemeOld == null) {
            throw new IllegalParametersException("未查询到方案信息！");
        }
        if (bunkerScheme.getValidTime().getTime() != bunkerSchemeOld.getValidTime().getTime()) {
            throw new BusinessProhibitionException("生效日期禁止编辑");
        }
        List<BunkerScheme> checkList = bunkerSchemeMapper.selectListForCheck(bunkerScheme.getSchemeNo(), bunkerScheme.getCompanyId(), null);
        if (StrUtils.isNotNullList(checkList) && checkList.stream().anyMatch(item -> !bunkerScheme.getSchemeId().equals(item.getSchemeId()))) {
            throw new IllegalParametersException(String.format("方案编号【%s】已存在！", bunkerScheme.getSchemeNo()));
        }
        if (DateUtil.compareDateHm(new Date(), bunkerScheme.getInvalidTime())) {
            throw new IllegalParametersException("失效时间的修改必须大于当前时间！");
        }
        checkList = bunkerSchemeMapper.selectListForCheck(null, bunkerScheme.getCompanyId(), bunkerScheme.getProductType());
        if (StrUtils.isNotNullList(checkList) && checkList.stream().anyMatch(item -> !bunkerScheme.getSchemeId().equals(item.getSchemeId()))) {
            checkList.stream().filter(item -> !bunkerScheme.getSchemeId().equals(item.getSchemeId())).forEach(item -> {
                if (checkValidTime(bunkerScheme, item)) {
                    throw new IllegalParametersException(String.format("与方案编号【%s】的有效期重合！", item.getSchemeNo()));
                }
            });
        }

        bunkerScheme.setUpdUser(employee.getEmpName());
        bunkerScheme.setUpdTime(DateUtil.getNowDateTime());

        if (StrUtils.isNotNull(jsonStr)) {
            List<BunkerSchemeDtl> bunkerSchemeDtlList = JsonUtils.toList(jsonStr, BunkerSchemeDtl.class);
            // 检查区间是否重合
            checkCoincideSection(null, bunkerSchemeDtlList);

            bunkerSchemeDtlList.forEach(item -> {
                item.setSchemeDtlId(StrUtils.getUUID());
                item.setTenantId(employee.getTenantId());
                item.setCompanyId(employee.getCompanyId());
                item.setSchemeId(bunkerScheme.getSchemeId());
                item.setInsUser(employee.getEmpName());
                item.setInsTime(DateUtil.getNowDateTime());
            });
            bunkerSchemeDtlMapper.deleteBySchemeIdList(Lists.newArrayList(bunkerScheme.getSchemeId()));
            bunkerSchemeDtlMapper.batchInsertSelective(bunkerSchemeDtlList);
        }
        bunkerSchemeMapper.updateByPrimaryKeySelective(bunkerScheme);
    }

    /**
     * @param schemeId
     * @Description: 删除燃油附加费方案
     * @param:schemeId
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBunkerScheme(String schemeId) {
        List<String> schemeIdList = Arrays.stream(schemeId.split(",")).filter(StrUtils::isNotNull).collect(Collectors.toList());
        if (StrUtils.isNullList(schemeIdList)) {
            throw new IllegalParametersException("方案id不能为空！");
        }

        List<String> deleteSchemeIdList = Lists.newArrayList();
        List<BunkerScheme> voList = bunkerSchemeMapper.selectBunkerSchemeByIds(schemeIdList);
        for (BunkerScheme vo : voList) {
            if (!getValidStatus(vo.getValidTime(), vo.getInvalidTime()).equals(Dicts.ValidStatus.VALID_STATUS_19803)) {
                throw new IllegalParametersException("请选择已失效的数据信息进行删除！");
            }
            deleteSchemeIdList.add(vo.getSchemeId());
        }
        if (CollUtil.isNotEmpty(deleteSchemeIdList)) {
            bunkerSchemeMapper.deleteByIds(deleteSchemeIdList);
        }
    }

    /**
     * @param schemeId
     * @Description:查询单个方案
     * @param:schemeId
     * @return: BunkerScheme
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    public BunkerSchemeVO selectBunkerScheme(String schemeId) {
        BunkerScheme bunkerScheme = bunkerSchemeMapper.selectByPrimaryKey(schemeId);
        BunkerSchemeVO bunkerSchemeVO = new BunkerSchemeVO();
        if (StrUtils.isNotNull(bunkerScheme)) {
            BeanUtils.copyProperties(bunkerScheme, bunkerSchemeVO);
            List<BunkerSchemeDtl> bunkerSchemeDtlList = bunkerSchemeDtlMapper.selectBunkerSchemeDtlBySchemeId(schemeId);
            bunkerSchemeVO.setBunkerSchemeDtlList(bunkerSchemeDtlList);
        }
        return bunkerSchemeVO;
    }

    /**
     * @param param
     * @Description: 查询列表
     * @param: param
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    public List<BunkerSchemeVO> selectBunkerSchemeList(BunkerSchemeVO param) {
        Employee employee = SessionUtils.getCurrentUser();
        if (!DataConstants.CENTER_COMPANY_ID.equals(employee.getCompanyId())) {
            if (StrUtils.isNull(param.getCompanyId())) {
                param.setCompanyId(employee.getCompanyId());
            } else if (!employee.getCompanyId().equals(param.getCompanyId())) {
                return new ArrayList<>();
            }
        }
        List<BunkerScheme> list = bunkerSchemeMapper.selectBunkerSchemeList(param);
        List<BunkerSchemeVO> result = new ArrayList<>(list.size());
        if (CollUtil.isNotEmpty(list)) {
            for (BunkerScheme bunkerScheme : list) {
                BunkerSchemeVO bunkerSchemeVO = new BunkerSchemeVO();
                BeanUtils.copyProperties(bunkerScheme, bunkerSchemeVO);
                bunkerSchemeVO.setSchemeStatus(getSchemeStatus(bunkerSchemeVO.getValidTime(), bunkerSchemeVO.getInvalidTime(), bunkerSchemeVO.getDeleteStatus()));
                result.add(bunkerSchemeVO);
            }
        }
        return result;
    }

    /**
     * @param param
     * @Description: 导出列表
     * @param:param
     * @return: ResponseEntity
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    public ResponseEntity exportBunkerScheme(BunkerSchemeVO param) {
        List<BunkerSchemeVO> bunkerSchemeList = selectBunkerSchemeList(param);
        if (StrUtils.isNotNullList(bunkerSchemeList)) {
            List<String> schemeIdList = bunkerSchemeList.stream().map(BunkerSchemeVO::getSchemeId).collect(Collectors.toList());
            List<BunkerSchemeDtl> bunkerSchemeDtlList = bunkerSchemeDtlMapper.selectBunkerSchemeDtlBySchemeIds(schemeIdList);
            Map<String, List<BunkerSchemeDtl>> listMap = bunkerSchemeDtlList.stream().collect(Collectors.groupingBy(item -> item.getSchemeId()));

            List<Map<String, Object>> mapList1 = new ArrayList<>();
            List<Map<String, Object>> mapList2 = new ArrayList<>();
            List<Map<String, Object>> mapList3 = new ArrayList<>();
            List<Map<String, Object>> mapList4 = new ArrayList<>();

            bunkerSchemeList.stream().forEach(item -> {
                Map<String, Object> map = ObjectUtils.toMap(item);
                map.put("validTime", DateUtil.getDateTimeFormat(item.getValidTime()));
                map.put("invalidTime", DateUtil.getDateTimeFormat(item.getInvalidTime()));

                List<BunkerSchemeDtl> bunkerSchemeDtls = listMap.get(item.getSchemeId());
                Map<String, List<BunkerSchemeDtl>> bunkerSchemeDtlListMap = bunkerSchemeDtls.stream().collect(Collectors.groupingBy(BunkerSchemeDtl::getDataType));
                for (Map.Entry<String, List<BunkerSchemeDtl>> entry : bunkerSchemeDtlListMap.entrySet()) {
                    List<BunkerSchemeDtl> schemeDtls = entry.getValue();
                    schemeDtls = schemeDtls.stream().sorted(Comparator.comparing(BunkerSchemeDtl::getKmLowerLimit)).collect(Collectors.toList());
                    List<Map<String, Object>> dtlMapList = schemeDtls.stream().map(dtl -> {
                        Map<String, Object> dtlMap = ObjectUtils.toMap(dtl);
                        dtlMap.put("schemeId", item.getSchemeNo());
                        dtlMap.put("schemeName", item.getSchemeName());
                        return dtlMap;
                    }).collect(Collectors.toList());
                    if (Dicts.FreightDtlComputeType.KGS.equals(entry.getKey())) {
                        mapList2.addAll(dtlMapList);
                    } else if (Dicts.FreightDtlComputeType.CBM.equals(entry.getKey())) {
                        mapList3.addAll(dtlMapList);
                    } else if (Dicts.FreightDtlComputeType.QTY.equals(entry.getKey())) {
                        mapList4.addAll(dtlMapList);
                    }
                }
                mapList1.add(map);
            });

            List<List<Map<String, Object>>> mapList = new ArrayList<>();
            mapList.add(DictionaryUtils.dictionaryCodeExport(mapList1));
            mapList.add(DictionaryUtils.dictionaryCodeExport(mapList2));
            mapList.add(DictionaryUtils.dictionaryCodeExport(mapList3));
            mapList.add(DictionaryUtils.dictionaryCodeExport(mapList4));
            List<String> headList = Arrays.asList("provinceStageScheme", "provinceStageSchemeDtl", "provinceStageSchemeDtl", "provinceStageSchemeDtl");
            List<String> titleList = Arrays.asList("燃油附加费方案", "重量区间", "体积区间", "件数区间");
            try {
                return PoiExcelUtils.generateExcel(mapList, headList, titleList, "燃油附加费");
            } catch (Exception e) {
                throw new IllegalParametersException("导出异常！");
            }
        }
        return null;
    }

    /**
     * @param fileName
     * @param file
     * @Description: 导入
     * @param:fileName file
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importBunkerScheme(String fileName, MultipartFile file) {
        Employee employee = SessionUtils.getCurrentUser();
        List<ImportExcel> importExcelList = importExcelService.selectImportExcelByModelId("bunkerScheme");
        //excel 从第二行读取的 第三行才是数据
        List<HashMap<String, String>> bunkerSchemeMapList = PoiExcelUtils.analysisExcel(fileName, file, importExcelList, 2, 0);
        if (StrUtils.isNullList(bunkerSchemeMapList)) {
            throw new IllegalParametersException("方案至少插入一条数据！");
        }

        List<HashMap<String, String>> importExcelDtlMapList = new ArrayList<>();
        //重量
        List<ImportExcel> importExcelDtlList = importExcelService.selectImportExcelByModelId("bunkerSchemeDtl");
        List<HashMap<String, String>> importExcelDtlMapList4W = PoiExcelUtils.analysisExcel(fileName, file, importExcelDtlList, 2, 1);
        if (StrUtils.isNotNullList(importExcelDtlMapList4W)) {
            importExcelDtlMapList4W.forEach(item -> item.put("dataType", Dicts.FreightDtlComputeType.KGS));
            importExcelDtlMapList.addAll(importExcelDtlMapList4W);
        }
        //体积
        List<HashMap<String, String>> importExcelDtlMapList4T = PoiExcelUtils.analysisExcel(fileName, file, importExcelDtlList, 2, 2);
        if (StrUtils.isNotNullList(importExcelDtlMapList4T)) {
            importExcelDtlMapList4T.forEach(item -> item.put("dataType", Dicts.FreightDtlComputeType.CBM));
            importExcelDtlMapList.addAll(importExcelDtlMapList4T);
        }
        //件数
        List<HashMap<String, String>> importExcelDtlMapList4J = PoiExcelUtils.analysisExcel(fileName, file, importExcelDtlList, 2, 3);
        if (StrUtils.isNotNullList(importExcelDtlMapList4J)) {
            importExcelDtlMapList4J.forEach(item -> item.put("dataType", Dicts.FreightDtlComputeType.QTY));
            importExcelDtlMapList.addAll(importExcelDtlMapList4J);
        }

        List<Company> companyList = companyClient.selectAllCompany();
        Map<String, Company> companyMap = companyList.stream().collect(Collectors.toMap(Company::getCompanyName, Function.identity()));

        List<DepartmentVo> departmentList = departmentClient.selectAllDepartmentVo();
        Map<String, DepartmentVo> departmentMap = departmentList.stream().collect(Collectors.toMap(DepartmentVo::getDeptNo, Function.identity()));
        departmentMap.putAll(departmentList.stream().collect(Collectors.toMap(DepartmentVo::getDeptName, Function.identity())));

        List<ProvinceAreaDeptHdr> provinceAreaDeptHdrList = provinceAreaDeptHdrMgrMapper.selectProvinceAreaDeptByDeptAreaNameAndDeptChargeType(Dicts.DeptChargeType.DEPT_CHARGE_TYPE_59116, null);
        Map<String, ProvinceAreaDeptHdr> provinceAreaDeptHdrMap = provinceAreaDeptHdrList.stream().collect(Collectors.toMap(ProvinceAreaDeptHdr::getDeptAreaNo, Function.identity()));
        provinceAreaDeptHdrMap.putAll(provinceAreaDeptHdrList.stream().collect(Collectors.toMap(ProvinceAreaDeptHdr::getDeptAreaName, Function.identity())));
        int len = 3;//行数
        List<BunkerScheme> bunkerSchemeList = new ArrayList<>();
        for (HashMap item : bunkerSchemeMapList) {
            {
                List<String> strList = new ArrayList<>();
                BunkerScheme bunkerScheme = JsonUtils.parseObjectByFastJson(JsonUtils.toJSONStringByFastJson(item), BunkerScheme.class);
                if (StrUtils.isNull(bunkerScheme.getSchemeNo())) {
                    strList.add("方案编号");
                }
                if (StrUtils.isNull(bunkerScheme.getSchemeName())) {
                    strList.add("方案名称");
                }
                if (StrUtils.isNull(bunkerScheme.getRelationDeptName())) {
                    strList.add("所属分拨名称");
                }
                if (StrUtils.isNull(bunkerScheme.getValidTime())) {
                    strList.add("生效日期");
                }
                if (StrUtils.isNull(bunkerScheme.getInvalidTime())) {
                    strList.add("失效日期");
                }
                if (StrUtils.isNotNullList(strList)) {
                    throw new IllegalParametersException(String.format("第%s行，【%s】不得为空", len, String.join("、", strList)));
                }

                checkSchemeNoAndNameRule(bunkerScheme);
                getValidStatus(bunkerScheme.getValidTime(), bunkerScheme.getInvalidTime());

                Company company = companyMap.get(bunkerScheme.getCompanyName());
                if (StrUtils.isNotNull(company)) {
                    bunkerScheme.setCompanyId(company.getCompanyId());
                } else {
                    throw new IllegalParametersException(String.format("第%s行，公司未维护", len));
                }
                List<Department>departments=departmentClient.selectByCompanyIdAndDeptLevelAndDeptName(company.getCompanyId(),null,null,bunkerScheme.getRelationDeptName());
                if (CollUtil.isNotEmpty(departments)){
                    bunkerScheme.setRelationDeptId(departments.get(0).getDeptId());
                    bunkerScheme.setRelationDeptName(departments.get(0).getDeptName());
                }else {
                    throw new IllegalParametersException(String.format("第%s行，所属分拨未维护", len));
                }
                List<ItemType> itemTypeList = itemTypeService.selectItemTypeListAllIdAndName();
                Map<String, String> itemTypeMap = itemTypeList.stream().collect(Collectors.toMap(ItemType::getItemName, ItemType::getItemId));
                if (StrUtils.isNotNull(bunkerScheme.getItemType()) && StrUtils.isNotNull(itemTypeMap.get(bunkerScheme.getItemType()))) {
                    bunkerScheme.setItemTypeId(itemTypeMap.getOrDefault(bunkerScheme.getItemType(), null));
                }else {
                    bunkerScheme.setItemTypeId(null);
                    bunkerScheme.setItemType(null);
                }
                bunkerScheme.setTenantId(employee.getTenantId());
                bunkerScheme.setSchemeId(StrUtils.getUUID());
                bunkerScheme.setInsTime(DateUtil.getNowDateTime());
                bunkerScheme.setInsUser(employee.getEmpName());
                bunkerSchemeList.add(bunkerScheme);
                len++;
            }
        }

        List<BunkerSchemeDtl> bunkerSchemeDtlList = getProvinceStageSchemeList(employee, importExcelDtlMapList);
        List<String> newSchemeNoList = bunkerSchemeList.stream().map(BunkerScheme::getSchemeNo).collect(Collectors.toList());
        if (bunkerSchemeDtlList.stream().anyMatch(item -> !newSchemeNoList.contains(item.getSchemeId()))) {
            String errorSchemeNo = bunkerSchemeDtlList.stream().filter(item ->
                    !newSchemeNoList.contains(item.getSchemeId())).findFirst().get().getSchemeId();
            throw new IllegalParametersException(String.format("明细中方案编号【%s】没有对应的方案！", errorSchemeNo));
        }

        Map<String, List<BunkerSchemeDtl>> listMap = bunkerSchemeDtlList.stream().collect(Collectors.groupingBy(BunkerSchemeDtl::getSchemeId));
        bunkerSchemeList.stream().forEach(bunkerScheme -> {
            List<BunkerSchemeDtl> dtlList = listMap.get(bunkerScheme.getSchemeNo());
            if (StrUtils.isNullList(dtlList)) {
                throw new IllegalParametersException(String.format("方案编号【%s】没有方案明细！", bunkerScheme.getSchemeNo()));
            }

            dtlList.forEach(dtl -> {
                dtl.setSchemeId(bunkerScheme.getSchemeId());
                dtl.setCompanyId(bunkerScheme.getCompanyId());
            });
            checkCoincideSection(bunkerScheme.getSchemeNo(), dtlList);
        });

        List<String> companyIdAndSchemeNoList = bunkerSchemeList.stream().map(item -> item.getCompanyId() + "->" + item.getSchemeNo()).collect(Collectors.toList());
        List<BunkerScheme> bunkerSchemeAll = bunkerSchemeMapper.selectAll();
        // 已有的方案和导入的方案方案编号相同的方案id集合
        List<String> schemeIdDelList = bunkerSchemeAll.stream().filter(item -> companyIdAndSchemeNoList.contains(item.getCompanyId() + "->" + item.getSchemeNo()))
                .map(BunkerScheme::getSchemeId).distinct().collect(Collectors.toList());
        // 数据库中已有的方案不包含与导入方案编号相同所有方案
        List<BunkerScheme> bunkerSchemeListNotContainsNewSchemeNo = bunkerSchemeAll.stream().filter(item ->
                !companyIdAndSchemeNoList.contains(item.getCompanyId() + "->" + item.getSchemeNo())).collect(Collectors.toList());

        // 验证有效期是否有重合
        bunkerSchemeList.forEach(bunkerScheme -> {
            // 验证导入的数据中的有效期是否重合
            bunkerSchemeList.forEach(item -> {
                if (!bunkerScheme.getSchemeId().equals(item.getSchemeId())
                        && StrUtils.nullToStr(bunkerScheme.getCompanyId()).equals(StrUtils.nullToStr(item.getCompanyId()))
                        && StrUtils.nullToStr(bunkerScheme.getProductType()).equals(StrUtils.nullToStr(item.getProductType()))
                        && checkValidTime(bunkerScheme, item)) {
                    throw new IllegalParametersException(String.format("公司名称【%s】新增方案编号【%s】与【%s】有效期重合！",
                            bunkerScheme.getCompanyName(), bunkerScheme.getSchemeNo(), item.getSchemeNo()));
                }
            });
            // 验证新增方案有效期是否与已存在的数据有重合
            bunkerSchemeListNotContainsNewSchemeNo.forEach(item -> {
                if (StrUtils.nullToStr(bunkerScheme.getCompanyId()).equals(StrUtils.nullToStr(item.getCompanyId()))
                        && StrUtils.nullToStr(bunkerScheme.getProductType()).equals(StrUtils.nullToStr(item.getProductType()))
                        && checkValidTime(bunkerScheme, item)) {
                    throw new IllegalParametersException(String.format("公司名称【%s】新增方案编号【%s】与已有方案编号【%s】有效期重合！",
                            bunkerScheme.getCompanyName(), bunkerScheme.getSchemeNo(), item.getSchemeNo()));
                }
            });
        });

        if (StrUtils.isNotNullList(schemeIdDelList)) {
            bunkerSchemeMapper.deleteByIds(schemeIdDelList);
            bunkerSchemeDtlMapper.deleteBySchemeIdList(schemeIdDelList);
        }
        if (StrUtils.isNotNullList(bunkerSchemeList)) {
            bunkerSchemeMapper.batchInsertSelective(bunkerSchemeList);
            bunkerSchemeDtlMapper.batchInsertSelective(bunkerSchemeDtlList);
        }
    }

    /**
     * 批量修改生效时间
     *
     * @param schemeIds 方案ids
     * @param validTime 生效时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBunkerSchemeValidTimeBySchemeIds(String schemeIds, Date validTime) {
        List<String> schemeIdList = Arrays.stream(schemeIds.split(",")).filter(item -> StrUtils.isNotNull(item))
                .distinct().collect(Collectors.toList());
        if (StrUtils.isNullList(schemeIdList)) {
            throw new IllegalParametersException("修改条数不能为空！");
        }

        List<BunkerScheme> bunkerSchemeList = bunkerSchemeMapper.selectBunkerSchemeByIds(schemeIdList);
        if (bunkerSchemeList.stream().anyMatch(item -> DataConstants.CONSTANT_STR_ONE.equals(item.getDeleteStatus()))) {
            throw new IllegalParametersException("无法修改状态为已删除的数据信息！");
        }
        if (bunkerSchemeList.stream().anyMatch(item -> !getValidStatus(item.getValidTime(), item.getInvalidTime()).equals(Dicts.ValidStatus.VALID_STATUS_19801))) {
            throw new IllegalParametersException("非未生效的数据无法修改生效时间！");
        }

        if (!DateUtil.compareDateHm(validTime, new Date())) {
            throw new IllegalParametersException("生效时间修改时要大于等于当前时间！");
        }

        bunkerSchemeList.stream().forEach(item -> {
            if (DateUtil.compareDateHm(validTime, item.getInvalidTime())) {
                throw new IllegalParametersException(String.format("方案【%s】生效时间的修改必须小于失效时间！", item.getSchemeNo()));
            }

            List<BunkerScheme> bunkerSchemes = bunkerSchemeMapper.selectListForCheck(null, item.getCompanyId(), item.getProductType());

            //有效期 验重
            if (StrUtils.isNotNullList(bunkerSchemes)) {
                //过滤掉ID相同的
                bunkerSchemes = bunkerSchemes.stream().filter(item1 -> !item.getSchemeId()
                        .equals(item1.getSchemeId()) && checkValidTime(item1, item)).collect(Collectors.toList());

                if (StrUtils.isNotNullList(bunkerSchemes)) {
                    List<String> schemeNoList = bunkerSchemes.stream().map(BunkerScheme::getSchemeNo).distinct()
                            .collect(Collectors.toList());
                    throw new IllegalParametersException(String.format("与方案【%s】的有效期重合,修改失败！",
                            String.join("、", schemeNoList)));
                }
            }
        });

        bunkerSchemeMapper.updateBunkerSchemeTimeBySchemeIds(schemeIdList, validTime, null);
    }

    /**
     * 批量修改失效时间
     *
     * @param schemeIds   方案ids
     * @param invalidTime 失效时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBunkerSchemeInvalidTimeBySchemeIds(String schemeIds, Date invalidTime) {
        List<String> schemeIdList = Arrays.stream(schemeIds.split(",")).filter(item -> StrUtils.isNotNull(item))
                .distinct().collect(Collectors.toList());
        if (StrUtils.isNullList(schemeIdList)) {
            throw new IllegalParametersException("修改条数不能为空！");
        }

        List<BunkerScheme> bunkerSchemeList = bunkerSchemeMapper.selectBunkerSchemeByIds(schemeIdList);
        if (bunkerSchemeList.stream().anyMatch(item -> DataConstants.CONSTANT_STR_ONE.equals(item.getDeleteStatus()))) {
            throw new IllegalParametersException("无法修改状态为已删除的数据信息！");
        }

        if (DateUtil.compareDateHm(new Date(), invalidTime)) {
            throw new IllegalParametersException("失效时间的修改必须大于当前时间！");
        }

        bunkerSchemeList.stream().forEach(item -> {
            if (DateUtil.compareDateHm(item.getValidTime(), invalidTime)) {
                throw new IllegalParametersException(String.format("方案【%s】失效时间的修改必须大于生效时间！", item.getSchemeNo()));
            }
            //取出来数据
            List<BunkerScheme> bunkerSchemes = bunkerSchemeMapper.selectListForCheck(null, item.getCompanyId(), item.getProductType());
            //有效期 验重
            if (StrUtils.isNotNullList(bunkerSchemes)) {
                //过滤掉ID相同的
                bunkerSchemes = bunkerSchemes.stream().filter(item1 -> !item.getSchemeId()
                        .equals(item1.getSchemeId()) && checkValidTime(item1, item)).collect(Collectors.toList());

                if (StrUtils.isNotNullList(bunkerSchemes)) {
                    List<String> schemeNoList = bunkerSchemes.stream().map(BunkerScheme::getSchemeNo).distinct()
                            .collect(Collectors.toList());
                    throw new IllegalParametersException(String.format("与方案【%s】的有效期重合,修改失败！",
                            String.join("、", schemeNoList)));
                }
            }
        });
        bunkerSchemeMapper.updateBunkerSchemeTimeBySchemeIds(schemeIdList, null, invalidTime);
    }

    /**
     * 验证编号和名称
     *
     * @Author liuyongli
     * @Date 2020/12/9
     * @param: scheme
     */
    private void checkSchemeNoAndNameRule(BunkerScheme scheme) {
        // 报价编号：只允许输入字母、数字
        String regex_no = "[0-9A-Za-z]*";
        if (!Pattern.matches(regex_no, scheme.getSchemeNo())) {
            throw new IllegalParametersException("方案编号只允许输入字母、数字！");
        }
        // 报价名称：只允许输入汉字、字母、数字
        String regex_name = "[0-9A-Za-z\u4e00-\u9fa5]*";
        if (!Pattern.matches(regex_name, scheme.getSchemeName())) {
            throw new IllegalParametersException("方案名称只允许输入汉字、字母、数字！");
        }
    }

    /**
     * 获取有效状态
     *
     * @param validTime   生效日期
     * @param invalidTime 失效日期
     * @Author liuyongli
     * @Date 2020/12/9
     */
    private String getValidStatus(Date validTime, Date invalidTime) {
        if (StrUtils.isNull(validTime)) {
            throw new IllegalParametersException("生效日期不能为空！");
        }
        if (StrUtils.isNull(invalidTime)) {
            throw new IllegalParametersException("失效日期不能为空！");
        }
        if (!validTime.before(invalidTime)) {
            throw new IllegalParametersException("失效日期必须在生效日期之后！");
        }
        Date now = DateUtil.getNowDateTime();
        if (now.before(validTime)) {
            return Dicts.ValidStatus.VALID_STATUS_19801;
        }
        if (now.after(invalidTime)) {
            return Dicts.ValidStatus.VALID_STATUS_19803;
        }
        return Dicts.ValidStatus.VALID_STATUS_19802;
    }

    /**
     * 获取有效状态
     */
    private String getSchemeStatus(Date validTime, Date invalidTime, String deleteStatus) {
        Date now = DateUtil.getNowDateTime();
        if (DataConstants.CONSTANT_STR_ONE.equals(deleteStatus)) {
            return Dicts.ValidStatus.VALID_STATUS_19804;
        }
        if (now.before(validTime)) {
            return Dicts.ValidStatus.VALID_STATUS_19801;
        }
        if (now.after(invalidTime)) {
            return Dicts.ValidStatus.VALID_STATUS_19803;
        }
        return Dicts.ValidStatus.VALID_STATUS_19802;
    }

    /**
     * 验证方案的有效期是否重合
     *
     * @return true为存在重合 false为没有重合
     * @Author liuyongli
     * @Date 2020/12/9
     * @param: scheme1
     * @param: scheme2
     */
    private boolean checkValidTime(BunkerScheme scheme1, BunkerScheme scheme2) {
        if (scheme1.getValidTime().getTime() <= scheme2.getValidTime().getTime() &&
                scheme1.getInvalidTime().getTime() > scheme2.getValidTime().getTime()) {
            return true;
        }
        if (scheme1.getValidTime().getTime() < scheme2.getInvalidTime().getTime() &&
                scheme1.getInvalidTime().getTime() >= scheme2.getInvalidTime().getTime()) {
            return true;
        }
        if (scheme1.getValidTime().getTime() <= scheme2.getValidTime().getTime() &&
                scheme1.getInvalidTime().getTime() >= scheme2.getInvalidTime().getTime()) {
            return true;
        }
        if (scheme1.getValidTime().getTime() >= scheme2.getValidTime().getTime() &&
                scheme1.getInvalidTime().getTime() <= scheme2.getInvalidTime().getTime()) {
            return true;
        }
        return false;
    }

    /**
     * 检查区间是否重合
     *
     * @Author liuyongli
     * @Date 2020/12/9
     * @param: schemeNo
     * @param: schemeDtlList
     */
    private void checkCoincideSection(String schemeNo, List<BunkerSchemeDtl> schemeDtlListAll) {
        if (StrUtils.isNullList(schemeDtlListAll)) {
            throw new IllegalParametersException("区间明细不能为空！");
        }
        if (schemeDtlListAll.stream().anyMatch(item -> StrUtils.isNull(item.getDataType()))) {
            throw new IllegalParametersException("计算方式不能为空！");
        }
        if (schemeDtlListAll.stream().anyMatch(item -> StrUtils.isNull(item.getContinuePrice()))) {
            throw new IllegalParametersException("续重/方/件单价不能为空！");
        }
        if (StrUtils.isNullList(schemeDtlListAll)) {
            throw new IllegalParametersException("区间明细不能为空！");
        }
        Map<String, List<BunkerSchemeDtl>> collect = schemeDtlListAll.stream().collect(Collectors.groupingBy(BunkerSchemeDtl::getDataType));
        schemeNo = StrUtils.isNull(schemeNo) ? "" : String.format("方案编号【%s】的", schemeNo);
        for (Map.Entry<String, List<BunkerSchemeDtl>> entry : collect.entrySet()) {
            List<BunkerSchemeDtl> schemeDtlList = entry.getValue();
            schemeDtlList = schemeDtlList.stream().sorted(Comparator.comparing(BunkerSchemeDtl::getKmLowerLimit))
                    .collect(Collectors.toList());
            for (int i = 0; i < schemeDtlList.size(); i++) {
                if (StrUtils.isNull(schemeDtlList.get(i).getKmUpperLimit())
                        || StrUtils.isNull(schemeDtlList.get(i).getKmLowerLimit())) {
                    throw new IllegalParametersException("km区间上限和区间下限都不能为空！");
                }
                if (NumberUtils.nullToZero(schemeDtlList.get(i).getKmUpperLimit())
                        <= NumberUtils.nullToZero(schemeDtlList.get(i).getKmLowerLimit())) {
                    throw new IllegalParametersException("km区间下限必须小于区间上限！");
                }
                if (StrUtils.isNull(schemeDtlList.get(i).getUpperLimit())
                        || StrUtils.isNull(schemeDtlList.get(i).getLowerLimit())) {
                    throw new IllegalParametersException("区间上限和区间下限都不能为空！");
                }
                if (NumberUtils.nullToZero(schemeDtlList.get(i).getUpperLimit())
                        <= NumberUtils.nullToZero(schemeDtlList.get(i).getLowerLimit())) {
                    throw new IllegalParametersException("区间下限必须小于区间上限！");
                }
                if (NumberUtils.isNotNull(schemeDtlList.get(i).getFirstStandard())
                        && (NumberUtils.nullToZero(schemeDtlList.get(i).getUpperLimit()) <= NumberUtils.nullToZero(schemeDtlList.get(i).getFirstStandard())
                        || NumberUtils.nullToZero(schemeDtlList.get(i).getLowerLimit()) > NumberUtils.nullToZero(schemeDtlList.get(i).getFirstStandard()))) {
                    throw new IllegalParametersException("首重/方/件必须大于等于区间下限，小于区间上限！");
                }
                if (i + 1 < schemeDtlList.size()) {
                    double currentUpperLimit = NumberUtils.nullToZero(schemeDtlList.get(i).getKmUpperLimit());
                    double nextLowerLimit = NumberUtils.nullToZero(schemeDtlList.get(i + 1).getKmLowerLimit());
                    if (currentUpperLimit != nextLowerLimit) {
                        if (currentUpperLimit > nextLowerLimit) {
                            throw new IllegalParametersException(String.format("%s区间[%s-%s]与[%s-%s]存在重合!",
                                    schemeNo, schemeDtlList.get(i).getKmLowerLimit(), currentUpperLimit, nextLowerLimit,
                                    schemeDtlList.get(i + 1).getKmUpperLimit()));
                        }
                        throw new IllegalParametersException(String.format("%s区间[%s-%s]与[%s-%s]存在间隔!",
                                schemeNo, schemeDtlList.get(i).getKmLowerLimit(), currentUpperLimit, nextLowerLimit,
                                schemeDtlList.get(i + 1).getKmUpperLimit()));
                    }
                }
                if (i + 1 < schemeDtlList.size()) {
                    double currentUpperLimit = NumberUtils.nullToZero(schemeDtlList.get(i).getUpperLimit());
                    double nextLowerLimit = NumberUtils.nullToZero(schemeDtlList.get(i + 1).getLowerLimit());
                    if (currentUpperLimit != nextLowerLimit) {
                        if (currentUpperLimit > nextLowerLimit) {
                            throw new IllegalParametersException(String.format("%s区间[%s-%s]与[%s-%s]存在重合!",
                                    schemeNo, schemeDtlList.get(i).getLowerLimit(), currentUpperLimit, nextLowerLimit,
                                    schemeDtlList.get(i + 1).getUpperLimit()));
                        }
                        throw new IllegalParametersException(String.format("%s区间[%s-%s]与[%s-%s]存在间隔!",
                                schemeNo, schemeDtlList.get(i).getLowerLimit(), currentUpperLimit, nextLowerLimit,
                                schemeDtlList.get(i + 1).getUpperLimit()));
                    }
                }
                //进位方式必须大于0
                if (schemeDtlList.get(i).getAddType() <= DataConstants.CONSTANT_DOUBLE_ZERO) {
                    throw new IllegalParametersException("【进位方式】必须大于0");
                }
            }
        }
    }


    /**
     * 封装明细
     *
     * @Author liuyongli
     * @Date 2020/12/16
     * @param: employee
     * @param: schemeDtlMapList
     */
    private List<BunkerSchemeDtl> getProvinceStageSchemeList(Employee employee, List<HashMap<String, String>> bunkerSchemeMapList) {
        if (null == bunkerSchemeMapList) {
            return new ArrayList<>();
        }
        List<BunkerSchemeDtl> bunkerSchemeDtls = bunkerSchemeMapList.stream().map(item -> {
            BunkerSchemeDtl bunkerSchemeDtl = JsonUtils.parseObjectByFastJson(JsonUtils.toJSONStringByFastJson(item), BunkerSchemeDtl.class);
            bunkerSchemeDtl.setTenantId(employee.getTenantId());
            bunkerSchemeDtl.setSchemeDtlId(StrUtils.getUUID());
            bunkerSchemeDtl.setInsTime(DateUtil.getNowDateTime());
            bunkerSchemeDtl.setInsUser(employee.getEmpName());
            return bunkerSchemeDtl;
        }).collect(Collectors.toList());
        return bunkerSchemeDtls;
    }
}
