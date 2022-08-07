package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchCallsActivity extends AppCompatActivity {

  private Spinner startToD, endToD;
  private EditText startDate, startTime, endDate, endTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_calls);
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

  public void onSubmit(View view) {
    Intent currentIntent = new Intent();
    String startDate = this.startDate.getText().toString();
    String startTime = this.startTime.getText().toString();
    String startToD = this.startToD.getSelectedItem().toString();
    String endDate = this.endDate.getText().toString();
    String endTime = this.endTime.getText().toString();
    String endToD = this.endToD.getSelectedItem().toString();
    InformationParser ip = new InformationParser();
    String combinedStart = startDate + " " + startTime + " " + startToD;
    String combinedEnd = endDate + " " + endTime + " " + endToD;
    String[] searchInfo = new String[]{startDate, startTime, startToD, endDate, endTime, endToD};
    PhoneCall call = new PhoneCall(null);
    try {
      ip.dateTimeValidator(call, combinedStart, combinedEnd);
    } catch (InformationParser.CommandLineParserException ex) {
      Toast.makeText(this, "Bad date format: " + ex.getMessage(), Toast.LENGTH_LONG)
          .show();
      setResult(RESULT_CANCELED, null);
    }

    currentIntent.putExtra("searchInfo", searchInfo);
    setResult(RESULT_OK, currentIntent);
    finish();
  }

  public void onCancel(View view) {
    setResult(RESULT_CANCELED, null);
    finish();
  }
}