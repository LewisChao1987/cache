package com.sinotech.system.price.mapper;


import com.sinotech.system.price.model.BunkerSchemeDtl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BunkerSchemeDtlMapper {

    int deleteByPrimaryKey(String schemeDtlId);

    int insert(BunkerSchemeDtl record);

    int insertSelective(BunkerSchemeDtl record);

    BunkerSchemeDtl selectByPrimaryKey(String schemeDtlId);

    int updateByPrimaryKeySelective(BunkerSchemeDtl record);

    int updateByPrimaryKey(BunkerSchemeDtl record);

    int batchInsertSelective(List<BunkerSchemeDtl> list);

    int deleteBySchemeIdList(@Param("schemeIdList")List<String> schemeIdList);

    List<BunkerSchemeDtl> selectBunkerSchemeDtlBySchemeId(@Param("schemeId") String schemeId);

    List<BunkerSchemeDtl>selectBunkerSchemeDtlBySchemeIds(@Param("schemeIdList")List<String> schemeIdList);
}