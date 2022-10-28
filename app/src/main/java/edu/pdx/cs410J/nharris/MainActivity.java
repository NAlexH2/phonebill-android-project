package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

/**
 * This class initializes the entire app, enabling all other activities to be accessed.
 * Nothing serious, nothing wild, super default interface from the template chosen.
 */
public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Load the "front-page" so to speak of the app
    setContentView(R.layout.activity_main);
    // Change the bar at the top to display more accurate information for the user and the activity
    // they are currently looking at
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.main_activity_ab_title);
  }

  public void readmeViewer(View view) {
    // As with all new activities within the app, we have to create an intent prior to going
    // to said activity
    Intent readme = new Intent(this, ReadmeActivity.class);
    // Goto the readme activity, displaying instructions to the user
    startActivity(readme);
  }

  public void gotoCustomerManagerActivity(View view) {
    // Rinse and repeat, new activity
    Intent customerManager = new Intent(this, CustomerManagerActivity.class);
    // Goto the primary management activity
    startActivity(customerManager);
  }
}