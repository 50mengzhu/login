package com.gyh.login.Stepper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class SteppersView extends LinearLayout {

    private RecyclerView mRecyclerView;
    private SteppersAdapter mAdapter;

    private Config config;
    private List<StepperItem> mItems;

    public SteppersView(Context context) {
        super(context);
    }

    public SteppersView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SteppersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SteppersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public SteppersView setConfig(Config config) {
        this.config = config;
        return this;
    }

    public SteppersView setItems(List<StepperItem> items) {
        this.mItems = items;
        return this;
    }

    public void build() {
        if (config != null) {
            mRecyclerView = new RecyclerView(getContext());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRecyclerView.setLayoutParams(layoutParams);

            addView(mRecyclerView);

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new SteppersAdapter(this, config, mItems);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            throw new RuntimeException("SteppersView need config, read documentation to get more info");
        }
    }

    public static class Config {

        private OnFinishAction mOnFinishAction;
        private OnCancelAction mOnCancelAction;
        private OnChangeStepAction mOnChangeStepAction;
        private FragmentManager mFragmentManager;

        public Config() {}

        public Config setOnFinishAction(OnFinishAction onFinishAction) {
            this.mOnFinishAction = onFinishAction;
            return this;
        }

        public OnFinishAction getOnFinishAction() {
            return mOnFinishAction;
        }

        public Config setOnCancelAction(OnCancelAction onCancelAction) {
            this.mOnCancelAction = onCancelAction;
            return this;
        }

        public OnCancelAction getOnCancelAction() {
            return mOnCancelAction;
        }

        public void setOnChangeStepAction(OnChangeStepAction onChangeStepAction) {
            this.mOnChangeStepAction = onChangeStepAction;
        }

        public OnChangeStepAction getOnChangeStepAction() {
            return mOnChangeStepAction;
        }

        public void setFragmentManager(FragmentManager fragmentManager) {
            this.mFragmentManager = fragmentManager;
        }

        public FragmentManager getFragmentManager() {
            return mFragmentManager;
        }
    }

    static int fID = 190980;
    protected static int findUnusedId(View view) {
        while( view.getRootView().findViewById(++fID) != null);
        return fID;
    }
}
