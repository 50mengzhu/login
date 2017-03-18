package com.gyh.login.Stepper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.gyh.login.EditText.ClearEditText;
import com.gyh.login.R;

public class MethodFragment extends Fragment {

    public static String method = "到达方式";
    public static String time = "预计时间";

    private Spinner methodSpinner;
    private Spinner timeSpinner;

    public static ClearEditText tagInput;

    public MethodFragment() {

    }

    public static Fragment newInstance(String method, String time, String tag) {
        MethodFragment fragment = new MethodFragment();
        Bundle bundle = new Bundle();
        bundle.putString("method", method);
        bundle.putString("time", time);
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_method, container, false);

        methodSpinner = (Spinner) view.findViewById(R.id.method_spinner);
        timeSpinner = (Spinner) view.findViewById(R.id.time_spinner);
        tagInput = (ClearEditText) view.findViewById(R.id.method_tag_input);

        tagInput.setText(getArguments().getString("tag"));

        final String[] methods = getActivity().getResources().getStringArray(R.array.methodarray);
        final String[] times = getActivity().getResources().getStringArray(R.array.timearray);

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].equals(getArguments().getString("method"))) {
                methodSpinner.setSelection(i, true);
                break;
            }
        }

        for (int i = 0; i < times.length; i++) {
            if (times[i].equals(getArguments().getString("time"))) {
                timeSpinner.setSelection(i, true);
                break;
            }
        }

        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                method = methods[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = times[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }
}