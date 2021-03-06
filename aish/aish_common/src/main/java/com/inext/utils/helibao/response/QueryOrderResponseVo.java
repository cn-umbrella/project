package com.inext.utils.helibao.response;

import java.util.List;
import java.util.Map;

import com.inext.utils.helibao.annotation.SignExclude;

/**
 * Created by heli50 on 2017/4/15.
 */
public class QueryOrderResponseVo {
    private String rt1_bizType;
    private String rt2_retCode;
    private String rt3_retMsg;
    private String rt4_customerNumber;
    private List<Map<String, Object>> rt5_bindCardList;
    
    @SignExclude
    private String sign;

	public String getRt1_bizType() {
		return rt1_bizType;
	}

	public void setRt1_bizType(String rt1_bizType) {
		this.rt1_bizType = rt1_bizType;
	}

	public String getRt2_retCode() {
		return rt2_retCode;
	}

	public void setRt2_retCode(String rt2_retCode) {
		this.rt2_retCode = rt2_retCode;
	}

	public String getRt3_retMsg() {
		return rt3_retMsg;
	}

	public void setRt3_retMsg(String rt3_retMsg) {
		this.rt3_retMsg = rt3_retMsg;
	}

	public String getRt4_customerNumber() {
		return rt4_customerNumber;
	}

	public void setRt4_customerNumber(String rt4_customerNumber) {
		this.rt4_customerNumber = rt4_customerNumber;
	}

	public List<Map<String, Object>> getRt5_bindCardList() {
		return rt5_bindCardList;
	}

	public void setRt5_bindCardList(List<Map<String, Object>> rt5_bindCardList) {
		this.rt5_bindCardList = rt5_bindCardList;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

    

}
