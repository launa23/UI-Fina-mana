package com.project.financemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AccountFragment extends Fragment {
    private RelativeLayout relative32;
    private RelativeLayout relative40;
    private RelativeLayout btnLogout;
    private TextView txtNameUser;
    private TextView txtEmailUser;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        txtNameUser = rootView.findViewById(R.id.txtNameUser);
        txtEmailUser = rootView.findViewById(R.id.txtEmailUser);
        relative32 = rootView.findViewById(R.id.relative32);
        relative40 = rootView.findViewById(R.id.relative40);
        btnLogout = rootView.findViewById(R.id.btnLogout);

        SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = getActivity().getSharedPreferences("CHECK_TOKEN", getActivity().MODE_PRIVATE);
        String fullname = sharedPreferences.getString("fullname", "");
        String email = sharedPreferences.getString("email", "");
        txtNameUser.setText(fullname);
        txtEmailUser.setText(email);

        Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink_animation);
        relative32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relative32.startAnimation(blinkAnimation);
                try {
                    Intent intent = new Intent(getActivity(), WalletMainActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        relative40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative40.startAnimation(blinkAnimation);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogout.startAnimation(blinkAnimation);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();

                editor.clear();
                editor1.clear();
                editor.apply();
                editor1.apply();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();

            }
        });
        return rootView;
    }
}