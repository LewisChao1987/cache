package com.sinotech.system.provincePrice.service.impl;

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
import com.sinotech.system.provinceBasic.mapper.ProvinceAreaDeptHdrMgrMapper;
import com.sinotech.system.provinceBasic.model.ProvinceAreaDeptHdr;
import com.sinotech.system.provincePrice.mapper.ProvinceStageSchemeDtlMapper;
import com.sinotech.system.provincePrice.mapper.ProvinceStageSchemeMapper;
import com.sinotech.system.provincePrice.model.ProvinceStageScheme;
import com.sinotech.system.provincePrice.model.ProvinceStageSchemeDtl;
import com.sinotech.system.provincePrice.model.ProvinceStageSchemeVO;
import com.sinotech.system.provincePrice.service.ProvinceStageSchemeService;
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
 * @ClassName ProvinceStageSchemeServiceImpl
 * @Description 省网全段运费方案
 * @Author szq
 * @Date 2022/3/16 9:36
 * @Version 1.0
 */
@Service
public class ProvinceStageSchemeServiceImpl implements ProvinceStageSchemeService {
    @Autowired
    private CompanyClient companyClient;
    @Autowired
    private ProvinceStageSchemeMapper provinceStageSchemeMapper;
    @Autowired
    private ProvinceStageSchemeDtlMapper provinceStageSchemeDtlMapper;
    @Autowired
    private DepartmentClient departmentClient;
    @Autowired
    private ImportExcelService importExcelService;
    @Autowired
    private ProvinceAreaDeptHdrMgrMapper provinceAreaDeptHdrMgrMapper;
    @Autowired
    private ItemTypeService itemTypeService;

