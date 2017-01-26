package com.gyh.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        TextView usernameText = (TextView) findViewById(R.id.username);
        TextView passwordText = (TextView) findViewById(R.id.password);
        usernameText.setText(username);
        passwordText.setText(password);
    }
}
