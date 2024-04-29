package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.project.financemanager.broadcast.BroadcastAirplaneMode;
import com.project.financemanager.broadcast.BroadcastWifi;
import com.project.financemanager.databinding.ActivityMainBinding;
import com.project.financemanager.dtos.CategoryDTO;
import com.project.financemanager.models.Category;
import com.project.financemanager.models.Transaction;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    //tus
    private ActivityResultLauncher<Intent> launcherforAdd;
    private BroadcastWifi broadcastWifi;
    private BroadcastAirplaneMode broadcastAirplaneMode;

    //sut
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //wifi mode show
        broadcastWifi = new BroadcastWifi();
        IntentFilter filterWifi = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(broadcastWifi, filterWifi);

        //airplane mode show
        broadcastAirplaneMode = new BroadcastAirplaneMode();
        IntentFilter filterAirplane = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        registerReceiver(broadcastAirplaneMode, filterAirplane);

        replaceFragment(new HomeFragment());
        //tus
        initResultLauncher();
        //sut
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
                binding.fabMenu.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.categorys) {
                replaceFragment(new CategoryFragment());
                binding.fabMenu.setVisibility(View.VISIBLE);
            } else if (item.getItemId() == R.id.charts) {
                replaceFragment(new ChartFragment());
                binding.fabMenu.setVisibility(View.GONE);
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new AccountFragment());
                binding.fabMenu.setVisibility(View.GONE);
            }
            return true;
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tus
                try {
                    Intent intent = new Intent(MainActivity.this, InsertAndUpdateTransaction.class);
                    intent.putExtra("flag", "0"); // danh dau la them
                    launcherforAdd.launch(intent);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //sut
            }
        });

        ActivityResultLauncher<Intent> launcherAddCategory = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                        CategoryDTO cateCreate = (CategoryDTO) result.getData().getSerializableExtra("cateCreate");
                        if (cateCreate != null) {
                            binding.bottomNavigationView.setSelectedItemId(R.id.categorys);
                        }
                    }
                }
        );
        binding.btnAddOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateAndInsertCategory.class);
                intent.putExtra("fl", "0");
                intent.putExtra("typeCategory", "0");
                launcherAddCategory.launch(intent);
            }
        });

        binding.btnAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateAndInsertCategory.class);
                intent.putExtra("fl", "0");
                intent.putExtra("typeCategory", "1");
                launcherAddCategory.launch(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastWifi);
        unregisterReceiver(broadcastAirplaneMode);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
//    private void showBottomDialog(){
//            final Dialog dialog = new Dialog(this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.bottomsheet_layout);
//
//        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//    }

    //tus
    private void initResultLauncher() {
        try {
            launcherforAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null && result.getResultCode() == RESULT_OK) {
                    Transaction transCreate = (Transaction) result.getData().getSerializableExtra("transCreate");
                    if (transCreate != null) {
                        binding.bottomNavigationView.setSelectedItemId(R.id.home);
                    }
                }
            });
        } catch (Exception ex) {
            Log.e("initResultLauncher", ex.getMessage());
        }
    }
    //sut

}