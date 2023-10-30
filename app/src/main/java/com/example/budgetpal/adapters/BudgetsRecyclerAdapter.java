package com.example.budgetpal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.R;
import com.example.budgetpal.data_models.BudgetModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BudgetsRecyclerAdapter extends RecyclerView.Adapter<BudgetsRecyclerAdapter.MyViewHolder> {

    private final ArrayList<BudgetModel> data;

    public BudgetsRecyclerAdapter(ArrayList<BudgetModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.budgets_recycler_view,parent,false);
        return new MyViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category_name.setText(data.get(position).getCategoryName());
        holder.percentage.setText(calculatePercentage(position));
        holder.image.setImageResource(data.get(position).getImage());
        holder.progressBar.setMax((data.get(position).getMaxBudget()).intValue());
        holder.progressBar.setProgress((data.get(position).getCurrentProgress()).intValue(),true);
    }

    private String calculatePercentage(int position) {
        BigDecimal currentProgress = data.get(position).getCurrentProgress();
        BigDecimal maxBudget = data.get(position).getMaxBudget();

        if (maxBudget.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal percentage = currentProgress.divide(maxBudget, 4, RoundingMode.HALF_UP);
            DecimalFormat decimalFormat = new DecimalFormat("0.0%");
            return decimalFormat.format(percentage);
        } else {
            return "0%";
        }
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        return 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView category_name, percentage;
        public ImageView image;
        public ProgressBar progressBar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name=itemView.findViewById(R.id.budget_category_name);
            percentage=itemView.findViewById(R.id.budgets_progress_percentage);
            image=itemView.findViewById(R.id.budget_category_image);
            progressBar=itemView.findViewById(R.id.budget_progressBar);
        }
    }
}
