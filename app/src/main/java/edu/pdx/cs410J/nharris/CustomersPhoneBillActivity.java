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
  private File path;
  private File fileToAccess;
  private PhoneBill bill;
  private TextView emptyCallListAlert;
  private TextView callsList;
  private TextView callsExplained;
  private String searchStartDateTime = "";
  private String searchEndDateTime = "";
  private Button searchButton;
  public final static int REQUEST_CODE_RESULT_ADD = 1;
  public final static int REQUEST_CODE_RESULT_SEARCH = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_phone_bill);
    this.path = getFilesDir();
    this.emptyCallListAlert = findViewById(R.id.empty_calls_list_alert);
    this.callsList = findViewById(R.id.calls_list);
    this.callsExplained = findViewById(R.id.calls_explained);
    this.searchButton = findViewById(R.id.search_call);
    this.callsList.setMovementMethod(new ScrollingMovementMethod());
    Intent intent = getIntent();
    Bundle intentsExtras = intent.getExtras();
    if(intentsExtras != null) {
      this.customerName = intentsExtras.getString("custInfo");
    }
    this.fileToAccess = new File(this.path,this.customerName + ".txt");
    this.bill = new PhoneBill(this.customerName);
    //Set the actionBar (place with title) to customer name
    Objects.requireNonNull(getSupportActionBar()).setTitle("Customer - "
        + this.customerName);
    if(this.fileToAccess.length() != 0) {
      this.callsExplained.setVisibility(View.VISIBLE);
      this.emptyCallListAlert.setVisibility(View.GONE);
      this.searchButton.setClickable(true);
      this.searchButton.getBackground().setAlpha(255);
      pullCurrentCustomerCalls(getCurrentFocus());
    }
    else {
      this.searchButton.setClickable(false);
      this.searchButton.getBackground().setAlpha(100);
      this.emptyCallListAlert.setVisibility(View.VISIBLE);
      this.callsExplained.setVisibility(View.INVISIBLE);
    }
  }

  public void searchCallsByDate(View view) {
    Intent intent = new Intent(this, SearchCallsActivity.class);
    startActivityForResult(intent, REQUEST_CODE_RESULT_SEARCH);
  }


  public void pullCurrentCustomerCalls(View view) {
    String fileName = this.customerName + ".txt";
    File toPull = new File(this.path, fileName);
    String assignedToCallList = "";
    if(toPull.length() != 0) {
      try {
        TextParser parser = new TextParser(new FileReader(toPull));
        this.bill = parser.parse();
      } catch (IOException | ParserException ex) {
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

  public void pushCallToCurrentCustomer(View view) {
    Intent intent = new Intent(this, CallInfoInput.class);
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