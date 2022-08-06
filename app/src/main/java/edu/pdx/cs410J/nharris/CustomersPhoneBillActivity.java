package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.Objects;

public class CustomersPhoneBillActivity extends AppCompatActivity {
    private Intent intent;
    private Bundle intentsExtras;
    private String customerName;
    private File path;
    private ArrayAdapter<PhoneCall> allCalls;
    private PhoneBill bill;
    private TextView emptyCallListAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_phone_bill);
        this.path = getFilesDir();
        this.emptyCallListAlert = findViewById(R.id.empty_calls_list_alert);
        this.intent = getIntent();
        this.intentsExtras = this.intent.getExtras();
        if(intentsExtras != null) {
            this.customerName = this.intentsExtras.getString("custInfo");
        }
        //Set the actionBar (place with title) to customer name
        Objects.requireNonNull(getSupportActionBar()).setTitle("Customer - "
            + this.customerName);
    }

    private void loadCurrentCustomerCalls() {
        String fileName = this.customerName + ".txt";
        File toPull = new File(path, fileName);
    }

    private void areThereCustomers() {
        if(this.allCalls.isEmpty()) {
            this.emptyCallListAlert.setVisibility(View.VISIBLE);
        }
        this.emptyCallListAlert.setVisibility(View.GONE);
    }
}