package com.gyh.login.Stepper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyh.login.EditText.ClearEditText;
import com.gyh.login.R;

public class TitleFragment extends Fragment {

    public static ClearEditText titleInput;
    public static ClearEditText introInput;

    public TitleFragment() {

    }

    public static Fragment newInstance(String title, String intro) {
        TitleFragment titleFragment = new TitleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("intro", intro);
        titleFragment.setArguments(bundle);
        return titleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title, container, false);

        titleInput = (ClearEditText) view.findViewById(R.id.title_input);
        introInput = (ClearEditText) view.findViewById(R.id.intro_input);

        titleInput.setText(getArguments().getString("title"));
        introInput.setText(getArguments().getString("intro"));

        return view;
    }
}
