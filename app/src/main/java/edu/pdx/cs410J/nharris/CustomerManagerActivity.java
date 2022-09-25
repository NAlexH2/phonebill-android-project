package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class CustomerManagerActivity extends AppCompatActivity {

  private Button addCustomerButton;
  private String customerName = "";
  private ArrayAdapter<String> allCustomers;
  private File appPath;
  private ListView customerList;
  private TextView emptyListAlert;
  private TextView customerManagerInstructions;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customers);
    this.appPath = getFilesDir();
    this.allCustomers = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
    //Set the actionBar (place with title) to something else.
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.customers_activity_ab_title);
    this.customerList = findViewById(R.id.customer_list);
    this.emptyListAlert = findViewById(R.id.empty_list_alert);
    this.customerManagerInstructions = findViewById(R.id.customer_manager_instructions);

    this.addCustomerButton = findViewById(R.id.add_customer_button);

    //Load customer bills if any in app's data storage
    refreshCustomerList(allCustomers);
    this.customerList.setAdapter(allCustomers);
    areThereCustomers();

    //Call these function super quick to simply establish the event listeners in the class
    //other wise the buttons must be tapped 2x to take effect.
    addCustomerShowPopup(getCurrentFocus());
    onListViewClick(getCurrentFocus());

  }

  public void onListViewClick(View view) {
    this.customerList.setOnItemClickListener((parent, view1, position, id) -> {
      String selected_customer = (String) parent.getItemAtPosition(position);
      Intent intent = new Intent(getBaseContext(), CustomersPhoneBillActivity.class);
      intent.putExtra("custInfo", selected_customer);
      startActivity(intent);
    });
  }

  public void addCustomerShowPopup(View view) {
    this.addCustomerButton.setOnClickListener(view1 -> {
      //Simple popup alert box! Fantastic!
      AlertDialog.Builder customerNameAlertBox =
          new AlertDialog.Builder(CustomerManagerActivity.this);
      customerNameAlertBox.setTitle("Customer Name");
      customerNameAlertBox.setMessage("Invalid characters: \n<  >  :  \\  |  ?  *  \\\\  //");

      //Inserts a text field of some type determined....
      EditText cNameInput = new EditText(CustomerManagerActivity.this);
      //HERE!
      cNameInput.setInputType(InputType.TYPE_CLASS_TEXT |
          InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      customerNameAlertBox.setView(cNameInput);
      customerNameAlertBox.setPositiveButton("Add Customer",
          (dialogInterface, i) -> {
            this.customerName = cNameInput.getText().toString();
            if(!customerName.isEmpty()) {
              makeCustomerFile(this.customerName);
              refreshCustomerList(this.allCustomers);
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
    String[] invalidChars = new String[]{"<", ">", ":", "\"", "|", "?", "*", "\\\\", "//"};
    for (String i : invalidChars) {
      if (customerName.contains(i)) {
        Toast.makeText(getBaseContext(),
            "Customer name contains invalid characters!", Toast.LENGTH_LONG).show();
        return;
      }
    }
    String fileName = customerName + ".txt";
    File file = new File(this.appPath, fileName);
    if(!file.exists()) {
      try {
        file.createNewFile();
        Toast.makeText(getBaseContext(),
            "Customer file created!", Toast.LENGTH_SHORT).show();
      } catch (IOException ex) {
        Toast.makeText(getBaseContext(),
            "Unable to create file. Try again.", Toast.LENGTH_LONG).show();
      }
    }
    else Toast.makeText(getBaseContext(),
        "Customer already on file", Toast.LENGTH_LONG).show();
  }

  private void refreshCustomerList(ArrayAdapter<String> allCustomerBills) {
    if(Objects.requireNonNull(this.appPath.listFiles()).length != 0) {
      File[] files = Objects.requireNonNull(this.appPath.listFiles());
      String[] customerFileNames = new String[Objects.requireNonNull(this.appPath.list()).length];
      for (int i = 0; i < files.length; ++i) {
        customerFileNames[i] = FilenameUtils.removeExtension(files[i].getName());
        if(this.allCustomers.getPosition(customerFileNames[i]) == -1
            && !customerFileNames[i].contains("rList")) {
          this.allCustomers.add(customerFileNames[i]);
        }
      }
    }
    areThereCustomers();
  }

  private void areThereCustomers() {
    if(this.allCustomers.isEmpty()) {
      this.customerManagerInstructions.setVisibility(View.INVISIBLE);
      this.emptyListAlert.setVisibility(View.VISIBLE);
    }
    else {
      this.emptyListAlert.setVisibility(View.GONE);
      this.customerManagerInstructions.setVisibility(View.VISIBLE);
    }
  }
}