package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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


public class CustomersActivity extends AppCompatActivity {

  private Button addCustomerButton;
  private String customerName = "";
  private ArrayAdapter<String> allCustomerBills;
  private File appPath;
  ListView customerList;
  TextView empty_list_alert;
  TextView customer_manager_instructions;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customers);
    this.appPath = getFilesDir();
    this.allCustomerBills = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.customers_activity_ab_title);
    this.customerList = findViewById(R.id.customer_list);
    this.empty_list_alert = findViewById(R.id.empty_list_alert);
    this.customer_manager_instructions = findViewById(R.id.customer_manager_instructions);

    this.addCustomerButton = findViewById(R.id.add_customer_button);

    //Load customer bills if any in app's data storage
    refreshCustomerList(allCustomerBills);
    this.customerList.setAdapter(allCustomerBills);
    areThereCustomers();

    //Call these function super quick to simply establish the event listeners in the class
    //other wise the buttons must be tapped 2x to take effect.
    addCustomerShowPopup(getCurrentFocus());
    onListViewClick();

  }

  public void onListViewClick() {
    this.customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getBaseContext(),
                "ITEM WAS CLICKED WEEEW!!!!!", Toast.LENGTH_LONG).show();
      }
    });
  }

  public void addCustomerShowPopup(View view) {
    this.addCustomerButton.setOnClickListener(view1 -> {
      //Simple popup alert box! Fantastic!
      AlertDialog.Builder customerNameAlertBox =
          new AlertDialog.Builder(CustomersActivity.this);
      customerNameAlertBox.setTitle("Customer Name");
      customerNameAlertBox.setMessage("Invalid characters: \n<  >  :  \\  |  ?  *  \\\\  //");

      //Inserts a text field of some type determined....
      EditText cNameInput = new EditText(CustomersActivity.this);
      //HERE!
      cNameInput.setInputType(InputType.TYPE_CLASS_TEXT |
          InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
      customerNameAlertBox.setView(cNameInput);
      customerNameAlertBox.setPositiveButton("Add Customer",
          (dialogInterface, i) -> {
            this.customerName = cNameInput.getText().toString();
            if(!customerName.isEmpty()) {
              makeCustomerFile(this.customerName);
              refreshCustomerList(this.allCustomerBills);
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
            "Customer file created!", Toast.LENGTH_LONG).show();
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
        if(this.allCustomerBills.getPosition(customerFileNames[i]) == -1) {
          this.allCustomerBills.add(customerFileNames[i]);
        }
      }
    }
    areThereCustomers();
  }

  private void areThereCustomers() {
    if(this.allCustomerBills.isEmpty()) {
      this.customer_manager_instructions.setVisibility(View.INVISIBLE);
      this.empty_list_alert.setVisibility(View.VISIBLE);
    }
    else {
      this.empty_list_alert.setVisibility(View.GONE);
      this.customer_manager_instructions.setVisibility(View.VISIBLE);
    }
  }
}