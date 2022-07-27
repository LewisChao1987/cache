package com.sinotech.system.provincePrice.mapper;


import com.sinotech.system.provincePrice.model.ProvinceStageScheme;
import com.sinotech.system.provincePrice.model.ProvinceStageSchemeVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ProvinceStageSchemeMapper {

    int deleteByPrimaryKey(String schemeId);

    int insert(ProvinceStageScheme record);

    int insertSelective(ProvinceStageScheme record);

    ProvinceStageScheme selectByPrimaryKey(String schemeId);

    int updateByPrimaryKeySelective(ProvinceStageScheme record);

    int updateByPrimaryKey(ProvinceStageScheme record);

    List<ProvinceStageScheme> selectListForCheck(@Param("billAreaId") String billAreaId,@Param("discAreaId") String discAreaId, @Param("companyId") String companyId);

    List<ProvinceStageScheme> selectListForUnique(@Param("schemeNo") String schemeNo,@Param("schemeName") String schemeName);

    List<ProvinceStageScheme> selectProvinceStageSchemeByIds(@Param("schemeIdList") List<String> schemeIdList);

    int deleteByIds(@Param("schemeIdList") List<String> schemeIdList);

    List<ProvinceStageScheme>selectProvinceStageSchemeList(ProvinceStageSchemeVO record);

    List<ProvinceStageScheme>selectAll();

    int batchInsertSelective(List<ProvinceStageScheme> list);

    void updateProvinceStageSchemeTimeBySchemeIds(@Param("schemeIdList") List<String> schemeIdList, @Param("validTime") Date validTime, @Param("invalidTime") Date invalidTime);



}