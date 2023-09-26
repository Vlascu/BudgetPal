package com.example.budgetpal.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.budgetpal.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class AddDialog extends AppCompatDialogFragment {

    EditText name, amount;

    String name_text, category;

    BigDecimal value;

    View customDialog;

    Spinner category_spinner;

    AddDialogListener listener;
    int user_id, option;

    public AddDialog(int user_id, int option) {
        this.user_id = user_id;
        this.option = option;
    }

    public interface AddDialogListener {
        void onPositiveButtonClick(int user_id, String name, BigDecimal value, String category);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AddDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (option == 1) {
            customDialog = getLayoutInflater().inflate(R.layout.add_dialog, null);
            findGraphicalElements();
            changeHint(option);
            builder.setView(customDialog).setTitle("Add information").setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (name.getText().length() == 0 || amount.getText().length() == 0)
                        Toast.makeText(getContext(), "Insert all the data!", Toast.LENGTH_LONG).show();
                    else {
                        name_text = name.getText().toString();
                        value = new BigDecimal(amount.getText().toString());
                        listener.onPositiveButtonClick(user_id, name_text, value, "");
                    }
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else if (option == 2) {
            customDialog = getLayoutInflater().inflate(R.layout.spenings_add_dialog, null);
            findGraphicalElements();
            changeHint(option);
            fillSpinner();
            category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    category = (String) parent.getItemAtPosition(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            builder.setView(customDialog).setTitle("Add information").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (name.getText().length() == 0 || amount.getText().length() == 0)
                        Toast.makeText(getContext(), "Insert all the data!", Toast.LENGTH_LONG).show();
                    else {
                        name_text = name.getText().toString();
                        value = new BigDecimal(amount.getText().toString());
                        listener.onPositiveButtonClick(user_id, name_text, value, category);
                    }
                }
            });
        }
        return builder.create();
    }

    private void fillSpinner() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                getCategories());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(arrayAdapter);
    }

    private void findGraphicalElements() {
        if (option == 1) {
            name = customDialog.findViewById(R.id.addDialog_name);
            amount = customDialog.findViewById(R.id.addDialog_value);
        } else if (option == 2) {
            name = customDialog.findViewById(R.id.spending_addDialog_name);
            amount = customDialog.findViewById(R.id.spending_addDialog_value);
            category_spinner = customDialog.findViewById(R.id.addDialog_spinner);
        }
    }

    private ArrayList<String> getCategories() {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Restaurants");
        categories.add("Food");
        categories.add("Entertainment");
        categories.add("Transport");
        categories.add("Services");
        categories.add("Shopping");
        categories.add("Investments");
        return categories;
    }

    private void changeHint(int option) {
        switch (option) {
            case 1:
                name.setHint("Account name");
                break;
            case 2:
                name.setHint("Product name");
                break;
        }
    }
}
