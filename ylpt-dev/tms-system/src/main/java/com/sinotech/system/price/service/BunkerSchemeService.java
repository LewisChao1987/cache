package com.sinotech.system.price.service;

import com.sinotech.system.price.model.BunkerScheme;
import com.sinotech.system.price.model.BunkerSchemeVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface BunkerSchemeService {
    /**
     * @Description: 新增燃油附加费方案
     * @param: BunkerScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     **/
    void addBunkerScheme(BunkerScheme bunkerScheme, String jsonStr);

    /**
     * @Description:修改燃油附加费方案
     * @param:BunkerScheme jsonStr
     * @Author: szq
     * @date: 2022/3/16
     **/
    void editBunkerScheme(BunkerScheme bunkerScheme, String jsonStr);

    /**
     * @Description: 删除燃油附加费方案
     * @param:schemeId
     * @Author: szq
     * @date: 2022/3/16
     **/
    void deleteBunkerScheme(String schemeId);

    /**
     * @Description:查询单个方案
     * @param:schemeId
     * @return: BunkerScheme
     * @Author: szq
     * @date: 2022/3/16
     **/
    BunkerSchemeVO selectBunkerScheme(String schemeId);

    /**
     * @Description: 查询列表
     * @param: param
     * @Author: szq
     * @date: 2022/3/16
     **/
    List<BunkerSchemeVO> selectBunkerSchemeList(BunkerSchemeVO param);

    /**
     * @Description: 导出列表
     * @param:param
     * @return: ResponseEntity
     * @Author: szq
     * @date: 2022/3/16
     **/
    ResponseEntity exportBunkerScheme(BunkerSchemeVO param);

    /**
     * @Description: 导入
     * @param:fileName file
     * @Author: szq
     * @date: 2022/3/16
     **/
    void importBunkerScheme(String fileName, MultipartFile file);

    /**
     * 批量修改生效时间
     *
     * @param schemeIds 方案ids
     * @param validTime 生效时间
     */
    void updateBunkerSchemeValidTimeBySchemeIds(String schemeIds, Date validTime);

    /**
     * 批量修改失效时间
     *
     * @param schemeIds   方案ids
     * @param invalidTime 失效时间
     */
    void updateBunkerSchemeInvalidTimeBySchemeIds(String schemeIds, Date invalidTime);
}
