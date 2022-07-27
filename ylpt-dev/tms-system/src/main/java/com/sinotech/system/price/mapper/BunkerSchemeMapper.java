package com.sinotech.system.price.mapper;


import com.sinotech.system.price.model.BunkerScheme;
import com.sinotech.system.price.model.BunkerSchemeVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BunkerSchemeMapper {
    int deleteByPrimaryKey(String schemeId);

    int insert(BunkerScheme record);

    int insertSelective(BunkerScheme record);

    BunkerScheme selectByPrimaryKey(String schemeId);

    int updateByPrimaryKeySelective(BunkerScheme record);

    int updateByPrimaryKey(BunkerScheme record);

    List<BunkerScheme> selectListForCheck(@Param("companyId") String companyId, @Param("schemeMode") String schemeMode, @Param("relationDeptId") String relationDeptId);

    List<BunkerScheme> selectListForUnique(@Param("schemeNo") String schemeNo, @Param("schemeName") String schemeName);

    List<BunkerScheme> selectBunkerSchemeByIds(@Param("schemeIdList") List<String> schemeIdList);

    int deleteByIds(@Param("schemeIdList") List<String> schemeIdList);

    List<BunkerScheme> selectBunkerSchemeList(BunkerSchemeVO bunkerScheme);

    void updateBunkerSchemeTimeBySchemeIds(@Param("schemeIdList") List<String> schemeIdList, @Param("validTime") Date validTime, @Param("invalidTime") Date invalidTime);

    List<BunkerScheme> selectAll();

    int batchInsertSelective(List<BunkerScheme> list);
}