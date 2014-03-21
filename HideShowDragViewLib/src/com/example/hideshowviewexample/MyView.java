package com.example.hideshowviewexample;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

public class MyView extends HideShowView {

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        addView(LayoutInflater.from(context).inflate(R.layout.my_view, null));
    }
    
    public void setText(String text) {
        ((TextView) findViewById(R.id.tvStatus)).setText(text);
    }
}
