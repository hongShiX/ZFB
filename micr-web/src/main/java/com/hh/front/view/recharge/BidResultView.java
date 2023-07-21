package com.hh.front.view.recharge;

import com.hh.api.model.BidInfo;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;

public class BidResultView {
    private Integer id;
    private String productName = "未知";
    private String bidTime = "-";
    private BigDecimal bidMoney;

    public BidResultView(BidInfo bidInfo) {
        this.id = bidInfo.getId();
        this.bidTime = DateFormatUtils.format(bidInfo.getBidTime(), "yyyy-MM-dd");
        this.bidMoney = bidInfo.getBidMoney();
        switch (bidInfo.getProdId()) {
            case 0:
                this.productName = "新手宝";
                break;
            case 1:
                this.productName = "优选";
                break;
            case 2:
                this.productName = "散标";
                break;
        }
    }


}
