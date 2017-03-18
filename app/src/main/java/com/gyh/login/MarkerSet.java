package com.gyh.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gyh.login.Stepper.MethodFragment;
import com.gyh.login.Stepper.OnCancelAction;
import com.gyh.login.Stepper.OnChangeStepAction;
import com.gyh.login.Stepper.OnFinishAction;
import com.gyh.login.Stepper.StepperItem;
import com.gyh.login.Stepper.SteppersView;
import com.gyh.login.Stepper.TitleFragment;

import java.util.ArrayList;

public class MarkerSet extends AppCompatActivity {

    private Toolbar mToolbar;

    public static String title = "";
    public static String intro = "";
    public static String method = "到达方式";
    public static String time = "预计时间";
    public static String tag = "";

    public void reset() {
        title = "";
        intro = "";
        method = "到达方式";
        time = "预计时间";
        tag = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_set);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("描述地点");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();

        SteppersView.Config steppersViewConfig = new SteppersView.Config();
        steppersViewConfig.setOnFinishAction(new OnFinishAction() {
            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("intro", intro);
                intent.putExtra("method", method);
                intent.putExtra("time", time);
                intent.putExtra("tag", tag);
                intent.putExtra("latitude", getIntent().getDoubleExtra("latitude", 0));
                intent.putExtra("longitude", getIntent().getDoubleExtra("longitude", 0));
                setResult(RESULT_OK, intent);
                reset();
                finish();
            }
        });

        steppersViewConfig.setOnCancelAction(new OnCancelAction() {
            @Override
            public void onCancel() {
            }
        });

        steppersViewConfig.setOnChangeStepAction(new OnChangeStepAction() {
            @Override
            public void onChangeStep(int position, StepperItem activeStep) {
            }
        });

        steppersViewConfig.setFragmentManager(getSupportFragmentManager());
        ArrayList<StepperItem> stepperItems = new ArrayList<>();

        for (int i = 0; i < 2; i ++) {
            StepperItem item = new StepperItem();
            item.setLabel("步骤 " + (i + 1));

            if (i == 0) {
                TitleFragment titleFragment = (TitleFragment) TitleFragment.newInstance(title, intro);
                item.setSubLabel("地点的基本信息");
                item.setFragment(titleFragment);
            } else if (i == 1) {
                MethodFragment methodFragment = (MethodFragment) MethodFragment.newInstance(method, time, tag);
                item.setSubLabel("如何到达地点");
                item.setFragment(methodFragment);
            }
            stepperItems.add(item);
        }

        SteppersView steppersView = (SteppersView) findViewById(R.id.steppersView);
        steppersView.setConfig(steppersViewConfig);
        steppersView.setItems(stepperItems);
        steppersView.build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                reset();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        if (getIntent().getBooleanExtra("edit", true)) {
            title = getIntent().getStringExtra("title");
            intro = getIntent().getStringExtra("snippet");
            method = getIntent().getStringExtra("method");
            time = getIntent().getStringExtra("time");
            MethodFragment.method = method;
            MethodFragment.time = time;
        }
    }
}
