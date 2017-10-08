package com.example.drakewin.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton;
         loginButton =(Button)findViewById(R.id.loginButton);

        //final SharedPreferences sharedPref = getSharedPreferences("pref_emails", Context.MODE_PRIVATE);
        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedPref.getString("DefaultEmail", "email@domain.com");

        final EditText etxt = (EditText)findViewById(R.id.editTextLogin);
        etxt.setText(sharedPref.getString("DefaultEmail", "no entry found"));


        // login button listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString ("DefaultEmail", etxt.getText().toString());
                editor.commit();

                Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });

    }
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}
