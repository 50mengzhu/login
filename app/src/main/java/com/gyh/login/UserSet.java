package com.gyh.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyh.login.db.User;

public class UserSet extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout mNamePart;
    private LinearLayout mIntroPart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set);

        mToolbar = (Toolbar) findViewById(R.id.set_toolbar);
        mToolbar.setTitle("个人资料");

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        User user = getIntent().getParcelableExtra("user");
        String name = user.getName();
        String intro = user.getIntro();

        TextView nameView = (TextView) findViewById(R.id.name);
        nameView.setText(name);
        TextView introView = (TextView) findViewById(R.id.intro);
        introView.setText(intro);

        mNamePart = (LinearLayout) findViewById(R.id.set_name);
        mIntroPart = (LinearLayout) findViewById(R.id.set_intro);

        mNamePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserSet.this, "设置昵称", Toast.LENGTH_SHORT).show();
            }
        });

        mIntroPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserSet.this, "设置个性签名", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
