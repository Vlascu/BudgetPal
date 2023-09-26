package com.example.budgetpal.data_models;

import java.math.BigDecimal;

public class SpendingsModel {
    private String item_name;
    private BigDecimal item_value;

    public SpendingsModel(String item_name, BigDecimal item_value) {
        this.item_name = item_name;
        this.item_value = item_value;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public BigDecimal getItem_value() {
        return item_value;
    }

    public void setItem_value(BigDecimal item_value) {
        this.item_value = new BigDecimal(item_value.toString());
    }
}
