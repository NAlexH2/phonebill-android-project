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


/**
 * This class is responsible for managing all the users names and the ability to go to their
 * bills to be modified.
 */
public class CustomerManagerActivity extends AppCompatActivity {

  // Button to add new customer to the database
  private Button addCustomerButton;
  private String customerName = "";
  // ArrayAdapter used to hold all customer names as a clickable button
  private ArrayAdapter<String> allCustomers;
  // Where each customer and their data is stored
  private File appPath;
  // Enables a scrolling view of the ArrayAdapter
  private ListView customerList;
  // Displays a message if there are no customers in the apps file location
  private TextView emptyListAlert;
  // Provide instructions on how to manage a customers actual bill once a customer has
  // been added
  private TextView customerManagerInstructions;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // load the activity that will display the customer list
    setContentView(R.layout.activity_customers);

    // get the directory of the app to be use
    this.appPath = getFilesDir();

    // connect the customer list adapter item on the display to the user, to display a loaded
    // list of customers in refreshCustomerList. This is a clickable item on the display to the
    // user
    this.allCustomers = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

    //Set the actionBar (place with title) to something else.
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.customers_activity_ab_title);

    // Connect the customerList, emptyListAlert and customerManagerInstructions ListViews
    // to the proper location on the UI. Doing this allows for hiding/showing the item on the UI
    // to the use.
    this.customerList = findViewById(R.id.customer_list);
    this.emptyListAlert = findViewById(R.id.empty_list_alert);
    this.customerManagerInstructions = findViewById(R.id.customer_manager_instructions);

    // Connects the code of adding a user to the database to the button on the UI enabling a
    // popup to be used to enter a customers name.
    this.addCustomerButton = findViewById(R.id.add_customer_button);

    // Load customer bills if any in app's data storage
    refreshCustomerList(allCustomers);
    // Set the customerList to the ArrayAdapter to generate and display information to the user
    this.customerList.setAdapter(allCustomers);
    areThereCustomers(); // Checks for customers and modifies various widgets to be displayed

    // Call these function super quick to simply establish the event listeners in the class
    // other wise the buttons must be tapped 2x to take effect.
    addCustomerShowPopup(getCurrentFocus());

    // We must call this BEFORE the user has an opportunity to interact with the activity.
    // This will establish the click listeners required for interaction
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
        if(file.createNewFile()) {
          Toast.makeText(getBaseContext(),
                  "Customer file created!", Toast.LENGTH_SHORT).show();
        }
      } catch (IOException ex) {
        Toast.makeText(getBaseContext(),
            "Unable to create file. Try again.", Toast.LENGTH_LONG).show();
      }
    }
    else Toast.makeText(getBaseContext(),
        "Customer already on file", Toast.LENGTH_LONG).show();
  }

  /**
   * Refreshes the <code>allCustomers</code> from the app's data folder. This function is only
   * called when the activity is first started, or a new customer is added through
   * <code></code>
   * @param allCustomerBills
   */
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

  /**
   * Checks to see if the <code>allCustomers</code> is empty or not and adjusts the widgets within
   * the activity to properly display hints on what to do next to the user.
   */
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