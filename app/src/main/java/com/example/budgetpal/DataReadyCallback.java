package com.example.budgetpal;

import java.math.BigDecimal;
import java.util.List;

public interface DataReadyCallback {
    void onDataReady(BigDecimal revenuesSum, BigDecimal spendingsSum, List<String> dates);
}
