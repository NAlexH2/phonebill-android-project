package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_ab_title);
  }

  public void sayHello(View view) {
    PhoneCall call = new PhoneCall();
    Toast.makeText(this, "This call is: " + call, Toast.LENGTH_SHORT).show();
  }

  public void readmeViewer(View view) {
    Intent intent = new Intent(this, ReadmeActivity.class);
    startActivity(intent);
  }

  public void customerViewer(View view) {
    Intent intent = new Intent(this, CustomersActivity.class);
    startActivity(intent);
  }
}