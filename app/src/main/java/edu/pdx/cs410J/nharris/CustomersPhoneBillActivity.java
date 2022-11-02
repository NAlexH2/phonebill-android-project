package edu.pdx.cs410J.nharris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import edu.pdx.cs410J.ParserException;

public class CustomersPhoneBillActivity extends AppCompatActivity {
  private String customerName;

  // File path of app
  private File path;
  // File name of current customer
  private File fileToAccess;

  // This customers bill, with each of the calls specific information for all calls
  private PhoneBill bill;

  // Hint to the user there are currently no calls for this customer
  private TextView emptyCallListAlert;

  // Call list for current customer
  private TextView callsList;

  // Hint if there are calls in the list, and how it is sorted
  private TextView callsExplained;

  private String searchStartDateTime = "";
  private String searchEndDateTime = "";
  private Button searchButton;

  public final static int REQUEST_CODE_RESULT_ADD = 1;
  public final static int REQUEST_CODE_RESULT_SEARCH = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Load any potentially saved instance from last visit
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_phone_bill);
    // Get the apps directory for storing information
    this.path = getFilesDir();

    // Connect several class objects to their widget on the UI
    this.emptyCallListAlert = findViewById(R.id.empty_calls_list_alert);
    this.callsList = findViewById(R.id.calls_list);
    this.callsExplained = findViewById(R.id.calls_explained);
    this.searchButton = findViewById(R.id.search_call);

    // Turn on scrolling for the calls list
    this.callsList.setMovementMethod(new ScrollingMovementMethod());

    // Get the current intent (the activity the user is on, this one, right here)
    Intent intent = getIntent();
    // Then store whatever extras we saved (from the CustomerManagerActivity, customer name)
    Bundle intentsExtras = intent.getExtras();
    if(intentsExtras != null) {
      // Using the established key, get that data store it here.
      this.customerName = intentsExtras.getString("custInfo");
    }

    // Some more file connection setup
    this.fileToAccess = new File(this.path,this.customerName + ".txt");

    // Create an independent PhoneBill that will be loaded each time the user makes a new call to
    // to add to the file.
    this.bill = new PhoneBill(this.customerName);

    // Set the actionBar (place with title) to customer name
    Objects.requireNonNull(getSupportActionBar()).setTitle("Customer - "
        + this.customerName);

    // Show/hide specific elements if current customer either has calls or not
    // by looking at the actual line length of the file
    if(this.fileToAccess.length() != 0) {
      // If there are calls to show...
      // Show hint if calls are present and what to do
      this.callsExplained.setVisibility(View.VISIBLE);
      // Completely "remove" the hint to make room for the list of calls
      this.emptyCallListAlert.setVisibility(View.GONE);
      // Turn on the search button!
      this.searchButton.setClickable(true);
      this.searchButton.getBackground().setAlpha(255);
      // Load up all calls from the current file!
      pullCurrentCustomerCalls(getCurrentFocus());
    }
    else {
      // If there are no calls to show...
      // Disable search. Can't search nothing if there's nothing to search
      this.searchButton.setClickable(false);
      this.searchButton.getBackground().setAlpha(100);

      // Set the hint to visible for the user
      this.emptyCallListAlert.setVisibility(View.VISIBLE);
      // Hide the hint if calls are present
      this.callsExplained.setVisibility(View.INVISIBLE);
    }
  }

  /**
   * Takes user to a new activity to show search params to the user
   * @param view - The current activity
   */
  public void searchCallsByDate(View view) {
    Intent intent = new Intent(this, SearchCallsActivity.class);
    startActivityForResult(intent, REQUEST_CODE_RESULT_SEARCH);
  }


  /**
   * The current customer selected has calls to show off
   * @param view - The current activity
   */
  public void pullCurrentCustomerCalls(View view) {
    // All customer files are just name.txt
    String fileName = this.customerName + ".txt";
    // the path of the apps folder and the specific file we want to look at
    File toPull = new File(this.path, fileName);
    String assignedToCallList = "";

    // If the customer has stuff in the file...
    if(toPull.length() != 0) {
      try {
        // Lets parse it!
        // Create a new TextParser from a FileReader using the toPull created above.
        // TextParser is the class that will pull data from a file. Just go look!
        TextParser parser = new TextParser(new FileReader(toPull));
        this.bill = parser.parse();
      } catch (IOException | ParserException ex) {
        // If parse failed, show message to user
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
      }
      int i = 1; // List calls with numbers using this int below.
      for (PhoneCall call : bill.getPhoneCalls()) { // Here
        assignedToCallList = assignedToCallList.concat(i + ": \nCaller: " + call.getCaller() + "\nNumber Dialed: "
            + call.getCallee() + "\nStart Time: " + call.getBeginTimeString()
            + "\nEnd Time: " + call.getEndTimeString() + "\nCall Length: "
            + call.getCallDuration() + " minutes.\n\n");
        ++i; // Increment to keep on counting!
      }
      // Set the text for the widget to the large string of assignedToCallList
      this.callsList.setText(assignedToCallList);
    }
  }

  /**
   * Directs the user to the activity to add a new call to the current customer. Activated by
   * the Add button
   * @param view - Current activity
   */
  public void pushCallToCurrentCustomer(View view) {
    Intent intent = new Intent(this, CallDetailsActivity.class);
    intent.putExtra("custName",this.customerName);
    startActivityForResult(intent, REQUEST_CODE_RESULT_ADD);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == REQUEST_CODE_RESULT_ADD && resultCode == RESULT_OK && data != null) {
      PhoneCall freshCall = (PhoneCall)data.getExtras().getSerializable("call");
      this.bill.addPhoneCall(freshCall);
      writeCurrentCallsToFile();
    }
    if(requestCode == REQUEST_CODE_RESULT_SEARCH && resultCode == RESULT_OK && data != null) {
      String[] range = (String[])data.getExtras().getSerializable("searchInfo");
      this.searchStartDateTime = range[0] + " " + range[1] + " " + range[2];
      this.searchEndDateTime = range[3] + " " + range[4] + " " + range[5];
      loadCallsByDateOnly();

    }
  }

  public void loadCallsByDateOnly() {
    pullCurrentCustomerCalls(getCurrentFocus());
    String fileName = this.customerName + ".txt";
    File toPull = new File(this.path, fileName);
    String assignedToCallList = "";
    if(toPull.length() != 0) {
      try {
        TextParser parser = new TextParser(new FileReader(toPull));
        this.bill = parser.parseByDateRange(this.bill, this.searchStartDateTime, this.searchEndDateTime);
      } catch (IOException ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
      }
      int i = 1;
      for (PhoneCall call : bill.getPhoneCalls()) {
        assignedToCallList = assignedToCallList.concat(i + ": \nCaller: " + call.getCaller() + "\nNumber Dialed: "
            + call.getCallee() + "\nStart Time: " + call.getBeginTimeString()
            + "\nEnd Time: " + call.getEndTimeString() + "\nCall Length: "
            + call.getCallDuration() + " minutes.\n\n");
        ++i;
      }
      this.callsList.setText(assignedToCallList);
    }
  }

  private void writeCurrentCallsToFile() {
    TextDumper dumper;
    try {
      dumper = new TextDumper(new FileWriter(this.fileToAccess, true));
    } catch (IOException ex) {
      Toast.makeText(this, "Failed to dump to file. Sorry.", Toast.LENGTH_LONG).show();
      return;
    }
    dumper.dump(this.bill);
    pullCurrentCustomerCalls(getCurrentFocus());
    areThereCalls();
  }

  private void areThereCalls() {
    if(this.callsList.getText().length() == 0) {
      this.emptyCallListAlert.setVisibility(View.VISIBLE);
      this.callsExplained.setVisibility(View.INVISIBLE);
      this.searchButton.setClickable(false);
      this.searchButton.getBackground().setAlpha(100);
    }
    else {
      this.emptyCallListAlert.setVisibility(View.GONE);
      this.callsExplained.setVisibility(View.VISIBLE);
      this.searchButton.setClickable(true);
      this.searchButton.getBackground().setAlpha(255);
    }
  }
}