package com.gyh.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.gyh.login.EditText.ClearEditText;
import com.gyh.login.util.MarkersAdapter;

public class MapSet extends AppCompatActivity {

    private RecyclerView mMarkers;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_set);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("描述路线");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMarkers = (RecyclerView) findViewById(R.id.markers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMarkers.setLayoutManager(layoutManager);
        MarkersAdapter adapter = new MarkersAdapter(MapSetUp.markers);
        mMarkers.setAdapter(adapter);

        final ClearEditText title = (ClearEditText) findViewById(R.id.title_input);
        final ClearEditText intro = (ClearEditText) findViewById(R.id.intro_input);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("routeTitle", title.getText().toString());
                intent.putExtra("routeIntro", intro.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
