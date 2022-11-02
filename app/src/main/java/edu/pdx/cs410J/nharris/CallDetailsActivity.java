package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Objects;


// This class and activity was partitioned off to its own place as it's extensive and requires
// more work than originally expected
public class CallDetailsActivity extends AppCompatActivity {

  // Spinners for start time of day and end.
  private Spinner startToD, endToD;
  // Text boxes for each parameter for the call
  private EditText callerNumber, calleeNumber, startDate, startTime, endDate, endTime;

  private String customerName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_call_info_input);
    Objects.requireNonNull(getSupportActionBar()).setTitle("Adding Phone Call");
    Intent previousIntent = getIntent();
    Bundle intentsExtras = previousIntent.getExtras();

    // Get the customers names from the previous activity. Checking to avoid critical failure.
    // Shouldn't but could so why not. Costs nothing.
    if(intentsExtras != null) {
      this.customerName = intentsExtras.getString("custName");
    }

    ArrayAdapter<String> timeOfDaySpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    timeOfDaySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Give the spinner some things to pick from
    timeOfDaySpinner.addAll("AM","PM");
    this.callerNumber = findViewById(R.id.editTextPhone);
    this.calleeNumber = findViewById(R.id.editTextPhone2);
    this.startDate = findViewById(R.id.editTextDate);
    this.startTime = findViewById(R.id.editTextTime);
    this.startToD = findViewById(R.id.amPMSpinner);
    this.startToD.setAdapter(timeOfDaySpinner);
    this.endDate = findViewById(R.id.editTextDate2);
    this.endTime = findViewById(R.id.editTextTime2);
    this.endToD = findViewById(R.id.amPMSpinner2);
    this.endToD.setAdapter(timeOfDaySpinner);
  }

  // Once the user has entered data, and clicks submit.
  public void onSubmit(View view) {
    // Make an intent from the current intent
    Intent currentIntent = new Intent();
    // Parse all the data to these strings
    String callerNumber = this.callerNumber.getText().toString();
    String calleeNumber = this.calleeNumber.getText().toString();
    String startDate = this.startDate.getText().toString();
    String startTime = this.startTime.getText().toString();
    String startToD = this.startToD.getSelectedItem().toString();
    String endDate = this.endDate.getText().toString();
    String endTime = this.endTime.getText().toString();
    String endToD = this.endToD.getSelectedItem().toString();
    String combinedStart = startDate + " " + startTime + " " + startToD;
    String combinedEnd = endDate + " " + endTime + " " + endToD;
    String[] callInfo = new String[]{this.customerName, callerNumber, calleeNumber, startDate,
        startTime, startToD, endDate, endTime, endToD};

    // If the data is bad/wrong don't actually submit and just "return" to the activity.
    // Data isn't lost and various alerts are provided to the user to fix things
    if(!validation(callInfo))
      return;

    InformationParser ip = new InformationParser();
    String result;
    result = ip.dateTimeValidator(new PhoneCall(null), combinedStart, combinedEnd);
    // Weird and lazy exception handling
    if(!result.isEmpty()) {
      Toast.makeText(this, "Unable to add call. " + result, Toast.LENGTH_LONG)
          .show();
      return;
    }

    // Otherwise, we can make a new call and add it to the current intents extras to "send back"
    // to the previous activity!
    PhoneCall call = new PhoneCall(callInfo);
    currentIntent.putExtra("call", call);
    // set the result of the current activity
    setResult(RESULT_OK, currentIntent);
    // finish from here!
    finish();
  }

  /**
   * User has elected to not add any calls.
   * @param view - Current activity
   */
  public void onCancel (View view) {
    setResult(RESULT_CANCELED, null);
    finish();
  }

  /**
   * Validates all the information provided from the user to ensure it's not bad.
   * @param callInfo - Information provided by the user of the call they are trying to add to
   *                 a customer.
   * @return true if it passes all checks, false otherwise and make errors visible for each box
   * that needs to be fixed.
   */
  private boolean validation(String[] callInfo) {
    boolean areWeOkay = true;

    if(!callInfo[1].matches("^\\d{3}-\\d{3}-\\d{4}$")) {
      this.callerNumber.setError("Error. Caller must be the format of ###-###-####");
      areWeOkay = false;
    }
    else this.callerNumber.setError(null);

    if(!callInfo[2].matches("^\\d{3}-\\d{3}-\\d{4}|\\d{3}$")) {
      this.calleeNumber.setError("Error. Calle must be either ### or ###-###-####");
      areWeOkay = false;
    }
    else this.calleeNumber.setError(null);

    if(callInfo[3].isEmpty()) {
      this.startDate.setError("Error. Date is required. Format: mm/dd/yyyy");
      areWeOkay = false;
    }
    else this.startDate.setError(null);

    if(callInfo[4].isEmpty()) {
      this.startTime.setError("Error. Time is required. Format: hh:mm");
      areWeOkay = false;
    }
    else this.startTime.setError(null);

    if(callInfo[6].isEmpty()) {
      this.endDate.setError("Error. Date is required. Format: mm/dd/yyyy");
      areWeOkay = false;
    }
    else this.endDate.setError(null);

    if(callInfo[7].isEmpty()) {
      this.endTime.setError("Error. Time is required. Format: hh:mm");
      areWeOkay = false;
    }
    else this.endTime.setError(null);

    return areWeOkay;
  }
}