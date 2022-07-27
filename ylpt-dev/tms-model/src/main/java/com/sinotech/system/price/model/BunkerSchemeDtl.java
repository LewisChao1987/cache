package com.sinotech.system.price.model;

import java.util.Date;

/**
 * 描述:ba_bunkers_scheme_dtl表的实体类
 * @version
 * @author:  szq
 * @创建时间: 2022-03-17
 */
public class BunkerSchemeDtl {
    /**
     * 方案明细id
     */
    private String schemeDtlId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 方案id
     */
    private String schemeId;

    /**
     * 数据类型 重量 体积 件数
     */
    private String dataType;

    /**
     * 首重/首方/首件运费
     */
    private Double firstFreight;

    /**
     * 首重/首方/首件
     */
    private Double firstStandard;

    /**
     * 重量/体积/件数下限
     */
    private Double kmLowerLimit;

    /**
     * 重量/体积/件数上限
     */
    private Double kmUpperLimit;

    /**
     * 重量/体积/件数下限
     */
    private Double lowerLimit;

    /**
     * 重量/体积/件数上限
     */
    private Double upperLimit;

    /**
     * 重量/体积/件数进位方式
     */
    private Double addType;

    /**
     * 增续重量/体积/件数价格
     */
    private Double continuePrice;

    /**
     * 单票上限
     */
    private Double maxPrice;

    /**
     * 新增用户
     */
    private String insUser;

    /**
     * 新增时间
     */
    private Date insTime;

    /**
     * 修改用户
     */
    private String updUser;

    /**
     * 修改时间
     */
    private Date updTime;

    /**
     * 1删除0正常
     */
    private String deleteStatus;

    /**
     * 方案明细id
     * @return scheme_dtl_id 方案明细id
     */
    public String getSchemeDtlId() {
        return schemeDtlId;
    }

    /**
     * 方案明细id
     * @param schemeDtlId 方案明细id
     */
    public void setSchemeDtlId(String schemeDtlId) {
        this.schemeDtlId = schemeDtlId == null ? null : schemeDtlId.trim();
    }

    /**
     * 租户id
     * @return tenant_id 租户id
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * 租户id
     * @param tenantId 租户id
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    /**
     * 公司id
     * @return company_id 公司id
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 公司id
     * @param companyId 公司id
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * 方案id
     * @return scheme_id 方案id
     */
    public String getSchemeId() {
        return schemeId;
    }

    /**
     * 方案id
     * @param schemeId 方案id
     */
    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId == null ? null : schemeId.trim();
    }

    /**
     * 数据类型 重量 体积 件数
     * @return data_type 数据类型 重量 体积 件数
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 数据类型 重量 体积 件数
     * @param dataType 数据类型 重量 体积 件数
     */
    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    /**
     * 首重/首方/首件运费
     * @return first_freight 首重/首方/首件运费
     */
    public Double getFirstFreight() {
        return firstFreight;
    }

    /**
     * 首重/首方/首件运费
     * @param firstFreight 首重/首方/首件运费
     */
    public void setFirstFreight(Double firstFreight) {
        this.firstFreight = firstFreight;
    }

    /**
     * 首重/首方/首件
     * @return first_standard 首重/首方/首件
     */
    public Double getFirstStandard() {
        return firstStandard;
    }

    /**
     * 首重/首方/首件
     * @param firstStandard 首重/首方/首件
     */
    public void setFirstStandard(Double firstStandard) {
        this.firstStandard = firstStandard;
    }

    /**
     * 重量/体积/件数下限
     * @return one_lower_limit 重量/体积/件数下限
     */
    public Double getKmLowerLimit() {
        return kmLowerLimit;
    }

    /**
     * 重量/体积/件数下限
     * @param kmLowerLimit 重量/体积/件数下限
     */
    public void setKmLowerLimit(Double kmLowerLimit) {
        this.kmLowerLimit = kmLowerLimit;
    }

    /**
     * 重量/体积/件数上限
     * @return one_upper_limit 重量/体积/件数上限
     */
    public Double getKmUpperLimit() {
        return kmUpperLimit;
    }

    /**
     * 重量/体积/件数上限
     * @param kmUpperLimit 重量/体积/件数上限
     */
    public void setKmUpperLimit(Double kmUpperLimit) {
        this.kmUpperLimit = kmUpperLimit;
    }

    /**
     * 重量/体积/件数下限
     * @return two_lower_limit 重量/体积/件数下限
     */
    public Double getLowerLimit() {
        return lowerLimit;
    }

    /**
     * 重量/体积/件数下限
     * @param lowerLimit 重量/体积/件数下限
     */
    public void setLowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * 重量/体积/件数上限
     * @return two_upper_limit 重量/体积/件数上限
     */
    public Double getUpperLimit() {
        return upperLimit;
    }

    /**
     * 重量/体积/件数上限
     * @param upperLimit 重量/体积/件数上限
     */
    public void setUpperLimit(Double upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * 重量/体积/件数进位方式
     * @return add_type 重量/体积/件数进位方式
     */
    public Double getAddType() {
        return addType;
    }

    /**
     * 重量/体积/件数进位方式
     * @param addType 重量/体积/件数进位方式
     */
    public void setAddType(Double addType) {
        this.addType = addType;
    }

    /**
     * 增续重量/体积/件数价格
     * @return continue_price 增续重量/体积/件数价格
     */
    public Double getContinuePrice() {
        return continuePrice;
    }

    /**
     * 增续重量/体积/件数价格
     * @param continuePrice 增续重量/体积/件数价格
     */
    public void setContinuePrice(Double continuePrice) {
        this.continuePrice = continuePrice;
    }

    /**
     * 单票上限
     * @return max_price 单票上限
     */
    public Double getMaxPrice() {
        return maxPrice;
    }

    /**
     * 单票上限
     * @param maxPrice 单票上限
     */
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    /**
     * 新增用户
     * @return ins_user 新增用户
     */
    public String getInsUser() {
        return insUser;
    }

    /**
     * 新增用户
     * @param insUser 新增用户
     */
    public void setInsUser(String insUser) {
        this.insUser = insUser == null ? null : insUser.trim();
    }

    /**
     * 新增时间
     * @return ins_time 新增时间
     */
    public Date getInsTime() {
        return insTime;
    }

    /**
     * 新增时间
     * @param insTime 新增时间
     */
    public void setInsTime(Date insTime) {
        this.insTime = insTime;
    }

    /**
     * 修改用户
     * @return upd_user 修改用户
     */
    public String getUpdUser() {
        return updUser;
    }

    /**
     * 修改用户
     * @param updUser 修改用户
     */
    public void setUpdUser(String updUser) {
        this.updUser = updUser == null ? null : updUser.trim();
    }

    /**
     * 修改时间
     * @return upd_time 修改时间
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * 修改时间
     * @param updTime 修改时间
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * 1删除0正常
     * @return delete_status 1删除0正常
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 1删除0正常
     * @param deleteStatus 1删除0正常
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }
}