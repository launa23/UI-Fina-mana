package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.financemanager.adapters.IconAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IconActivity extends AppCompatActivity {
    private List<Integer> imageResourceIds;
    private RecyclerView rcvIconList;
    private ImageView btnBackInChooseIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        rcvIconList = findViewById(R.id.rcvIconList);
        btnBackInChooseIcon = findViewById(R.id.btnBackInChooseIcon);

        btnBackInChooseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Field[] drawableFields = R.drawable.class.getFields();
        imageResourceIds = new ArrayList<>();
        for (Field field : drawableFields) {
            try {
                String resourceName = field.getName();
                if (!resourceName.startsWith("ic_") && !resourceName.startsWith("xml_")) {
                    int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
                    imageResourceIds.add(resourceId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        IconAdapter iconAdapter = new IconAdapter(imageResourceIds, new IconAdapter.HandleClickIcon() {
            @Override
            public void onItemClick(int position) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("resourceId", imageResourceIds.get(position));

//                String resourceName = getResources().getResourceEntryName(imageResourceIds.get(position));
//                Toast.makeText(getApplicationContext(), resourceName, Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        rcvIconList.setLayoutManager(new GridLayoutManager(this,6));
        rcvIconList.setAdapter(iconAdapter);
    }
}