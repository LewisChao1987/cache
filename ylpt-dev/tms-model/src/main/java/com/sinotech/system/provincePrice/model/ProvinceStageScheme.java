package com.sinotech.system.provincePrice.model;

import java.util.Date;

/**
 * 描述:ba_province_stage_scheme表的实体类
 * @version
 * @author:  szq
 * @创建时间: 2022-03-15
 */
public class ProvinceStageScheme {
    /**
     * 方案id
     */
    private String schemeId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 方案编号
     */
    private String schemeNo;

    /**
     * 方案名称
     */
    private String schemeName;

    /**
     * 始发区域id
     */
    private String billAreaId;

    /**
     * 始发区域编号
     */
    private String billAreaNo;

    /**
     * 始发区域名称
     */
    private String billAreaName;

    /**
     * 到达区域id
     */
    private String discAreaId;

    /**
     * 到达区域编号
     */
    private String discAreaNo;

    /**
     * 到达区域名称
     */
    private String discAreaName;

    /**
     * 货物类型id
     */
    private String itemTypeId;

    /**
     * 货物类型
     */
    private String itemType;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 服务类型
     */
    private String serviceLevel;

    /**
     * 单票价格
     */
    private Double orderPrice;


    /**
     * 生效日期
     */
    private Date validTime;

    /**
     * 失效日期
     */
    private Date invalidTime;

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
     * 始发区域类型
     */
    private String billAreaType;

    /**
     * 到达区域类型
     */
    private String discAreaType;


    private String deleteStatus;

    private String schemeStatus;

    public String getSchemeStatus() {
        return schemeStatus;
    }

    public void setSchemeStatus(String schemeStatus) {
        this.schemeStatus = schemeStatus;
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
     * 公司名称
     * @return company_name 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 公司名称
     * @param companyName 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    /**
     * 方案编号
     * @return scheme_no 方案编号
     */
    public String getSchemeNo() {
        return schemeNo;
    }

    /**
     * 方案编号
     * @param schemeNo 方案编号
     */
    public void setSchemeNo(String schemeNo) {
        this.schemeNo = schemeNo == null ? null : schemeNo.trim();
    }

    /**
     * 方案名称
     * @return scheme_name 方案名称
     */
    public String getSchemeName() {
        return schemeName;
    }

    /**
     * 方案名称
     * @param schemeName 方案名称
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName == null ? null : schemeName.trim();
    }

    /**
     * 始发区域id
     * @return bill_area_id 始发区域id
     */
    public String getBillAreaId() {
        return billAreaId;
    }

    /**
     * 始发区域id
     * @param billAreaId 始发区域id
     */
    public void setBillAreaId(String billAreaId) {
        this.billAreaId = billAreaId == null ? null : billAreaId.trim();
    }

    /**
     * 始发区域编号
     * @return bill_area_no 始发区域编号
     */
    public String getBillAreaNo() {
        return billAreaNo;
    }

    /**
     * 始发区域编号
     * @param billAreaNo 始发区域编号
     */
    public void setBillAreaNo(String billAreaNo) {
        this.billAreaNo = billAreaNo == null ? null : billAreaNo.trim();
    }

    /**
     * 始发区域名称
     * @return bill_area_name 始发区域名称
     */
    public String getBillAreaName() {
        return billAreaName;
    }

    /**
     * 始发区域名称
     * @param billAreaName 始发区域名称
     */
    public void setBillAreaName(String billAreaName) {
        this.billAreaName = billAreaName == null ? null : billAreaName.trim();
    }

    /**
     * 到达区域id
     * @return disc_area_id 到达区域id
     */
    public String getDiscAreaId() {
        return discAreaId;
    }

    /**
     * 到达区域id
     * @param discAreaId 到达区域id
     */
    public void setDiscAreaId(String discAreaId) {
        this.discAreaId = discAreaId == null ? null : discAreaId.trim();
    }

    /**
     * 到达区域编号
     * @return disc_area_no 到达区域编号
     */
    public String getDiscAreaNo() {
        return discAreaNo;
    }

    /**
     * 到达区域编号
     * @param discAreaNo 到达区域编号
     */
    public void setDiscAreaNo(String discAreaNo) {
        this.discAreaNo = discAreaNo == null ? null : discAreaNo.trim();
    }

    /**
     * 到达区域名称
     * @return disc_area_name 到达区域名称
     */
    public String getDiscAreaName() {
        return discAreaName;
    }

    /**
     * 到达区域名称
     * @param discAreaName 到达区域名称
     */
    public void setDiscAreaName(String discAreaName) {
        this.discAreaName = discAreaName == null ? null : discAreaName.trim();
    }

    /**
     * 货物类型id
     * @return item_type_id 货物类型id
     */
    public String getItemTypeId() {
        return itemTypeId;
    }

    /**
     * 货物类型id
     * @param itemTypeId 货物类型id
     */
    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId == null ? null : itemTypeId.trim();
    }

    /**
     * 货物类型
     * @return item_type 货物类型
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * 货物类型
     * @param itemType 货物类型
     */
    public void setItemType(String itemType) {
        this.itemType = itemType == null ? null : itemType.trim();
    }

    /**
     * 产品类型
     * @return product_type 产品类型
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 产品类型
     * @param productType 产品类型
     */
    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    /**
     * 服务类型
     * @return service_level 服务类型
     */
    public String getServiceLevel() {
        return serviceLevel;
    }

    /**
     * 服务类型
     * @param serviceLevel 服务类型
     */
    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel == null ? null : serviceLevel.trim();
    }

    /**
     * 单票价格
     * @return order_price 单票价格
     */
    public Double getOrderPrice() {
        return orderPrice;
    }

    /**
     * 单票价格
     * @param orderPrice 单票价格
     */
    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }


    /**
     * 生效日期
     * @return valid_time 生效日期
     */
    public Date getValidTime() {
        return validTime;
    }

    /**
     * 生效日期
     * @param validTime 生效日期
     */
    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    /**
     * 失效日期
     * @return invalid_time 失效日期
     */
    public Date getInvalidTime() {
        return invalidTime;
    }

    /**
     * 失效日期
     * @param invalidTime 失效日期
     */
    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
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
     * 始发区域类型
     * @return bill_area_type 始发区域类型
     */
    public String getBillAreaType() {
        return billAreaType;
    }

    /**
     * 始发区域类型
     * @param billAreaType 始发区域类型
     */
    public void setBillAreaType(String billAreaType) {
        this.billAreaType = billAreaType == null ? null : billAreaType.trim();
    }

    /**
     * 到达区域类型
     * @return disc_area_type 到达区域类型
     */
    public String getDiscAreaType() {
        return discAreaType;
    }

    /**
     * 到达区域类型
     * @param discAreaType 到达区域类型
     */
    public void setDiscAreaType(String discAreaType) {
        this.discAreaType = discAreaType == null ? null : discAreaType.trim();
    }
    /**
     * 删除状态
     * @return delete_status 删除状态
     */
    public String getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * 删除状态
     * @param deleteStatus 删除状态
     */
    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus == null ? null : deleteStatus.trim();
    }
}