package com.project.financemanager.common;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.project.financemanager.R;

public class CustomMarkerView extends MarkerView {
    private ImageView imageView;
    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        imageView = (ImageView) findViewById(com.project.financemanager.R.id.tvContent);
    }
}
