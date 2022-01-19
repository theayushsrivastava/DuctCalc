package com.c2mtechnology.ductcalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.c2mtechnology.ductcalc.classes.SharedPrefManager;

public class SelectLanguageActivity extends AppCompatActivity {
    String lan = "eng";
    CardView cv1,cv2;
    TextView tv1,tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        cv1     = findViewById(R.id.cv1);
        cv2     = findViewById(R.id.cv2);
        tv1     = findViewById(R.id.tv1);
        tv2     = findViewById(R.id.tv2);

    }

    public void buttonOperations(View v)
    {
        String tag = v.getTag().toString();
        switch (tag)
        {
            case "eng":
                lan = "eng";
                switchLang();
                break;
            case "spanish":
                lan = "spanish";
                switchLang();
                break;
            case "start":
                startActivity();
                break;
        }
    }

    private void switchLang()
    {
        if(lan.equals("eng"))
        {
            cv1.setCardBackgroundColor(getResources().getColor(R.color.black));
            tv1.setTextColor(getResources().getColor(R.color.white));
            cv2.setCardBackgroundColor(getResources().getColor(R.color.white));
            tv2.setTextColor(getResources().getColor(R.color.black));
        }else{
            cv2.setCardBackgroundColor(getResources().getColor(R.color.black));
            tv2.setTextColor(getResources().getColor(R.color.white));
            cv1.setCardBackgroundColor(getResources().getColor(R.color.white));
            tv1.setTextColor(getResources().getColor(R.color.black));
        }
    }

    private void startActivity()
    {
        SharedPrefManager.getInstance(this).setLang(lan);
        Intent intent  = new Intent(this,CalculateActivity.class);
        startActivity(intent);
//        finish();
    }
}