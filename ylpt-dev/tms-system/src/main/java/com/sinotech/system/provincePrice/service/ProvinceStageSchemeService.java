package com.sinotech.system.provincePrice.service;

import com.sinotech.system.provincePrice.model.ProvinceStageScheme;
import com.sinotech.system.provincePrice.model.ProvinceStageSchemeVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface ProvinceStageSchemeService {
    /**
     * @Description: 新增省网全段运费方案
     * @param: provinceStageScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     **/
    void addProvinceStageScheme(ProvinceStageScheme provinceStageScheme, String jsonStr);

    /**
     * @Description:修改省网全段运费方案
     * @param:provinceStageScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     **/
    void editProvinceStageScheme(ProvinceStageScheme provinceStageScheme, String jsonStr);

    /**
     * @Description: 删除省网全段运费方案
     * @param:schemeId
     * @Author: szq
     * @date: 2022/3/16
     **/
    void deleteProvinceStageScheme(String schemeId);

    /**
     * @Description:查询单个方案
     * @param:schemeId
     * @return: ProvinceStageScheme
     * @Author: szq
     * @date: 2022/3/16
     **/
    ProvinceStageSchemeVO selectProvinceStageScheme(String schemeId);

    /**
     * @Description: 查询列表
     * @param: param
     * @Author: szq
     * @date: 2022/3/16
     **/
    List<ProvinceStageSchemeVO> selectProvinceStageSchemeList(ProvinceStageSchemeVO param);

    /**
     * @Description: 导出列表
     * @param:param
     * @return: ResponseEntity
     * @Author: szq
     * @date: 2022/3/16
     **/
    ResponseEntity exportProvinceStageScheme(ProvinceStageSchemeVO param);

    /**
     * @Description: 导入
     * @param:fileName file
     * @Author: szq
     * @date: 2022/3/16
     **/
    void importProvinceStageScheme(String fileName, MultipartFile file);

    /**
     * 批量修改生效时间
     *
     * @param schemeIds 方案ids
     * @param validTime 生效时间
     */
    void updateProvinceStageSchemeValidTimeBySchemeIds(String schemeIds, Date validTime);

    /**
     * 批量修改失效时间
     *
     * @param schemeIds   方案ids
     * @param invalidTime 失效时间
     */
    void updateProvinceStageSchemeInvalidTimeBySchemeIds(String schemeIds, Date invalidTime);
}
