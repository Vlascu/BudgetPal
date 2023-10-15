package com.example.budgetpal.data_models;

import java.math.BigDecimal;

public class BudgetModel {

    private int image;
    private BigDecimal currentProgress, maxBudget;
    private String categoryName;

    public BudgetModel(int image, BigDecimal currentProgress, String categoryName, BigDecimal maxBudget) {
        this.image = image;
        this.currentProgress = new BigDecimal(currentProgress.toString());
        this.categoryName = categoryName;
        this.maxBudget = new BigDecimal(maxBudget.toString());
    }

    public BigDecimal getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(BigDecimal maxBudget) {
        this.maxBudget = new BigDecimal(maxBudget.toString());
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public BigDecimal getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(BigDecimal currentProgress) {
        this.currentProgress = new BigDecimal(currentProgress.toString());
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
