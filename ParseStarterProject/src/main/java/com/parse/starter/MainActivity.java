/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    RelativeLayout relativeLayout;
    ImageView logoImage;
    EditText usernameField;
    EditText passwordField;
    TextView changeSignUpModeTextView;
    Button loginButton;

    Boolean signUpModeActive;


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signUpLabel){
            if(signUpModeActive == false){
                signUpModeActive = true;
                changeSignUpModeTextView.setText("Log In");
                loginButton.setText("Sign Up");
            } else {
                signUpModeActive = false;
                changeSignUpModeTextView.setText("Sign Up");
                loginButton.setText("Log In");
            }
        } else if (view.getId() == R.id.logoImage || view.getId() == R.id.relativeLayout){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void signUpOrLogin (View view) {

        if(signUpModeActive == true) {

            ParseUser user = new ParseUser();
            user.setUsername(usernameField.getText().toString());
            user.setPassword(passwordField.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("AppInfo", "SignUp Successful");
                        showUserList();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            ParseUser.logInInBackground(usernameField.getText().toString(), passwordField.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user != null){
                        Log.i("AppInfo", "LogIn Was Successful!");
                        showUserList();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void showUserList() {
        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      //Check if the user is already logged in first off
      if(ParseUser.getCurrentUser() != null){
        showUserList();
      }

      //Default to Log In
      signUpModeActive = false;

      //Set up our fields
      relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
      logoImage = (ImageView) findViewById(R.id.logoImage);
      usernameField = (EditText) findViewById(R.id.username);
      passwordField = (EditText) findViewById(R.id.password);
      changeSignUpModeTextView = (TextView) findViewById(R.id.signUpLabel);
      loginButton = (Button) findViewById(R.id.logInButton);

      //Add our onClick listeners
      changeSignUpModeTextView.setOnClickListener(this);
      logoImage.setOnClickListener(this);
      relativeLayout.setOnClickListener(this);

      usernameField.setOnKeyListener(this);
      passwordField.setOnKeyListener(this);

    ParseAnalytics.trackAppOpenedInBackground(getIntent());

  }

    //Login when users press enter
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {
            signUpOrLogin(view);
        }

        return false;
    }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
