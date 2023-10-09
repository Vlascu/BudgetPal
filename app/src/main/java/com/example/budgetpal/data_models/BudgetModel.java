package com.example.budgetpal.data_models;

import java.math.BigDecimal;

public class BudgetModel {

    private int image;
    private BigDecimal current_progress, max_budget;
    private String category_name;

    public BudgetModel(int image, BigDecimal current_progress, String category_name, BigDecimal max_budget) {
        this.image = image;
        this.current_progress = new BigDecimal(current_progress.toString());
        this.category_name = category_name;
        this.max_budget = new BigDecimal(max_budget.toString());
    }

    public BigDecimal getMax_budget() {
        return max_budget;
    }

    public void setMax_budget(BigDecimal max_budget) {
        this.max_budget = new BigDecimal(max_budget.toString());
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public BigDecimal getCurrent_progress() {
        return current_progress;
    }

    public void setCurrent_progress(BigDecimal current_progress) {
        this.current_progress = new BigDecimal(current_progress.toString());
    }


    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
