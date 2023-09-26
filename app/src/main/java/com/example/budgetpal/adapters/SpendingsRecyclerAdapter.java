package com.example.budgetpal.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.R;
import com.example.budgetpal.data_models.SpendingsModel;
import com.example.budgetpal.model.tables.SpendingsTable;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SpendingsRecyclerAdapter extends RecyclerView.Adapter<SpendingsRecyclerAdapter.MyViewHolder>{

    private ArrayList<SpendingsTable> data;


    public SpendingsRecyclerAdapter(ArrayList<SpendingsTable> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewList = LayoutInflater.from(parent.getContext()).inflate(R.layout.spendings_recycler_view,parent,false);

        return new MyViewHolder(viewList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item_name.setText(data.get(position).getProduct_name());
        holder.item_value.setText(String.format("%.2f",data.get(position).getProduct_value()));
    }

    @Override
    public int getItemCount() {
        if(data!=null)
            return data.size();
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
       public TextView item_name,item_value;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.item_name = itemView.findViewById(R.id.spendings_item_name);
            this.item_value = itemView.findViewById(R.id.spendings_item_value);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo: delete method
                }
            });
        }
    }


}
