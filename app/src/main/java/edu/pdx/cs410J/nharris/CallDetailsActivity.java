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

public class CallDetailsActivity extends AppCompatActivity {

  private Spinner startToD, endToD;
  private EditText callerNumber, calleeNumber, startDate, startTime, endDate, endTime;

  private String customerName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_call_info_input);
    Objects.requireNonNull(getSupportActionBar()).setTitle("Adding Phone Call");
    Intent previousIntent = getIntent();
    Bundle intentsExtras = previousIntent.getExtras();

    if(intentsExtras != null) {
      this.customerName = intentsExtras.getString("custName");
    }
    ArrayAdapter<String> timeOfDaySpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    timeOfDaySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

  public void onSubmit(View view) {
    Intent currentIntent = new Intent();
    String callerNumber = this.callerNumber.getText().toString();
    String calleeNumber = this.calleeNumber.getText().toString();
    String startDate = this.startDate.getText().toString();
    String startTime = this.startTime.getText().toString();
    String startToD = this.startToD.getSelectedItem().toString();
    String endDate = this.endDate.getText().toString();
    String endTime = this.endTime.getText().toString();
    String endToD = this.endToD.getSelectedItem().toString();
    InformationParser ip = new InformationParser();
    String combinedStart = startDate + " " + startTime + " " + startToD;
    String combinedEnd = endDate + " " + endTime + " " + endToD;
    String[] callInfo = new String[]{this.customerName, callerNumber, calleeNumber, startDate,
        startTime, startToD, endDate, endTime, endToD};

    if(!validation(callInfo))
      return;

    String result;
    result = ip.dateTimeValidator(new PhoneCall(null), combinedStart, combinedEnd);
    if(!result.isEmpty()) {
      Toast.makeText(this, "Unable to add call. " + result, Toast.LENGTH_LONG)
          .show();
      return;
    }

    PhoneCall call = new PhoneCall(callInfo);
    currentIntent.putExtra("call", call);
    setResult(RESULT_OK, currentIntent);
    finish();
  }

  public void onCancel (View view) {
    setResult(RESULT_CANCELED, null);
    finish();
  }

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