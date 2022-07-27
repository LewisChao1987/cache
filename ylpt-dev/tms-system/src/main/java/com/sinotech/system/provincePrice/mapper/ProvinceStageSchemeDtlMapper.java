package com.sinotech.system.provincePrice.mapper;


import com.sinotech.system.provincePrice.model.ProvinceStageSchemeDtl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProvinceStageSchemeDtlMapper {

    int deleteByPrimaryKey(String schemeDtlId);

    int insert(ProvinceStageSchemeDtl record);

    int insertSelective(ProvinceStageSchemeDtl record);

    ProvinceStageSchemeDtl selectByPrimaryKey(String schemeDtlId);

    int updateByPrimaryKeySelective(ProvinceStageSchemeDtl record);

    int updateByPrimaryKey(ProvinceStageSchemeDtl record);

    int batchInsertSelective(List<ProvinceStageSchemeDtl> list);

    int deleteBySchemeIdList(@Param("schemeIdList") List<String> schemeIdList);

    List<ProvinceStageSchemeDtl>selectStageSchemeDtlBySchemeId(@Param("schemeId") String schemeId);

    List<ProvinceStageSchemeDtl>selectStageSchemeDtlBySchemeIds(@Param("schemeIdList") List<String> schemeIdList);

}