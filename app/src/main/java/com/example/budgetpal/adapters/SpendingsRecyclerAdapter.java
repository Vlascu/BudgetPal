package com.example.budgetpal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.budgetpal.R;
import com.example.budgetpal.model.tables.SpendingsTable;
import com.example.budgetpal.view_models.SpendingsViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SpendingsRecyclerAdapter extends RecyclerView.Adapter<SpendingsRecyclerAdapter.MyViewHolder>{

    private final ArrayList<SpendingsTable> data;
    private final SpendingsViewModel spendingsViewModel;
    private final int year;
    private final int day;
    private final int userId;
    private final String month;

    private BigDecimal productValue;

    private String productName;

    private final LifecycleOwner lifecycleOwner;

    public SpendingsRecyclerAdapter(ArrayList<SpendingsTable> data, SpendingsViewModel spendingsViewModel, int year, int day, String month, int userId
    , LifecycleOwner lifecycleOwner) {
        this.data = data;
        this.spendingsViewModel = spendingsViewModel;
        this.year = year;
        this.day = day;
        this.month = month;
        this.userId = userId;
        this.lifecycleOwner = lifecycleOwner;
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
        productName =holder.item_name.getText().toString();
        productValue =new BigDecimal(holder.item_value.getText().toString().replace(",","."));
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
                   int adapterPosition = getAdapterPosition();
                   if(adapterPosition!=RecyclerView.NO_POSITION)
                   {
                        spendingsViewModel.deleteSpending(userId,day,month,year, productName);
                       LiveData<BigDecimal> totalMoney = spendingsViewModel.getTotalMoney(userId);
                       totalMoney.observe(lifecycleOwner, new Observer<BigDecimal>() {
                           @Override
                           public void onChanged(BigDecimal value) {
                                totalMoney.removeObserver(this);
                                spendingsViewModel.updateTotalMoneyInDB(userId,value.add(productValue));
                           }
                       });
                       notifyItemRemoved(adapterPosition);
                   }
                }
            });
        }
    }


}
