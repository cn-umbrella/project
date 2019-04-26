package com.inext.dto;

import lombok.Data;

/**
 * 口袋理财放款返回值包装类
 *
 * @author lisige
 */
@Data
public class KouDaiWithdrawResultDto {

    /**
     * 打款状态，0：申请成功；
     * 200001：今日放款金额超限；200002：放款订单重复；200003:放款项目不存在；200004：放款订单不存在；200005：暂无可放款通道；
     * 200006：请求参数异常；200007：失败订单重复发起不能超过24小时；200008：重复打款信息不一致；200009：单笔请求金额超限；
     * 200010：单笔请求金额超限(非招行最多5w)；200011：手续费大于提现金额；200012：单日次数超限>200001：今日放款金额超限；
     * 200002：放款订单重复；200003:放款项目不存在；200004：放款订单不存在；200005：暂无可放款通道；200006：请求参数异常；
     * 200007：失败订单重复发起不能超过24小时；200008：重复打款信息不一致；200009：单笔请求金额超限；200010：单笔请求金额超限(非招行最多5w)；
     * 200011：手续费大于提现金额；200012：单日次数超限，500：系统异常
     */
    private Integer code;

    /**
     * 申请结果描述
     */
    private String msg;

    /**
     * 第三方通道编号
     */
    private Integer thirdPlatform;

    /**
     * 口袋订单号
     */
    private String payOrderId;

    /**
     * 本次操作是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return this.code.intValue() == 0;
    }
}
