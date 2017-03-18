package com.gyh.login.Stepper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gyh.login.MarkerSet;
import com.gyh.login.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class SteppersAdapter extends RecyclerView.Adapter<SteppersViewHolder> {

    private static final String TAG = "SteppersAdapter";
    private SteppersView mSteppersView;
    private Context mContext;
    private SteppersView.Config mConfig;
    private List<StepperItem> mItems;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private Map<Integer, Integer> frameLayoutIds = new HashMap<>();

    private int VIEW_COLLAPSED = 0;
    private int VIEW_EXPANDED = 1;

    private int removeStep = -1;
    private int beforeStep = -1;
    private int currentStep = 0;

    public SteppersAdapter(SteppersView steppersView, SteppersView.Config config, List<StepperItem> items) {
        this.mSteppersView = steppersView;
        this.mContext = mSteppersView.getContext();
        this.mConfig = config;
        this.mItems = items;
        this.mFragmentManager = config.getFragmentManager();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == currentStep ? VIEW_EXPANDED : VIEW_COLLAPSED);
    }

    @Override
    public SteppersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == VIEW_COLLAPSED) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steppers_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steppers_item_expanded, parent, false);
        }

        SteppersViewHolder vh = new SteppersViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SteppersViewHolder holder, int p) {
        final int position = holder.getAdapterPosition();
        final StepperItem stepperItem = mItems.get(position);

        holder.setChecked(position < currentStep);
        if (holder.isChecked()) {
            holder.roundedView.setChecked(true);
        } else {
            holder.roundedView.setChecked(false);
            holder.roundedView.setText(position + 1 + "");
        }

        if (position == currentStep || holder.isChecked()) {
            holder.roundedView.setCircleAccentColor();
        } else {
            holder.roundedView.setCircleGrayColor();
        }

        holder.textViewLabel.setText(stepperItem.getLabel());
        holder.textViewSubLabel.setText(stepperItem.getSubLabel());

        holder.linearLayoutContent.setVisibility(position == currentStep || position == beforeStep ? View.VISIBLE : View.GONE);

        holder.buttonContinue.setEnabled(stepperItem.isPositiveButtonEnable());
        stepperItem.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if (o != null) {
                    StepperItem item = (StepperItem) o;
                    holder.buttonContinue.setEnabled(item.isPositiveButtonEnable());
                }
            }
        });

        if (position == getItemCount() - 1) {
            holder.buttonContinue.setText(mContext.getResources().getString(R.string.step_finish));
        } else {
            holder.buttonContinue.setText(mContext.getResources().getString(R.string.step_continue));
        }

        if (position == 0) {
            holder.buttonCancel.setVisibility(View.GONE);
        } else {
            holder.buttonCancel.setVisibility(View.VISIBLE);
        }

        holder.buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
                    boolean flag = true;

                    if(TitleFragment.titleInput.getText().toString().trim().equals("")) {
                        TitleFragment.titleInput.startShakeAnimation();
                        TitleFragment.titleInput.setError("输入不可为空");
                        flag = false;
                    }

                    if(TitleFragment.introInput.getText().toString().trim().equals("")) {
                        TitleFragment.introInput.startShakeAnimation();
                        TitleFragment.introInput.setError("输入不可为空");
                        flag = false;
                    }

                    if (flag) {
                        MarkerSet.title = TitleFragment.titleInput.getText().toString();
                        MarkerSet.intro = TitleFragment.introInput.getText().toString();
                        nextStep();
                    }
                } else if (position == 1) {
                    if (MethodFragment.method.equals("到达方式") || MethodFragment.time.equals("预计时间")) {
                        Toast.makeText(mContext, "请选择相关选项", Toast.LENGTH_SHORT).show();
                    } else {
                        MarkerSet.method = MethodFragment.method;
                        MarkerSet.time = MethodFragment.time;
                        MarkerSet.tag = MethodFragment.tagInput.getText().toString();
                        MethodFragment.method = "到达方式";
                        MethodFragment.time = "预计时间";
                        mConfig.getOnFinishAction().onFinish();
                    }
                }
            }
        });

        if (mConfig.getOnCancelAction() != null) {
            holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConfig.getOnCancelAction().onCancel();
                    prevStep();
                }
            });
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        if (frameLayoutIds.get(position) != null) {
            frameLayoutIds.put(position, findUnusedId(holder.itemView));
        }

        if (mConfig.getFragmentManager() != null && stepperItem.getFragment() != null) {
            holder.frameLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
            holder.frameLayout.setTag(frameLayoutName());

            if (mFragmentTransaction == null) {
                mFragmentTransaction = mFragmentManager.beginTransaction();
            }

            String name = makeFragmentName(mSteppersView.getId(), position);
            Fragment fragment = mFragmentManager.findFragmentByTag(name);

            if (position < beforeStep) {
                if (fragment != null) {
                    mFragmentTransaction.detach(fragment);
                }
            } else if (position == beforeStep || position == currentStep) {
                if (fragment != null) {
                    mFragmentTransaction.attach(fragment);
                } else {
                    fragment = stepperItem.getFragment();
                    mFragmentTransaction.add(mSteppersView.getId(), fragment, name);
                }
            }

            if (mFragmentTransaction != null) {
                mFragmentTransaction.commitAllowingStateLoss();
                mFragmentTransaction = null;
                mFragmentManager.executePendingTransactions();
            }

            if (mFragmentManager.findFragmentByTag(name) != null &&
                    mFragmentManager.findFragmentByTag(name).getView() != null) {
                View fragmentView = mFragmentManager.findFragmentByTag(name).getView();

                if (fragmentView.getParent() != null && frameLayoutName() != ((View) fragmentView.getParent()).getTag()) {
                    mSteppersView.removeViewInLayout(fragmentView);
                    holder.frameLayout.removeAllViews();
                    holder.frameLayout.addView(fragmentView);
                }
            }
        }

        if (beforeStep == position) {
            com.gyh.login.Stepper.AnimationUtils.hide(holder.linearLayoutContent);
        }


        if (currentStep == position && !stepperItem.isDisplayed()) {
            stepperItem.setDisplayed(true);
        }
    }

    private void nextStep() {
        this.removeStep = currentStep - 1 > -1 ? currentStep - 1 : currentStep;
        this.beforeStep = currentStep;
        this.currentStep = this.currentStep + 1;
        notifyItemRangeChanged(removeStep, 1);

        if (mConfig.getOnChangeStepAction() != null) {
            StepperItem stepperItem = mItems.get(this.currentStep);
            mConfig.getOnChangeStepAction().onChangeStep(this.currentStep, stepperItem);
        }
    }

    private void prevStep() {
        this.removeStep = currentStep;
        this.beforeStep = currentStep;
        this.currentStep = this.currentStep - 1;
        notifyItemRangeChanged(removeStep, 1);
        notifyItemChanged(currentStep);
    }

    protected void setItems(List<StepperItem> items) {
        this.mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private int fID = 87352142;

    public int findUnusedId(View view) {
        while( view.findViewById(++fID) != null );
        return fID;
    }

    private static String frameLayoutName() {
        return "android:steppers:framelayout";
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:steppers:" + viewId + ":" + id;
    }
}
