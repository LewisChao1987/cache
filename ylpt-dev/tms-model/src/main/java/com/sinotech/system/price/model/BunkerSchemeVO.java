package com.sinotech.system.price.model;

import java.util.Date;
import java.util.List;

/**
 * 描述:ba_bunkers_scheme表的实体类
 * @version
 * @author:  szq
 * @创建时间: 2022-03-17
 */
public class BunkerSchemeVO {
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
     * 国网类型(一类方案、二类方案)
     */
    private String schemeMode;

    /**
     * 方案编号
     */
    private String schemeNo;

    /**
     * 方案名字
     */
    private String schemeName;

    /**
     * 所属分拨id
     */
    private String relationDeptId;

    /**
     * 所属分拨名称
     */
    private String relationDeptName;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 服务类型
     */
    private String serviceLevel;

    /**
     * 货物类型ID
     */
    private String itemTypeId;

    /**
     * 货物类型
     */
    private String itemType;

    /**
     * 生效日期
     */
    private Date validTime;

    /**
     * 失效日期
     */
    private Date invalidTime;

    /**
     * 单票价格
     */
    private Double orderPrice;

    /**
     * 1删除0正常
     */
    private String deleteStatus;

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

    private String schemeStatus;

    private String schemeStatusValue;

    private String schemeModeValue;

    private List<BunkerSchemeDtl>bunkerSchemeDtlList;

    public String getSchemeStatus() {
        return schemeStatus;
    }

    public void setSchemeStatus(String schemeStatus) {
        this.schemeStatus = schemeStatus;
    }

    public List<BunkerSchemeDtl> getBunkerSchemeDtlList() {
        return bunkerSchemeDtlList;
    }

    public void setBunkerSchemeDtlList(List<BunkerSchemeDtl> bunkerSchemeDtlList) {
        this.bunkerSchemeDtlList = bunkerSchemeDtlList;
    }

    public String getSchemeModeValue() {
        return schemeModeValue;
    }

    public void setSchemeModeValue(String schemeModeValue) {
        this.schemeModeValue = schemeModeValue;
    }


    public String getSchemeStatusValue() {
        return schemeStatusValue;
    }

    public void setSchemeStatusValue(String schemeStatusValue) {
        this.schemeStatusValue = schemeStatusValue;
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
     * 国网类型(一类方案、二类方案)
     * @return scheme_mode 国网类型(一类方案、二类方案)
     */
    public String getSchemeMode() {
        return schemeMode;
    }

    /**
     * 国网类型(一类方案、二类方案)
     * @param schemeMode 国网类型(一类方案、二类方案)
     */
    public void setSchemeMode(String schemeMode) {
        this.schemeMode = schemeMode == null ? null : schemeMode.trim();
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
     * 方案名字
     * @return scheme_name 方案名字
     */
    public String getSchemeName() {
        return schemeName;
    }

    /**
     * 方案名字
     * @param schemeName 方案名字
     */
    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName == null ? null : schemeName.trim();
    }

    /**
     * 所属分拨id
     * @return relation_dept_id 所属分拨id
     */
    public String getRelationDeptId() {
        return relationDeptId;
    }

    /**
     * 所属分拨id
     * @param relationDeptId 所属分拨id
     */
    public void setRelationDeptId(String relationDeptId) {
        this.relationDeptId = relationDeptId == null ? null : relationDeptId.trim();
    }

    /**
     * 所属分拨名称
     * @return relation_dept_name 所属分拨名称
     */
    public String getRelationDeptName() {
        return relationDeptName;
    }

    /**
     * 所属分拨名称
     * @param relationDeptName 所属分拨名称
     */
    public void setRelationDeptName(String relationDeptName) {
        this.relationDeptName = relationDeptName == null ? null : relationDeptName.trim();
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
     * 货物类型ID
     * @return item_type_id 货物类型ID
     */
    public String getItemTypeId() {
        return itemTypeId;
    }

    /**
     * 货物类型ID
     * @param itemTypeId 货物类型ID
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
}