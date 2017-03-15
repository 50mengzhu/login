package com.gyh.login.Stepper;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyh.login.R;

public class SteppersViewHolder extends RecyclerView.ViewHolder {

    private boolean isChecked;

    protected View itemView;
    protected RoundedView roundedView;
    protected TextView textViewLabel;
    protected TextView textViewSubLabel;
    protected LinearLayout linearLayoutContent;
    protected FrameLayout frameLayout;
    protected Button buttonContinue;
    protected Button buttonCancel;
    protected Fragment fragment;

    public SteppersViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.roundedView = (RoundedView) itemView.findViewById(R.id.roundedView);
        this.textViewLabel = (TextView) itemView.findViewById(R.id.textViewLabel);
        this.textViewSubLabel = (TextView) itemView.findViewById(R.id.textViewSubLabel);
        this.linearLayoutContent = (LinearLayout) itemView.findViewById(R.id.linearLayoutContent);
        this.frameLayout = (FrameLayout) itemView.findViewById(R.id.frameLayout);
        this.buttonContinue = (Button) itemView.findViewById(R.id.buttonContinue);
        this.buttonCancel = (Button) itemView.findViewById(R.id.buttonCancel);
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    /**
     * @return true if step is done, false if not
     */
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