    /**
     * @param provinceStageScheme
     * @param jsonStr
     * @Description: 新增省网全段运费方案
     * @param: provinceStageScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProvinceStageScheme(ProvinceStageScheme provinceStageScheme, String jsonStr) {
        Employee employee = SessionUtils.getCurrentUser();
        //校验方案编号  名称是否符合规范
        checkSchemeNoAndNameRule(provinceStageScheme);
        //校验有效期开始 和 结束
        getValidStatus(provinceStageScheme.getValidTime(), provinceStageScheme.getInvalidTime());
        if (StrUtils.isNull(provinceStageScheme.getCompanyId())) {
            throw new IllegalParametersException("报价省区不能为空！");
        }
        Company company = companyClient.selectCompanyById(provinceStageScheme.getCompanyId());
        if (StrUtils.isNull(company)) {
            throw new IllegalParametersException("省区未维护！");
        }
        if (!DataConstants.CONSTANT_STR_ONE.equals(company.getCompanyStatus())) {
            throw new IllegalParametersException(String.format("省区【%s】未启用 操作失败！", company.getCompanyName()));
        }
        List<ProvinceStageScheme> checkSchemeNoList = provinceStageSchemeMapper.selectListForUnique(provinceStageScheme.getSchemeNo(), null);
        if (StrUtils.isNotNullList(checkSchemeNoList)) {
            throw new IllegalParametersException(String.format("方案编号【%s】已存在！", provinceStageScheme.getSchemeNo()));
        }
        List<ProvinceStageScheme> checkSchemeNameList = provinceStageSchemeMapper.selectListForUnique(null, provinceStageScheme.getSchemeName());
        if (StrUtils.isNotNullList(checkSchemeNameList)) {
            throw new IllegalParametersException(String.format("方案名称【%s】已存在！", provinceStageScheme.getSchemeName()));
        }
        List<ProvinceStageScheme> checkList = provinceStageSchemeMapper.selectListForCheck(provinceStageScheme.getBillAreaId(),
                provinceStageScheme.getDiscAreaId(), provinceStageScheme.getCompanyId());
        if (StrUtils.isNotNullList(checkList)) {
            checkList.forEach(item -> {
                if (checkValidTime(provinceStageScheme, item)) {
                    throw new IllegalParametersException(String.format("与方案编号【%s】的有效期重合！", item.getSchemeNo()));
                }
            });
        }
        provinceStageScheme.setSchemeId(StrUtils.getUUID());
        provinceStageScheme.setTenantId(company.getTenantId());
        provinceStageScheme.setCompanyName(company.getCompanyName());
        provinceStageScheme.setInsUser(employee.getEmpName());
        provinceStageScheme.setInsTime(DateUtil.getNowDateTime());
        if (StrUtils.isNotNull(jsonStr)) {
            List<ProvinceStageSchemeDtl> provinceStageSchemeDtlList = JsonUtils.toList(jsonStr, ProvinceStageSchemeDtl.class);
            // 检查区间是否重合
            checkCoincideSection(null, provinceStageSchemeDtlList);

            provinceStageSchemeDtlList.forEach(item -> {
                item.setSchemeDtlId(StrUtils.getUUID());
                item.setTenantId(employee.getTenantId());
                item.setCompanyId(employee.getCompanyId());
                item.setSchemeId(provinceStageScheme.getSchemeId());
                item.setInsUser(employee.getEmpName());
                item.setInsTime(DateUtil.getNowDateTime());
            });
            provinceStageSchemeDtlMapper.batchInsertSelective(provinceStageSchemeDtlList);
        }
        provinceStageSchemeMapper.insertSelective(provinceStageScheme);
    }

    /**
     * @param provinceStageScheme
     * @param jsonStr
     * @Description:修改省网全段运费方案
     * @param:provinceStageScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editProvinceStageScheme(ProvinceStageScheme provinceStageScheme, String jsonStr) {
        Employee employee = SessionUtils.getCurrentUser();
        checkSchemeNoAndNameRule(provinceStageScheme);
        getValidStatus(provinceStageScheme.getValidTime(), provinceStageScheme.getInvalidTime());

        if (StrUtils.isNull(provinceStageScheme.getCompanyId())) {
            throw new IllegalParametersException("报价省区不能为空！");
        }
        Company company = companyClient.selectCompanyById(provinceStageScheme.getCompanyId());
        if (StrUtils.isNull(company)) {
            throw new IllegalParametersException("省区未维护！");
        }
        if (!DataConstants.CONSTANT_STR_ONE.equals(company.getCompanyStatus())) {
            throw new IllegalParametersException(String.format("省区【%s】未启用 操作失败！", company.getCompanyName()));
        }
        ProvinceStageScheme provinceStageSchemeOld = provinceStageSchemeMapper.selectByPrimaryKey(provinceStageScheme.getSchemeId());
        if (provinceStageSchemeOld == null) {
            throw new IllegalParametersException("未查询到方案信息！");
        }
        if (provinceStageScheme.getValidTime().getTime() != provinceStageSchemeOld.getValidTime().getTime()) {
            throw new BusinessProhibitionException("生效日期禁止编辑");
        }
        List<ProvinceStageScheme> checkSchemeNoList = provinceStageSchemeMapper.selectListForUnique(provinceStageScheme.getSchemeNo(), null);
        if (StrUtils.isNotNullList(checkSchemeNoList) && checkSchemeNoList.stream().anyMatch(item -> !provinceStageScheme.getSchemeId().equals(item.getSchemeId()))) {
            throw new IllegalParametersException(String.format("方案编号【%s】已存在！", provinceStageScheme.getSchemeNo()));
        }
        List<ProvinceStageScheme> checkSchemeNameList = provinceStageSchemeMapper.selectListForUnique(null, provinceStageScheme.getSchemeName());
        if (StrUtils.isNotNullList(checkSchemeNameList) && checkSchemeNameList.stream().anyMatch(item -> !provinceStageScheme.getSchemeId().equals(item.getSchemeId()))) {
            throw new IllegalParametersException(String.format("方案名称【%s】已存在！", provinceStageScheme.getSchemeName()));
        }
        if (DateUtil.compareDateHm(new Date(), provinceStageScheme.getInvalidTime())) {
            throw new IllegalParametersException("失效时间的修改必须大于当前时间！");
        }
        List<ProvinceStageScheme> checkList = provinceStageSchemeMapper.selectListForCheck(provinceStageScheme.getBillAreaId(),
                provinceStageScheme.getDiscAreaId(), provinceStageScheme.getCompanyId());
        if (StrUtils.isNotNullList(checkList) && checkList.stream().anyMatch(item -> !provinceStageScheme.getSchemeId().equals(item.getSchemeId()))) {
            checkList.stream().filter(item -> !provinceStageScheme.getSchemeId().equals(item.getSchemeId())).forEach(item -> {
                if (checkValidTime(provinceStageScheme, item)) {
                    throw new IllegalParametersException(String.format("与方案编号【%s】的有效期重合！", item.getSchemeNo()));
                }
            });
        }
        provinceStageScheme.setUpdUser(employee.getEmpName());
        provinceStageScheme.setUpdTime(DateUtil.getNowDateTime());

        if (StrUtils.isNotNull(jsonStr)) {
            List<ProvinceStageSchemeDtl> provinceStageSchemeDtlList = JsonUtils.toList(jsonStr, ProvinceStageSchemeDtl.class);
            // 检查区间是否重合
            checkCoincideSection(null, provinceStageSchemeDtlList);

            provinceStageSchemeDtlList.forEach(item -> {
                item.setSchemeDtlId(StrUtils.getUUID());
                item.setTenantId(employee.getTenantId());
                item.setCompanyId(employee.getCompanyId());
                item.setSchemeId(provinceStageScheme.getSchemeId());
                item.setInsUser(employee.getEmpName());
                item.setInsTime(DateUtil.getNowDateTime());
            });
            provinceStageSchemeDtlMapper.deleteBySchemeIdList(Lists.newArrayList(provinceStageScheme.getSchemeId()));
            provinceStageSchemeDtlMapper.batchInsertSelective(provinceStageSchemeDtlList);
        }
        provinceStageSchemeMapper.updateByPrimaryKeySelective(provinceStageScheme);
    }

    /**
     * @param schemeId
     * @Description: 删除省网全段运费方案
     * @param:schemeId
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProvinceStageScheme(String schemeId) {
        List<String> schemeIdList = Arrays.stream(schemeId.split(",")).filter(StrUtils::isNotNull).collect(Collectors.toList());
        if (StrUtils.isNullList(schemeIdList)) {
            throw new IllegalParametersException("方案id不能为空！");
        }
        List<String> deleteSchemeIdList = Lists.newArrayList();
        List<ProvinceStageScheme> voList = provinceStageSchemeMapper.selectProvinceStageSchemeByIds(schemeIdList);
        for (ProvinceStageScheme vo : voList) {
            if (!getValidStatus(vo.getValidTime(), vo.getInvalidTime()).equals(Dicts.ValidStatus.VALID_STATUS_19803)) {
                throw new IllegalParametersException("请选择已失效的数据信息进行删除！");
            }
            deleteSchemeIdList.add(vo.getSchemeId());
        }
        if (CollUtil.isNotEmpty(deleteSchemeIdList)) {
            provinceStageSchemeMapper.deleteByIds(deleteSchemeIdList);
            provinceStageSchemeDtlMapper.deleteBySchemeIdList(deleteSchemeIdList);
        }
    }

    /**
     * @param schemeId
     * @Description:查询单个方案
     * @param:schemeId
     * @return: ProvinceStageScheme
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    public ProvinceStageSchemeVO selectProvinceStageScheme(String schemeId) {
        ProvinceStageScheme provinceStageScheme = provinceStageSchemeMapper.selectByPrimaryKey(schemeId);
        ProvinceStageSchemeVO provinceStageSchemeVO = new ProvinceStageSchemeVO();
        if (StrUtils.isNotNull(provinceStageScheme)) {
            BeanUtils.copyProperties(provinceStageScheme, provinceStageSchemeVO);
            List<ProvinceStageSchemeDtl> provinceStageSchemeDtlList = provinceStageSchemeDtlMapper.selectStageSchemeDtlBySchemeId(schemeId);
            provinceStageSchemeVO.setProvinceStageSchemeDtlList(provinceStageSchemeDtlList);
        }
        return provinceStageSchemeVO;
    }

    /**
     * @param param
     * @Description: 查询列表
     * @param: param
     * @Author: szq
     * @date: 2022/3/16
     */
    @Override
    public List<ProvinceStageSchemeVO> selectProvinceStageSchemeList(ProvinceStageSchemeVO param) {
        Employee employee = SessionUtils.getCurrentUser();
        if (!DataConstants.CENTER_COMPANY_ID.equals(employee.getCompanyId())) {
            if (StrUtils.isNull(param.getCompanyId())) {
                param.setCompanyId(employee.getCompanyId());
            } else if (!employee.getCompanyId().equals(param.getCompanyId())) {
                return new ArrayList<>();
            }
        }
        List<ProvinceStageScheme> list = provinceStageSchemeMapper.selectProvinceStageSchemeList(param);
        List<ProvinceStageSchemeVO> result = new ArrayList<>(list.size());
        if (CollUtil.isNotEmpty(list)) {
            for (ProvinceStageScheme provinceStageScheme : list) {
                ProvinceStageSchemeVO provinceStageSchemeVO = new ProvinceStageSchemeVO();
                BeanUtils.copyProperties(provinceStageScheme, provinceStageSchemeVO);
                provinceStageSchemeVO.setSchemeStatus(getSchemeStatus(provinceStageSchemeVO.getValidTime(), provinceStageSchemeVO.getInvalidTime(), provinceStageSchemeVO.getDeleteStatus()));
                result.add(provinceStageSchemeVO);
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
    public ResponseEntity exportProvinceStageScheme(ProvinceStageSchemeVO param) {
        List<ProvinceStageSchemeVO> provinceStageSchemeList = selectProvinceStageSchemeList(param);
        if (StrUtils.isNotNullList(provinceStageSchemeList)) {
            List<String> schemeIdList = provinceStageSchemeList.stream().map(ProvinceStageSchemeVO::getSchemeId).collect(Collectors.toList());
            List<ProvinceStageSchemeDtl> provinceStageSchemeDtlList = provinceStageSchemeDtlMapper.selectStageSchemeDtlBySchemeIds(schemeIdList);
            Map<String, List<ProvinceStageSchemeDtl>> listMap = provinceStageSchemeDtlList.stream().collect(Collectors.groupingBy(item -> item.getSchemeId()));

            List<Map<String, Object>> mapList1 = new ArrayList<>();
            List<Map<String, Object>> mapList2 = new ArrayList<>();
            List<Map<String, Object>> mapList3 = new ArrayList<>();
            List<Map<String, Object>> mapList4 = new ArrayList<>();

            provinceStageSchemeList.stream().forEach(item -> {
                Map<String, Object> map = ObjectUtils.toMap(item);
                map.put("validTime", DateUtil.getDateTimeFormat(item.getValidTime()));
                map.put("invalidTime", DateUtil.getDateTimeFormat(item.getInvalidTime()));

                List<ProvinceStageSchemeDtl> provinceStageSchemeDtls = listMap.get(item.getSchemeId());
                Map<String, List<ProvinceStageSchemeDtl>> ProvinceStageSchemeDtlListMap = provinceStageSchemeDtls.stream().collect(Collectors.groupingBy(ProvinceStageSchemeDtl::getDataType));
                for (Map.Entry<String, List<ProvinceStageSchemeDtl>> entry : ProvinceStageSchemeDtlListMap.entrySet()) {
                    List<ProvinceStageSchemeDtl> schemeDtls = entry.getValue();
                    schemeDtls = schemeDtls.stream().sorted(Comparator.comparing(ProvinceStageSchemeDtl::getLowerLimit)).collect(Collectors.toList());
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
            List<String> titleList = Arrays.asList("省网全段运费方案", "重量区间", "体积区间", "件数区间");
            try {
                return PoiExcelUtils.generateExcel(mapList, headList, titleList, "省网全段运费");
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
    public void importProvinceStageScheme(String fileName, MultipartFile file) {
        Employee employee = SessionUtils.getCurrentUser();
        List<ImportExcel> importExcelList = importExcelService.selectImportExcelByModelId("provinceStageScheme");
        //excel 从第二行读取的 第三行才是数据
        List<HashMap<String, String>> provinceStageSchemeMapList = PoiExcelUtils.analysisExcel(fileName, file, importExcelList, 2, 0);
        if (StrUtils.isNullList(provinceStageSchemeMapList)) {
            throw new IllegalParametersException("方案至少插入一条数据！");
        }

        List<HashMap<String, String>> importExcelDtlMapList = new ArrayList<>();
        //重量
        List<ImportExcel> importExcelDtlList = importExcelService.selectImportExcelByModelId("provinceStageSchemeDtl");
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
        List<ProvinceStageScheme> provinceStageSchemeList = new ArrayList<>();
        for (HashMap item : provinceStageSchemeMapList) {
            List<String> strList = new ArrayList<>();
            ProvinceStageScheme provinceStageScheme = JsonUtils.parseObjectByFastJson(JsonUtils.toJSONStringByFastJson(item), ProvinceStageScheme.class);
            if (StrUtils.isNull(provinceStageScheme.getSchemeNo())) {
                strList.add("报价编号");
            }
            if (StrUtils.isNull(provinceStageScheme.getSchemeName())) {
                strList.add("报价名称");
            }
            if (StrUtils.isNull(provinceStageScheme.getCompanyName())) {
                strList.add("省区名称");
            }
            if (StrUtils.isNull(provinceStageScheme.getValidTime())) {
                strList.add("生效日期");
            }
            if (StrUtils.isNull(provinceStageScheme.getInvalidTime())) {
                strList.add("失效日期");
            }
            if (StrUtils.isNotNullList(strList)) {
                throw new IllegalParametersException(String.format("第%s行，【%s】不得为空", len, String.join("、", strList)));
            }

            checkSchemeNoAndNameRule(provinceStageScheme);
            getValidStatus(provinceStageScheme.getValidTime(), provinceStageScheme.getInvalidTime());

            Company company = companyMap.get(provinceStageScheme.getCompanyName());
            if (StrUtils.isNotNull(company)) {
                provinceStageScheme.setCompanyId(company.getCompanyId());
            }
            setBillAreaAndDiscArea(departmentMap, provinceAreaDeptHdrMap, provinceStageScheme);
            List<ItemType> itemTypeList = itemTypeService.selectItemTypeListAllIdAndName();
            Map<String, String> itemTypeMap = itemTypeList.stream().collect(Collectors.toMap(ItemType::getItemName, ItemType::getItemId));
            if (StrUtils.isNotNull(provinceStageScheme.getItemType()) && StrUtils.isNotNull(itemTypeMap.get(provinceStageScheme.getItemType()))) {
                provinceStageScheme.setItemTypeId(itemTypeMap.getOrDefault(provinceStageScheme.getItemType(), null));
            } else {
                provinceStageScheme.setItemTypeId(null);
                provinceStageScheme.setItemType(null);
            }
            provinceStageScheme.setTenantId(employee.getTenantId());
            provinceStageScheme.setSchemeId(StrUtils.getUUID());
            provinceStageScheme.setInsTime(DateUtil.getNowDateTime());
            provinceStageScheme.setInsUser(employee.getEmpName());
            provinceStageSchemeList.add(provinceStageScheme);
            len++;
        }
        ;

        List<ProvinceStageSchemeDtl> provinceStageSchemeDtlList = getProvinceStageSchemeList(employee, importExcelDtlMapList);
        List<String> newSchemeNoList = provinceStageSchemeList.stream().map(ProvinceStageScheme::getSchemeNo).collect(Collectors.toList());
        if (provinceStageSchemeDtlList.stream().anyMatch(item -> !newSchemeNoList.contains(item.getSchemeId()))) {
            String errorSchemeNo = provinceStageSchemeDtlList.stream().filter(item ->
                    !newSchemeNoList.contains(item.getSchemeId())).findFirst().get().getSchemeId();
            throw new IllegalParametersException(String.format("明细中方案编号【%s】没有对应的方案！", errorSchemeNo));
        }

        Map<String, List<ProvinceStageSchemeDtl>> listMap = provinceStageSchemeDtlList.stream().collect(Collectors.groupingBy(ProvinceStageSchemeDtl::getSchemeId));
        provinceStageSchemeList.stream().forEach(provinceStageScheme -> {
            List<ProvinceStageSchemeDtl> dtlList = listMap.get(provinceStageScheme.getSchemeNo());
            if (StrUtils.isNullList(dtlList)) {
                throw new IllegalParametersException(String.format("方案编号【%s】没有方案明细！", provinceStageScheme.getSchemeNo()));
            }

            dtlList.forEach(dtl -> {
                dtl.setSchemeId(provinceStageScheme.getSchemeId());
                dtl.setCompanyId(provinceStageScheme.getCompanyId());
            });
            checkCoincideSection(provinceStageScheme.getSchemeNo(), dtlList);
        });

        List<String> companyIdAndSchemeNoList = provinceStageSchemeList.stream().map(item -> item.getCompanyId() + "->" + item.getSchemeNo()).collect(Collectors.toList());
        List<ProvinceStageScheme> provinceStageSchemeAll = provinceStageSchemeMapper.selectAll();
        // 已有的方案和导入的方案方案编号相同的方案id集合
        List<String> schemeIdDelList = provinceStageSchemeAll.stream().filter(item -> companyIdAndSchemeNoList.contains(item.getCompanyId() + "->" + item.getSchemeNo()))
                .map(ProvinceStageScheme::getSchemeId).distinct().collect(Collectors.toList());
        // 数据库中已有的方案不包含与导入方案编号相同所有方案
        List<ProvinceStageScheme> provinceStageSchemeListNotContainsNewSchemeNo = provinceStageSchemeAll.stream().filter(item ->
                !companyIdAndSchemeNoList.contains(item.getCompanyId() + "->" + item.getSchemeNo())).collect(Collectors.toList());

        // 验证有效期是否有重合
        provinceStageSchemeList.forEach(provinceStageScheme -> {
            // 验证导入的数据中的有效期是否重合
            provinceStageSchemeList.forEach(item -> {
                if (!provinceStageScheme.getSchemeId().equals(item.getSchemeId())
                        && StrUtils.nullToStr(provinceStageScheme.getCompanyId()).equals(StrUtils.nullToStr(item.getCompanyId()))
                        && StrUtils.nullToStr(provinceStageScheme.getBillAreaId()).equals(StrUtils.nullToStr(item.getBillAreaId()))
                        && StrUtils.nullToStr(provinceStageScheme.getDiscAreaId()).equals(StrUtils.nullToStr(item.getDiscAreaId()))
                        && StrUtils.nullToStr(provinceStageScheme.getProductType()).equals(StrUtils.nullToStr(item.getProductType()))
                        && checkValidTime(provinceStageScheme, item)) {
                    throw new IllegalParametersException(String.format("公司名称【%s】新增方案编号【%s】与【%s】有效期重合！",
                            provinceStageScheme.getCompanyName(), provinceStageScheme.getSchemeNo(), item.getSchemeNo()));
                }
            });
            // 验证新增方案有效期是否与已存在的数据有重合
            provinceStageSchemeListNotContainsNewSchemeNo.forEach(item -> {
                if (StrUtils.nullToStr(provinceStageScheme.getCompanyId()).equals(StrUtils.nullToStr(item.getCompanyId()))
                        && StrUtils.nullToStr(provinceStageScheme.getBillAreaId()).equals(StrUtils.nullToStr(item.getBillAreaId()))
                        && StrUtils.nullToStr(provinceStageScheme.getDiscAreaId()).equals(StrUtils.nullToStr(item.getDiscAreaId()))
                        && StrUtils.nullToStr(provinceStageScheme.getProductType()).equals(StrUtils.nullToStr(item.getProductType()))
                        && checkValidTime(provinceStageScheme, item)) {
                    throw new IllegalParametersException(String.format("公司名称【%s】新增方案编号【%s】与已有方案编号【%s】有效期重合！",
                            provinceStageScheme.getCompanyName(), provinceStageScheme.getSchemeNo(), item.getSchemeNo()));
                }
            });
        });

        if (StrUtils.isNotNullList(schemeIdDelList)) {
            provinceStageSchemeMapper.deleteByIds(schemeIdDelList);
            provinceStageSchemeDtlMapper.deleteBySchemeIdList(schemeIdDelList);

        }
        if (StrUtils.isNotNullList(provinceStageSchemeList)) {
            provinceStageSchemeMapper.batchInsertSelective(provinceStageSchemeList);
            provinceStageSchemeDtlMapper.batchInsertSelective(provinceStageSchemeDtlList);
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
    public void updateProvinceStageSchemeValidTimeBySchemeIds(String schemeIds, Date validTime) {
        List<String> schemeIdList = Arrays.stream(schemeIds.split(",")).filter(item -> StrUtils.isNotNull(item))
                .distinct().collect(Collectors.toList());
        if (StrUtils.isNullList(schemeIdList)) {
            throw new IllegalParametersException("修改条数不能为空！");
        }

        List<ProvinceStageScheme> provinceStageSchemeList = provinceStageSchemeMapper.selectProvinceStageSchemeByIds(schemeIdList);
        if (provinceStageSchemeList.stream().anyMatch(item -> DataConstants.CONSTANT_STR_ONE.equals(item.getDeleteStatus()))) {
            throw new IllegalParametersException("无法修改状态为已删除的数据信息！");
        }
        if (provinceStageSchemeList.stream().anyMatch(item -> !getValidStatus(item.getValidTime(), item.getInvalidTime()).equals(Dicts.ValidStatus.VALID_STATUS_19801))) {
            throw new IllegalParametersException("非未生效的数据无法修改生效时间！");
        }

        if (!DateUtil.compareDateHm(validTime, new Date())) {
            throw new IllegalParametersException("生效时间修改时要大于等于当前时间！");
        }

        provinceStageSchemeList.stream().forEach(item -> {
            if (DateUtil.compareDateHm(validTime, item.getInvalidTime())) {
                throw new IllegalParametersException(String.format("方案【%s】生效时间的修改必须小于失效时间！", item.getSchemeNo()));
            }

            List<ProvinceStageScheme> provinceStageSchemes = provinceStageSchemeMapper.selectListForCheck(item.getBillAreaId(),
                    item.getDiscAreaId(), item.getCompanyId());
            //有效期 验重
            if (StrUtils.isNotNullList(provinceStageSchemes)) {
                //过滤掉ID相同的
                provinceStageSchemes = provinceStageSchemes.stream().filter(item1 -> !item.getSchemeId()
                        .equals(item1.getSchemeId()) && checkValidTime(item1, item)).collect(Collectors.toList());

                if (StrUtils.isNotNullList(provinceStageSchemes)) {
                    List<String> schemeNoList = provinceStageSchemes.stream().map(ProvinceStageScheme::getSchemeNo).distinct()
                            .collect(Collectors.toList());
                    throw new IllegalParametersException(String.format("与方案【%s】的有效期重合,修改失败！",
                            String.join("、", schemeNoList)));
                }
            }
        });
        provinceStageSchemeMapper.updateProvinceStageSchemeTimeBySchemeIds(schemeIdList, validTime, null);
    }

    /**
     * 批量修改失效时间
     *
     * @param schemeIds   方案ids
     * @param invalidTime 失效时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProvinceStageSchemeInvalidTimeBySchemeIds(String schemeIds, Date invalidTime) {
        List<String> schemeIdList = Arrays.stream(schemeIds.split(",")).filter(item -> StrUtils.isNotNull(item))
                .distinct().collect(Collectors.toList());
        if (StrUtils.isNullList(schemeIdList)) {
            throw new IllegalParametersException("修改条数不能为空！");
        }

        List<ProvinceStageScheme> provinceStageSchemeList = provinceStageSchemeMapper.selectProvinceStageSchemeByIds(schemeIdList);
        if (provinceStageSchemeList.stream().anyMatch(item -> DataConstants.CONSTANT_STR_ONE.equals(item.getDeleteStatus()))) {
            throw new IllegalParametersException("无法修改状态为已删除的数据信息！");
        }

        if (DateUtil.compareDateHm(new Date(), invalidTime)) {
            throw new IllegalParametersException("失效时间的修改必须大于当前时间！");
        }

        provinceStageSchemeList.stream().forEach(item -> {
            if (DateUtil.compareDateHm(item.getValidTime(), invalidTime)) {
                throw new IllegalParametersException(String.format("方案【%s】失效时间的修改必须大于生效时间！", item.getSchemeNo()));
            }
            //取出来数据
            List<ProvinceStageScheme> provinceStageSchemes = provinceStageSchemeMapper.selectListForCheck(item.getBillAreaId(),
                    item.getDiscAreaId(), item.getCompanyId());
            //有效期 验重
            if (StrUtils.isNotNullList(provinceStageSchemes)) {
                //过滤掉ID相同的
                provinceStageSchemes = provinceStageSchemes.stream().filter(item1 -> !item.getSchemeId()
                        .equals(item1.getSchemeId()) && checkValidTime(item1, item)).collect(Collectors.toList());

                if (StrUtils.isNotNullList(provinceStageSchemes)) {
                    List<String> schemeNoList = provinceStageSchemes.stream().map(ProvinceStageScheme::getSchemeNo).distinct()
                            .collect(Collectors.toList());
                    throw new IllegalParametersException(String.format("与方案【%s】的有效期重合,修改失败！",
                            String.join("、", schemeNoList)));
                }
            }
        });
        provinceStageSchemeMapper.updateProvinceStageSchemeTimeBySchemeIds(schemeIdList, null, invalidTime);
    }

    /**
     * 验证编号和名称
     *
     * @Author liuyongli
     * @Date 2020/12/9
     * @param: scheme
     */
    private void checkSchemeNoAndNameRule(ProvinceStageScheme scheme) {
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
        //未生效
        if (now.before(validTime)) {
            return Dicts.ValidStatus.VALID_STATUS_19801;
        }
        //失效
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
    private boolean checkValidTime(ProvinceStageScheme scheme1, ProvinceStageScheme scheme2) {
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
    private void checkCoincideSection(String schemeNo, List<ProvinceStageSchemeDtl> schemeDtlListAll) {
        if (StrUtils.isNullList(schemeDtlListAll)) {
            throw new IllegalParametersException("区间明细不能为空！");
        }
        if (schemeDtlListAll.stream().anyMatch(item -> StrUtils.isNull(item.getDataType()))) {
            throw new IllegalParametersException("计算方式不能为空！");
        }
        Map<String, List<ProvinceStageSchemeDtl>> collect = schemeDtlListAll.stream().collect(Collectors.groupingBy(ProvinceStageSchemeDtl::getDataType));
        schemeNo = StrUtils.isNull(schemeNo) ? "" : String.format("方案编号【%s】的", schemeNo);
        for (Map.Entry<String, List<ProvinceStageSchemeDtl>> entry : collect.entrySet()) {
            List<ProvinceStageSchemeDtl> schemeDtlList = entry.getValue();
            schemeDtlList = schemeDtlList.stream().sorted(Comparator.comparing(ProvinceStageSchemeDtl::getLowerLimit))
                    .collect(Collectors.toList());
            for (int i = 0; i < schemeDtlList.size(); i++) {
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
                    throw new IllegalParametersException("首重必须大于等于区间下限，小于区间上限！");
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
     * 设置始发区域和到达区域信息
     *
     * @Author liuyongli
     * @Date 2020/12/16
     * @param: departmentMap
     * @param: areaDeptHdrMap
     * @param: scheme
     */
    private void setBillAreaAndDiscArea(Map<String, DepartmentVo> departmentMap, Map<String, ProvinceAreaDeptHdr> areaDeptHdrMap, ProvinceStageScheme scheme) {
        if (departmentMap.containsKey(scheme.getBillAreaNo())) {
            DepartmentVo department = departmentMap.get(scheme.getBillAreaNo());
            scheme.setBillAreaId(department.getDeptId());
            scheme.setBillAreaName(department.getDeptName());
            scheme.setBillAreaNo(department.getDeptNo());
            scheme.setBillAreaType(Dicts.AreaType.AREA_TYPE_67701);
        } else if (departmentMap.containsKey(scheme.getBillAreaName())) {
            DepartmentVo department = departmentMap.get(scheme.getBillAreaName());
            scheme.setBillAreaId(department.getDeptId());
            scheme.setBillAreaName(department.getDeptName());
            scheme.setBillAreaNo(department.getDeptNo());
            scheme.setBillAreaType(Dicts.AreaType.AREA_TYPE_67701);
        } else if (areaDeptHdrMap.containsKey(scheme.getBillAreaNo())) {
            ProvinceAreaDeptHdr provinceAreaDeptHdr = areaDeptHdrMap.get(scheme.getBillAreaNo());
            scheme.setBillAreaId(provinceAreaDeptHdr.getDeptAreaId());
            scheme.setBillAreaName(provinceAreaDeptHdr.getDeptAreaName());
            scheme.setBillAreaNo(provinceAreaDeptHdr.getDeptAreaNo());
            scheme.setBillAreaType(Dicts.AreaType.AREA_TYPE_67702);
        } else if (areaDeptHdrMap.containsKey(scheme.getBillAreaName())) {
            ProvinceAreaDeptHdr provinceAreaDeptHdr = areaDeptHdrMap.get(scheme.getBillAreaName());
            scheme.setBillAreaId(provinceAreaDeptHdr.getDeptAreaId());
            scheme.setBillAreaName(provinceAreaDeptHdr.getDeptAreaName());
            scheme.setBillAreaNo(provinceAreaDeptHdr.getDeptAreaNo());
            scheme.setBillAreaType(Dicts.AreaType.AREA_TYPE_67702);
        } else {
//            throw new IllegalParametersException("未找到对应的始发区域编号或始发区域名称信息！");
        }

        if (departmentMap.containsKey(scheme.getDiscAreaNo())) {
            DepartmentVo department = departmentMap.get(scheme.getDiscAreaNo());
            scheme.setDiscAreaId(department.getDeptId());
            scheme.setDiscAreaName(department.getDeptName());
            scheme.setDiscAreaNo(department.getDeptNo());
            scheme.setDiscAreaType(Dicts.AreaType.AREA_TYPE_67701);
        } else if (departmentMap.containsKey(scheme.getDiscAreaName())) {
            DepartmentVo department = departmentMap.get(scheme.getDiscAreaName());
            scheme.setDiscAreaId(department.getDeptId());
            scheme.setDiscAreaName(department.getDeptName());
            scheme.setDiscAreaNo(department.getDeptNo());
            scheme.setDiscAreaType(Dicts.AreaType.AREA_TYPE_67701);
        } else if (areaDeptHdrMap.containsKey(scheme.getDiscAreaNo())) {
            ProvinceAreaDeptHdr provinceAreaDeptHdr = areaDeptHdrMap.get(scheme.getDiscAreaNo());
            scheme.setDiscAreaId(provinceAreaDeptHdr.getDeptAreaId());
            scheme.setDiscAreaName(provinceAreaDeptHdr.getDeptAreaName());
            scheme.setDiscAreaNo(provinceAreaDeptHdr.getDeptAreaNo());
            scheme.setDiscAreaType(Dicts.AreaType.AREA_TYPE_67702);
        } else if (areaDeptHdrMap.containsKey(scheme.getDiscAreaName())) {
            ProvinceAreaDeptHdr provinceAreaDeptHdr = areaDeptHdrMap.get(scheme.getDiscAreaName());
            scheme.setDiscAreaId(provinceAreaDeptHdr.getDeptAreaId());
            scheme.setDiscAreaName(provinceAreaDeptHdr.getDeptAreaName());
            scheme.setDiscAreaNo(provinceAreaDeptHdr.getDeptAreaNo());
            scheme.setDiscAreaType(Dicts.AreaType.AREA_TYPE_67702);
        } else {
//            throw new IllegalParametersException("未找到对应的到达区域编号或到达区域名称信息！");
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
    private List<ProvinceStageSchemeDtl> getProvinceStageSchemeList(Employee employee, List<HashMap<String, String>> provinceStageSchemeMapList) {
        if (null == provinceStageSchemeMapList) {
            return new ArrayList<>();
        }
        List<ProvinceStageSchemeDtl> provinceStageSchemeDtlList = provinceStageSchemeMapList.stream().map(item -> {
            ProvinceStageSchemeDtl provinceStageSchemeDtl = JsonUtils.parseObjectByFastJson(JsonUtils.toJSONStringByFastJson(item), ProvinceStageSchemeDtl.class);
            provinceStageSchemeDtl.setTenantId(employee.getTenantId());
            provinceStageSchemeDtl.setSchemeDtlId(StrUtils.getUUID());
            provinceStageSchemeDtl.setInsTime(DateUtil.getNowDateTime());
            provinceStageSchemeDtl.setInsUser(employee.getEmpName());
            return provinceStageSchemeDtl;
        }).collect(Collectors.toList());
        return provinceStageSchemeDtlList;
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
}
