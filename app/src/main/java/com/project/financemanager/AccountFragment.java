package com.project.financemanager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AccountFragment extends Fragment {

    private TextView txtNameUser;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        txtNameUser = rootView.findViewById(R.id.txtNameUser);
        sharedPreferences = getActivity().getSharedPreferences("CHECK_TOKEN", getActivity().MODE_PRIVATE);
        String fullname = sharedPreferences.getString("fullname", "");
        txtNameUser.setText(fullname);
        return rootView;
    }
}