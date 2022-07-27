package com.sinotech.system.price.model;


import com.sinotech.logistics.operation.order.model.ItemInfo;
import net.sf.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @author ly
 * @title: StandardQuoteResultInfo
 * @projectName ylpt-cloud
 * @description: TODO
 * @date 2022/3/2116:19
 */
public class StandardQuoteResultInfo {


    private Double cost;
    private Double deliveryScheme;
    private Double selfPickupScheme;
    private Double platformScheme;
    private Double operatorScheme;
    private Double arriveTransferScheme;
    private Double dedicatedFee;
    private Double price;
    private Double disCountPrice;
    private Double freightSchemeLow;
    private List<ItemInfo> itemInfo;
    private String isBeforeItemPriceShf;
    private net.sf.json.JSONArray outDeliverSchemeArray;
    private net.sf.json.JSONArray selfPickupSchemeMap;
    private net.sf.json.JSONArray deliverySchemeArray;
    private Double amountOts13;
    private Double amountOts13Standard;
    private Double amountOts14;
    private Double amountOts14Standard;
    private Map<String, Object> deliverySchemeMap;
    private Double outDeliverScheme;
    private Double outDeliverSchemeStandard;
    private String truckTypeId;
    private Double amountSfzzfDj;
    private Double[] amountSfzzfJS;
    private Double departureTransScheme;
    private Double billOperatorScheme;
    private Double amountSfccfDj;
    private Double[] amountSfccfJS;
    private Double mainLineScheme;
    private Double amountGxfDj;
    private Double[] amountGxfJS;
    private Double tranMainLineScheme;
    private Double bqf;
    private Double zdf;
    private Double amountFullGang;
    private Double amountFullGangStandard;
    private Double[] newDedicatedScheme;
    private net.sf.json.JSONArray dedicatedFeeSchemeMap;
    private Double discOperatorScheme;
    private Double provincialTransitScheme;
    private Map<String, Object> descMap;
    private Map<String, Object> descDiscountMap;
    private Double mdcbPrice;
    private String amountScheme;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getDeliveryScheme() {
        return deliveryScheme;
    }

    public void setDeliveryScheme(Double deliveryScheme) {
        this.deliveryScheme = deliveryScheme;
    }

    public Double getSelfPickupScheme() {
        return selfPickupScheme;
    }

    public void setSelfPickupScheme(Double selfPickupScheme) {
        this.selfPickupScheme = selfPickupScheme;
    }

    public Double getPlatformScheme() {
        return platformScheme;
    }

    public void setPlatformScheme(Double platformScheme) {
        this.platformScheme = platformScheme;
    }

    public Double getOperatorScheme() {
        return operatorScheme;
    }

    public void setOperatorScheme(Double operatorScheme) {
        this.operatorScheme = operatorScheme;
    }

    public Double getArriveTransferScheme() {
        return arriveTransferScheme;
    }

    public void setArriveTransferScheme(Double arriveTransferScheme) {
        this.arriveTransferScheme = arriveTransferScheme;
    }

    public Double getDedicatedFee() {
        return dedicatedFee;
    }

