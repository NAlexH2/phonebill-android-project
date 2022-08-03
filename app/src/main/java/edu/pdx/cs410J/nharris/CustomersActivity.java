package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class CustomersActivity extends AppCompatActivity {

  private Button addCustomerButton;
  private String customerName = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customers);
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.customers_activity_ab_title);
    TextView textView = findViewById(R.id.empty_list_alert);
    addCustomerButton = findViewById(R.id.add_customer_button);

    //Call this function super quick to simply establish the event listener in the class.
    addCustomerShowPopup(getCurrentFocus());

    // TODO: 8/2/2022 figure out if the list is empty some how and then display this message
    // based on that.\

    if(1+1==2) {
      textView.setVisibility(View.VISIBLE);
    }

  }

  public void addCustomerShowPopup(View view) {
    addCustomerButton.setOnClickListener(view1 -> {
      //Simple popup alert box! Fantastic!
      AlertDialog.Builder customerNameAlertBox =
          new AlertDialog.Builder(CustomersActivity.this);
      customerNameAlertBox.setTitle("Customer Name");

      //Inserts a text field of some type determined....
      EditText cNameInput = new EditText(CustomersActivity.this);
      //HERE!
      cNameInput.setInputType(InputType.TYPE_CLASS_TEXT |
          InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      customerNameAlertBox.setView(cNameInput);
      customerNameAlertBox.setPositiveButton("Add Customer",
          (dialogInterface, i) -> {
            customerName = cNameInput.getText().toString();
            if(!customerName.isEmpty()) {

            }
            else
              Toast.makeText(getBaseContext(),
                  "Customer name cannot be empty!",
                  Toast.LENGTH_LONG).show();
          });

      customerNameAlertBox.setNegativeButton("Cancel",
          (dialogInterface, i) -> dialogInterface.cancel());

      customerNameAlertBox.show();
    });
  }

  void makeCustomerFile (String customerName) {

  }
}