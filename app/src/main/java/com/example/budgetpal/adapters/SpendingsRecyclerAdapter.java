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
import com.example.budgetpal.data_models.model.tables.SpendingsTable;
import com.example.budgetpal.view_models.SpendingsViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SpendingsRecyclerAdapter extends RecyclerView.Adapter<SpendingsRecyclerAdapter.MyViewHolder>{

    private ArrayList<SpendingsTable> data;
    private SpendingsViewModel spendingsViewModel;
    private int year, day, user_id;
    private String month;

    private BigDecimal product_value;

    private String product_name;

    private LifecycleOwner lifecycleOwner;

    public SpendingsRecyclerAdapter(ArrayList<SpendingsTable> data, SpendingsViewModel spendingsViewModel, int year, int day, String month, int user_id
    , LifecycleOwner lifecycleOwner) {
        this.data = data;
        this.spendingsViewModel = spendingsViewModel;
        this.year = year;
        this.day = day;
        this.month = month;
        this.user_id = user_id;
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
        product_name=holder.item_name.getText().toString();
        product_value=new BigDecimal(holder.item_value.getText().toString().replace(",","."));
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
                        spendingsViewModel.deleteSpending(user_id,day,month,year,product_name);
                       LiveData<BigDecimal> totalMoney = spendingsViewModel.getTotalMoney(user_id);
                       totalMoney.observe(lifecycleOwner, new Observer<BigDecimal>() {
                           @Override
                           public void onChanged(BigDecimal value) {
                                totalMoney.removeObserver(this);
                                spendingsViewModel.updateTotalMoneyInDB(user_id,value.add(product_value));
                           }
                       });
                       notifyItemRemoved(adapterPosition);
                   }
                }
            });
        }
    }


}
