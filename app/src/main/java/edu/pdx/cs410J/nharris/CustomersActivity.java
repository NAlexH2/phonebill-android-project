package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);
        TextView textView = findViewById(R.id.empty_list_alert);
        // TODO: 8/2/2022 figure out if the list is empty some how and then display this message
        // based on that.

        if(1+1==2) {
            textView.setVisibility(View.VISIBLE);
        }

    }

    public void addCustomer(View view) {
        Intent intent = new Intent(this, AddCustomerActivity.class);
        startActivity(intent);
    }
}