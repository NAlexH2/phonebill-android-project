package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

/**
 * Class containing the necessary contents to display the readme to the user. Required as
 * part of the assignment.
 */
public class ReadmeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Jump straight into viewing the readme
    setContentView(R.layout.activity_readme);
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.readme_activity_ab_title);
  }
}