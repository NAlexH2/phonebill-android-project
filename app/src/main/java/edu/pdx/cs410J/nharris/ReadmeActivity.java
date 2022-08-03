package edu.pdx.cs410J.nharris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Objects;

public class ReadmeActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_readme);
    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.readme_activity_ab_title);
  }
}