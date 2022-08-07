package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CallInfoInput extends AppCompatActivity {

  private Spinner startToD, endToD;
  private EditText callerNumber, calleeNumber, startDate, startTime, endDate, endTime;

  private ArrayAdapter<String> timeOfDaySpinner;
  private String customerName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_call_info_input);

    Intent previousIntent = getIntent();
    Bundle intentsExtras = previousIntent.getExtras();

    if(intentsExtras != null) {
      this.customerName = intentsExtras.getString("custName");
    }
    this.timeOfDaySpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    this.timeOfDaySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    this.timeOfDaySpinner.addAll("AM","PM");
    this.callerNumber = findViewById(R.id.editTextPhone);
    this.calleeNumber = findViewById(R.id.editTextPhone2);
    this.startDate = findViewById(R.id.editTextDate);
    this.startTime = findViewById(R.id.editTextTime);
    this.startToD = findViewById(R.id.amPMSpinner);
    this.startToD.setAdapter(this.timeOfDaySpinner);
    this.endDate = findViewById(R.id.editTextDate2);
    this.endTime = findViewById(R.id.editTextTime2);
    this.endToD = findViewById(R.id.amPMSpinner2);
    this.endToD.setAdapter(this.timeOfDaySpinner);


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
    PhoneCall call = new PhoneCall(callInfo);


    try {
      ip.validNumber(callerNumber, calleeNumber);
      ip.argIndexExists(callInfo);
      ip.dateTimeValidator(call, combinedStart, combinedEnd);
    } catch (InformationParser.CommandLineParserException ex) {
      Toast.makeText(this, "Unable to add call. " + ex.getMessage(), Toast.LENGTH_LONG)
          .show();
      setResult(RESULT_CANCELED, null);
    }

    currentIntent.putExtra("call", call);
    setResult(RESULT_OK, currentIntent);
    finish();
  }

  public void onCancel (View view) {
    setResult(RESULT_CANCELED, null);
    finish();
  }
}