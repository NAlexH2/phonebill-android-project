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

/**
 * Loads and enables the activity to search customer calls based on date and time
 */
public class SearchCallsActivity extends AppCompatActivity {

  private Spinner startToD, endToD;
  private EditText startDate, startTime, endDate, endTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_calls);
    Objects.requireNonNull(getSupportActionBar()).setTitle("Searching Phone Calls");

    ArrayAdapter<String> timeOfDaySpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    timeOfDaySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    timeOfDaySpinner.addAll("AM","PM");
    this.startDate = findViewById(R.id.editTextDate3);
    this.startTime = findViewById(R.id.editTextTime3);
    this.startToD = findViewById(R.id.amPMSpinner3);
    this.startToD.setAdapter(timeOfDaySpinner);
    this.endDate = findViewById(R.id.editTextDate4);
    this.endTime = findViewById(R.id.editTextTime4);
    this.endToD = findViewById(R.id.amPMSpinner4);
    this.endToD.setAdapter(timeOfDaySpinner);
  }


  /**
   * Gathers date/time information neatly to provide back to the CustomersPhoneBillActivity as
   * a bundle of extras where CustomersPhoneBillActivity would execute the search
   * @param view - Current activity
   */
  public void onSubmit(View view) {
    Intent currentIntent = new Intent();
    String startDate = this.startDate.getText().toString();
    String startTime = this.startTime.getText().toString();
    String startToD = this.startToD.getSelectedItem().toString();
    String endDate = this.endDate.getText().toString();
    String endTime = this.endTime.getText().toString();
    String endToD = this.endToD.getSelectedItem().toString();
    String combinedStart = startDate + " " + startTime + " " + startToD;
    String combinedEnd = endDate + " " + endTime + " " + endToD;
    String[] searchInfo = new String[]{startDate, startTime, startToD, endDate, endTime, endToD};

    // "return" from this function to display base activity.
    if(!validation(searchInfo))
      return;

    // Crappy empty PhoneCall because dateTimeValidator requires one, better than re-writing code.
    // Probably a much smarter way to do this.
    PhoneCall call = new PhoneCall(null);
    InformationParser ip = new InformationParser();
    String result;
      result = ip.dateTimeValidator(call, combinedStart, combinedEnd);
      // If dateTimeValidator found an error show that error here. Lazy/weird exception handling.
      if(!result.isEmpty()) {
        Toast.makeText(this, "Bad date format: " + result, Toast.LENGTH_LONG)
            .show();
        return;
      }

    // Otherwise, bundle it into the current intents extras and finish with an ok result!
    currentIntent.putExtra("searchInfo", searchInfo);
    setResult(RESULT_OK, currentIntent);
    finish();
  }

  // User has chosen not to search, gets outta this activity.
  public void onCancel(View view) {
    setResult(RESULT_CANCELED, null);
    finish();
  }

  // Just checking formats to set alert errors on specific boxes within the search activity.
  private boolean validation(String[] callInfo) {
    boolean areWeOkay = true;

    if(callInfo[0].isEmpty()) {
      this.startDate.setError("Error. Date is required. Format: mm/dd/yyyy");
      areWeOkay = false;
    }
    else this.startDate.setError(null);

    if(callInfo[1].isEmpty()) {
      this.startTime.setError("Error. Time is required. Format: hh:mm");
      areWeOkay = false;
    }
    else this.startTime.setError(null);

    if(callInfo[3].isEmpty()) {
      this.endDate.setError("Error. Date is required. Format: mm/dd/yyyy");
      areWeOkay = false;
    }
    else this.endDate.setError(null);

    if(callInfo[4].isEmpty()) {
      this.endTime.setError("Error. Time is required. Format: hh:mm");
      areWeOkay = false;
    }
    else this.endTime.setError(null);

    return areWeOkay;
  }
}