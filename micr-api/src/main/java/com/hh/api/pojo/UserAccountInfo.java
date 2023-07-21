package com.hh.api.pojo;

import com.hh.api.model.User;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserAccountInfo extends User implements Serializable {
    private BigDecimal availableMoney;

    public UserAccountInfo() {
    }

    public UserAccountInfo(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }

    public BigDecimal getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }
}
