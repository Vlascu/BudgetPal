package com.example.budgetpal.model;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.math.BigDecimal;

public class BigDecimalDoubleTypeConverter {

    @TypeConverter
    public double bigDecimalToDouble(BigDecimal input) {
        return (input != null) ? input.doubleValue() : 0.0;
    }

    @TypeConverter
    public BigDecimal stringToBigDecimal(Double input) {
        if (input == null) return BigDecimal.ZERO;
        return BigDecimal.valueOf(input);
    }
}