    public void setDedicatedFee(Double dedicatedFee) {
        this.dedicatedFee = dedicatedFee;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDisCountPrice() {
        return disCountPrice;
    }

    public void setDisCountPrice(Double disCountPrice) {
        this.disCountPrice = disCountPrice;
    }

    public Double getFreightSchemeLow() {
        return freightSchemeLow;
    }

    public void setFreightSchemeLow(Double freightSchemeLow) {
        this.freightSchemeLow = freightSchemeLow;
    }

    public List<ItemInfo> getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(List<ItemInfo> itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getIsBeforeItemPriceShf() {
        return isBeforeItemPriceShf;
    }

    public void setIsBeforeItemPriceShf(String isBeforeItemPriceShf) {
        this.isBeforeItemPriceShf = isBeforeItemPriceShf;
    }

    public JSONArray getOutDeliverSchemeArray() {
        return outDeliverSchemeArray;
    }

    public void setOutDeliverSchemeArray(JSONArray outDeliverSchemeArray) {
        this.outDeliverSchemeArray = outDeliverSchemeArray;
    }

    public JSONArray getSelfPickupSchemeMap() {
        return selfPickupSchemeMap;
    }

    public void setSelfPickupSchemeMap(JSONArray selfPickupSchemeMap) {
        this.selfPickupSchemeMap = selfPickupSchemeMap;
    }

    public JSONArray getDeliverySchemeArray() {
        return deliverySchemeArray;
    }

    public void setDeliverySchemeArray(JSONArray deliverySchemeArray) {
        this.deliverySchemeArray = deliverySchemeArray;
    }

    public Double getAmountOts13() {
        return amountOts13;
    }

    public void setAmountOts13(Double amountOts13) {
        this.amountOts13 = amountOts13;
    }

    public Double getAmountOts13Standard() {
        return amountOts13Standard;
    }

    public void setAmountOts13Standard(Double amountOts13Standard) {
        this.amountOts13Standard = amountOts13Standard;
    }

    public Double getAmountOts14() {
        return amountOts14;
    }

    public void setAmountOts14(Double amountOts14) {
        this.amountOts14 = amountOts14;
    }

    public Double getAmountOts14Standard() {
        return amountOts14Standard;
    }

    public void setAmountOts14Standard(Double amountOts14Standard) {
        this.amountOts14Standard = amountOts14Standard;
    }

    public Map<String, Object> getDeliverySchemeMap() {
        return deliverySchemeMap;
    }

    public void setDeliverySchemeMap(Map<String, Object> deliverySchemeMap) {
        this.deliverySchemeMap = deliverySchemeMap;
    }

    public Double getOutDeliverScheme() {
        return outDeliverScheme;
    }

    public void setOutDeliverScheme(Double outDeliverScheme) {
        this.outDeliverScheme = outDeliverScheme;
    }

    public Double getOutDeliverSchemeStandard() {
        return outDeliverSchemeStandard;
    }

    public void setOutDeliverSchemeStandard(Double outDeliverSchemeStandard) {
        this.outDeliverSchemeStandard = outDeliverSchemeStandard;
    }

    public String getTruckTypeId() {
        return truckTypeId;
    }

    public void setTruckTypeId(String truckTypeId) {
        this.truckTypeId = truckTypeId;
    }

    public Double getAmountSfzzfDj() {
        return amountSfzzfDj;
    }

    public void setAmountSfzzfDj(Double amountSfzzfDj) {
        this.amountSfzzfDj = amountSfzzfDj;
    }

    public Double[] getAmountSfzzfJS() {
        return amountSfzzfJS;
    }

    public void setAmountSfzzfJS(Double[] amountSfzzfJS) {
        this.amountSfzzfJS = amountSfzzfJS;
    }

    public Double getDepartureTransScheme() {
        return departureTransScheme;
    }

    public void setDepartureTransScheme(Double departureTransScheme) {
        this.departureTransScheme = departureTransScheme;
    }

    public Double getBillOperatorScheme() {
        return billOperatorScheme;
    }

    public void setBillOperatorScheme(Double billOperatorScheme) {
        this.billOperatorScheme = billOperatorScheme;
    }

    public Double getAmountSfccfDj() {
        return amountSfccfDj;
    }

    public void setAmountSfccfDj(Double amountSfccfDj) {
        this.amountSfccfDj = amountSfccfDj;
    }

    public Double[] getAmountSfccfJS() {
        return amountSfccfJS;
    }

    public void setAmountSfccfJS(Double[] amountSfccfJS) {
        this.amountSfccfJS = amountSfccfJS;
    }

    public Double getMainLineScheme() {
        return mainLineScheme;
    }

    public void setMainLineScheme(Double mainLineScheme) {
        this.mainLineScheme = mainLineScheme;
    }

    public Double getAmountGxfDj() {
        return amountGxfDj;
    }

    public void setAmountGxfDj(Double amountGxfDj) {
        this.amountGxfDj = amountGxfDj;
    }

    public Double[] getAmountGxfJS() {
        return amountGxfJS;
    }

    public void setAmountGxfJS(Double[] amountGxfJS) {
        this.amountGxfJS = amountGxfJS;
    }

    public Double getTranMainLineScheme() {
        return tranMainLineScheme;
    }

    public void setTranMainLineScheme(Double tranMainLineScheme) {
        this.tranMainLineScheme = tranMainLineScheme;
    }

    public Double getBqf() {
        return bqf;
    }

    public void setBqf(Double bqf) {
        this.bqf = bqf;
    }

    public Double getZdf() {
        return zdf;
    }

    public void setZdf(Double zdf) {
        this.zdf = zdf;
    }

    public Double getAmountFullGang() {
        return amountFullGang;
    }

    public void setAmountFullGang(Double amountFullGang) {
        this.amountFullGang = amountFullGang;
    }

    public Double getAmountFullGangStandard() {
        return amountFullGangStandard;
    }

    public void setAmountFullGangStandard(Double amountFullGangStandard) {
        this.amountFullGangStandard = amountFullGangStandard;
    }

    public Double[] getNewDedicatedScheme() {
        return newDedicatedScheme;
    }

    public void setNewDedicatedScheme(Double[] newDedicatedScheme) {
        this.newDedicatedScheme = newDedicatedScheme;
    }

    public JSONArray getDedicatedFeeSchemeMap() {
        return dedicatedFeeSchemeMap;
    }

    public void setDedicatedFeeSchemeMap(JSONArray dedicatedFeeSchemeMap) {
        this.dedicatedFeeSchemeMap = dedicatedFeeSchemeMap;
    }

    public Double getDiscOperatorScheme() {
        return discOperatorScheme;
    }

    public void setDiscOperatorScheme(Double discOperatorScheme) {
        this.discOperatorScheme = discOperatorScheme;
    }

    public Double getProvincialTransitScheme() {
        return provincialTransitScheme;
    }

    public void setProvincialTransitScheme(Double provincialTransitScheme) {
        this.provincialTransitScheme = provincialTransitScheme;
    }

    public Map<String, Object> getDescMap() {
        return descMap;
    }

    public void setDescMap(Map<String, Object> descMap) {
        this.descMap = descMap;
    }

    public Map<String, Object> getDescDiscountMap() {
        return descDiscountMap;
    }

    public void setDescDiscountMap(Map<String, Object> descDiscountMap) {
        this.descDiscountMap = descDiscountMap;
    }

    public Double getMdcbPrice() {
        return mdcbPrice;
    }

    public void setMdcbPrice(Double mdcbPrice) {
        this.mdcbPrice = mdcbPrice;
    }

    public String getAmountScheme() {
        return amountScheme;
    }

    public void setAmountScheme(String amountScheme) {
        this.amountScheme = amountScheme;
    }
}
