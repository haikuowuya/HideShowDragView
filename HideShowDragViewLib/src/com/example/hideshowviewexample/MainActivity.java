
package com.example.hideshowviewexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.example.hideshowviewexample.HideShowDragView.HideShowListener;

public class MainActivity extends Activity implements OnClickListener, HideShowListener {

    private EditText etX, etY;
    private MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Button btnToggle = (Button) findViewById(R.id.btnAnimateTo);
        Button btnSet = (Button) findViewById(R.id.btnSet);
        btnToggle.setOnClickListener(this);
        btnSet.setOnClickListener(this);

        etX = (EditText) findViewById(R.id.etX);
        etY = (EditText) findViewById(R.id.etY);

        myView = (MyView) findViewById(R.id.myView);
        
        myView.setPosition(0, 0);
//        myView.setPositions(0, 0, 500, 500);
        
        myView.makeInvisibleOnHide(false);
        
        myView.setHideShowListener(this);
        
        myView.setDragEnabled(true);
        
        myView.setShowInterpolator(new DecelerateInterpolator(0.5f));
        
        myView.setHideInterpolator(new DecelerateInterpolator(0.5f));

        myView.setHideShowDuration(800); 
    } 

    @Override
    public void onClick(View v) {

        float x = Float.valueOf(etX.getText().toString());
        float y = Float.valueOf(etY.getText().toString());

        switch (v.getId()) {
            case R.id.btnAnimateTo:
                myView.toggle();
                break; 
            case R.id.btnSet:
                myView.setPosition(x, y);
                break;
        }
    }

    @Override
    public void onHide(HideShowDragView v, float curX, float curY) {
        Log.i("HideCompleted", "x: " + curX + ", y: " + curY);
        myView.setText("Shown: " + v.isShown());
    }

    @Override
    public void onShow(HideShowDragView v, float curX, float curY) {   
        Log.i("ShowCompleted", "x: " + curX + ", y: " + curY);
        myView.setText("Shown: " + v.isShown());
    }
}
