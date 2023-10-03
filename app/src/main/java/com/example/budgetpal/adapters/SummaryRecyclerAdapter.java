package com.example.budgetpal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.R;
import com.example.budgetpal.data_models.SpendingsModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SummaryRecyclerAdapter extends RecyclerView.Adapter<SummaryRecyclerAdapter.MyViewHolder> {
    final private ArrayList<String> dates;
    final private ArrayList<BigDecimal> revenue_data, spendings_data;


    public SummaryRecyclerAdapter(ArrayList<String> dates, ArrayList<BigDecimal> revenue_data, ArrayList<BigDecimal> spendings_data) {
        this.dates = dates;
        this.revenue_data = revenue_data;
        this.spendings_data = spendings_data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_recycler_view, parent, false);
        return new MyViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.date_view.setText(dates.get(position));
        holder.revenue_view.setText(String.format("%.2f", revenue_data.get(position)));
        holder.spending_view.setText(String.format("%.2f", spendings_data.get(position)));
        holder.pieChart.setData(setPieValues(position));
    }

    private PieData setPieValues(int position) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(revenue_data.get(position).floatValue(), ""));
        pieEntries.add(new PieEntry(spendings_data.get(position).floatValue(), ""));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        return new PieData(pieDataSet);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date_view, revenue_view, spending_view;
        private PieChart pieChart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date_view = itemView.findViewById(R.id.summary_date);
            revenue_view = itemView.findViewById(R.id.summary_revenue_value);
            spending_view = itemView.findViewById(R.id.summary_spending_value);
            pieChart = itemView.findViewById(R.id.summary_pieChart);
        }
    }
}
