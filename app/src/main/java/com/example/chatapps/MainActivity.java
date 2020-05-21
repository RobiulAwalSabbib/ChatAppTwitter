package com.example.chatapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {


    public void signupMethod(View view){

        final EditText usernameET = findViewById(R.id.usernameET);
        final EditText passwordET = findViewById(R.id.passwordET);

        ParseUser.logInInBackground(usernameET.getText().toString(), passwordET.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    redirectUser();
                    Toast.makeText(getApplicationContext(),"Login Succesful",Toast.LENGTH_LONG).show();
                }
                else {

                    ParseUser parseUser = new ParseUser();
                    parseUser.setUsername(usernameET.getText().toString());
                    parseUser.setPassword(passwordET.getText().toString());
                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                redirectUser();
                                Toast.makeText(getApplicationContext(),"SignUp Succesful",Toast.LENGTH_LONG).show();

                            }else {

                                Toast.makeText(getApplicationContext(),e.getMessage().substring(e.getMessage().indexOf(" ")),Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
            }
        });

    }
    private void redirectUser(){

        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Twitter");

        redirectUser();


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
