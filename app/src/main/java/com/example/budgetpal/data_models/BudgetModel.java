package com.example.budgetpal.data_models;

public class BudgetModel {

    private int image, current_progress;
    private String percentage, category_name;

    public BudgetModel(int image, int current_progress, String percentage, String category_name) {
        this.image = image;
        this.current_progress = current_progress;
        this.percentage = percentage;
        this.category_name = category_name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getCurrent_progress() {
        return current_progress;
    }

    public void setCurrent_progress(int current_progress) {
        this.current_progress = current_progress;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
