package com.c2mtechnology.ductcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.telephony.euicc.DownloadableSubscription;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.c2mtechnology.ductcalc.classes.SharedPrefManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class CalculateActivity extends AppCompatActivity {

    String lang;
    EditText friction_et,airflow_et,velocity_et,diameter_et,a_et,b_et,a1_et,b1_et,a2_et,b2_et;
    String error_msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        initUI();
    }

    private void initUI()
    {
        lang = SharedPrefManager.getInstance(this).getLang();
        TextView friction_tv    = findViewById(R.id.textView71);
        TextView airflow_tv     = findViewById(R.id.textView72);
        TextView speed_tv       = findViewById(R.id.textView73);
        TextView diameter       = findViewById(R.id.textView74);
        Button clear            = findViewById(R.id.clear_button);
        Button calculate        = findViewById(R.id.calculate_button);
        Resources resources = getResources();

        if(lang.equals("eng"))
        {

            friction_tv.setText(resources.getString(R.string.friction_en));
            airflow_tv.setText(resources.getString(R.string.airflow_en));
            speed_tv.setText(resources.getString(R.string.speed_en));
            diameter.setText(resources.getString(R.string.diameter_en));
            clear.setText(resources.getString(R.string.suppress_en));
            calculate.setText(resources.getString(R.string.calculate_en));
            error_msg = resources.getString(R.string.error_en);

        }else{
            friction_tv.setText(resources.getString(R.string.friction_es));
            airflow_tv.setText(resources.getString(R.string.airflow_es));
            speed_tv.setText(resources.getString(R.string.speed_es));
            diameter.setText(resources.getString(R.string.diameter_es));
            clear.setText(resources.getString(R.string.suppress_es));
            calculate.setText(resources.getString(R.string.calculate_es));
            error_msg = resources.getString(R.string.error_es);
        }

        friction_et     = findViewById(R.id.friction_et);
        airflow_et      = findViewById(R.id.airflow_et);
        velocity_et     = findViewById(R.id.velocity_et);
        diameter_et     = findViewById(R.id.diameter_et);
        a_et            = findViewById(R.id.a_et);
        b_et            = findViewById(R.id.b_et);
        a1_et           = findViewById(R.id.a1_et);
        b1_et           = findViewById(R.id.b1_et);
        a2_et           = findViewById(R.id.a2_et);
        b2_et           = findViewById(R.id.b2_et);
    }

    public void buttonOperations(View v)
    {
        String tag = v.getTag().toString();
        switch (tag){
            case "help":
                showHelp();
                break;
            case "calculate":
                calculate();
                break;
            case "clear":
                clearValues();
                break;
        }
    }

    private void calculate()
    {
        Double a = 0.0,b = 0.0, Q = 0.0, VV = 0.0, P = 0.0, D = 0.0;
        if(!a_et.getText().toString().equals(""))
        {
            a = Double.parseDouble(a_et.getText().toString())/1000;
        }

        if(!b_et.getText().toString().equals(""))
        {
            b = Double.parseDouble(b_et.getText().toString())/1000;
        }

        if(!airflow_et.getText().toString().equals(""))
        {
            Q = Double.parseDouble(airflow_et.getText().toString())/1000;
        }

        if(!velocity_et.getText().toString().equals("") && velocity_et.getText() != null)
        {
            VV = Double.parseDouble(velocity_et.getText().toString());
        }

        if(!friction_et.getText().toString().equals(""))
        {
            P = Double.parseDouble(friction_et.getText().toString());
        }

        if(!diameter_et.getText().toString().equals(""))
        {
            D = Double.parseDouble(diameter_et.getText().toString())/1000;
        }
        Log.i("INFO","a : "  + a + " v : "  + VV);
        calculateValues(a,b,Q,VV,P,D);
    }

    private void calculateValues(Double a, Double b, Double Q, Double VV, Double P,Double D)
    {
        Double A1 = 0.0, B1 = 0.0, a2 = 0.0,b2 = 0.0;
        if(Q != 0.0 && VV != 0.0)
        {
            D = 1.128 * (Math.pow((Q/VV),0.5));
            P = (0.013 * (Math.pow(VV,1.82))) / (Math.pow(D,1.22));

            a = 0.9149 * D;
            b = 0.9149 * D;

            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            D   = round(D);
            a   = round(a);
            b   = round(b);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);
        }else if(Q != 0.0 && D != 0.0)
        {
            VV = 1.2732 * Q / ( Math.pow(D , 2));
            P = (0.013 * (Math.pow(VV , 1.82 )))/ (Math.pow(D , 1.22));

            a = 0.9149 * D;
            b = 0.9149 * D;
            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            a   = round(a);
            b   = round(b);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            D   = D * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);
        }else if(Q != 0.0 && P != 0.0)
        {
            VV = 6.3598 * (Math.pow(P , 0.412)) * (Math.pow(Q , 0.251));
            D = 1.128 * (Math.pow((Q / VV) , 0.5));

            a = 0.9149 * D;
            b = 0.9149 * D;
            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            D   = round(D);
            a   = round(a);
            b   = round(b);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);

            Q = Q * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);

        }else if(Q != 0.0 && a != 0.0 && b != 0.0)
        {
            D = (1.3 * (Math.pow( (a * b) , 0.625) )) / (Math.pow((a + b) , 0.25));
            VV = 1.2732 * Q / (Math.pow(D , 2));
            P = 0.013 * (Math.pow(VV , 1.82)) / (Math.pow(D , 1.22));

            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            D   = round(D);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            a   = a * 1000;
            b   = b * 1000;
            Q   = Q * 1000;
            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);

        }else if(VV != 0 && P != 0.0)
        {
            Q = Math.pow((0.0112 * (Math.pow(VV , 2.43)) / P)  , 1.639);
            D = 1.128 * (Math.pow ((Q / VV) , 0.5));

            a = 0.9149 * D;
            b = 0.9149 * D;
            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            a   = round(a);
            b   = round(b);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            D   = D * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);

        }else if(VV != 0 && D != 0)
        {
            Q = 0.7854 * VV * (Math.pow(D , 2));
            P = (0.013 * (Math.pow(VV , 1.82))) / (Math.pow(D , 1.22));

            a = 0.9149 * D;
            b = 0.9149 * D;
            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;


            a   = round(a);
            b   = round(b);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            D   = D * 1000;


            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);

        }else if(VV != 0 && a != 0 && b != 0)
        {
            D = (1.3 * ( Math.pow( (a * b),0.625))) / (Math.pow( (a + b) , 0.25));
            Q = 0.7854 * VV * (Math.pow(D , 2));
            P = (0.013 * (Math.pow(VV , 1.82))) / (Math.pow(D , 1.22));

            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            D   = round(D);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            a   = a * 1000;
            b   = b * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);
        }else if(P != 0.0 && D != 0.0)
        {
            VV = 10.898 * (Math.pow(P , 0.55)) * (Math.pow(D , 0.671));
            Q = 0.7854 * VV * (Math.pow(D , 2));

            a = 0.9149 * D;
            b = 0.9149 * D;
            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            a   = round(a);
            b   = round(b);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            D   = D * 1000;
            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);

        }else if(P != 0 && a != 0.0 && b != 0.0)
        {
            D = 1.3 * (Math.pow((a * b) , 0.625)) / (Math.pow((a + b) , 0.25));
            VV = 10.898 * (Math.pow(P , 0.55)) * (Math.pow(D , 0.671));
            Q = 0.7854 * VV * (Math.pow(D , 2));

            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            D   = round(D);
            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);
            Q   = Q * 1000;
            a   = a * 1000;
            b   = b * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);

        }else if(D != 0.0 && a != 0.0 && b == 0.0)
        {
            b = findb(a,b,D);
            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);

            D   = D * 1000;
            a   = a * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);


        }else if(D != 0 && b != 0 && a == 0.0)
        {
            a = finda(a,b,D);

            A1 = 0.6564 * D;
            B1 = 1.3129 * D;
            a2 = 0.5475 * D;
            b2 = 1.6424 * D;

            A1  = round(A1);
            B1  = round(B1);
            a2  = round(a2);
            b2  = round(b2);

            D = D * 1000;
            b  = b * 1000;

            showValues(a,b,Q,VV,P,D,A1,B1,a2,b2);
        }else{
            Snackbar.make(b2_et,error_msg, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    private Double findb(Double a,Double b, Double D)
    {
        double m = 0;
        double diam = 0;
        while(diam < D)
        {
            double bb = m * 0.025;
            diam = 1.3 * ( ((Math.pow( (a * bb) , 0.625)) ) / (Math.pow((a + bb) , 0.25)) );
            m += 1;
        }

        double diff = diam - D;
        if(diff < 0.0125)
        {
            b = m * 0.025 * 1000;
        }else{
            b = (m-1) * 0.025 * 1000;
        }

        return b;
    }

    private Double finda(Double a,Double b, Double D)
    {
        double m = 0;
        double diam = 0;
        while(diam < D)
        {
            double aa = m * 0.025;
            diam = (1.3 * (Math.pow((aa * b) , 0.625))) / (Math.pow((aa + b) , 0.25));
            m += 1;
        }

        double diff = diam - D;
        if(diff < 0.0125)
        {
            a = m * 0.25 * 1000;
        }else{
            a = (m-1) * 0.025 * 1000;
        }

        return a;
    }

    private Double round(Double x)
    {
        double m = x / 0.025;
        int mm = (int) m;
        double mmm = mm + 1;
        x = mmm * 0.025 * 1000;
        return x;
    }

    private void showValues(Double a, Double b, Double Q, Double VV, Double P,Double D,Double a1,Double b1,Double a2,Double b2)
    {
        if(a != null)
        {
            int a_int = (int) roundAvoid(a,0);
            a_et.setText(String.valueOf( a_int ));
        }

        if(b != null)
        {
            int b_int = (int) roundAvoid(b,0);
            b_et.setText(String.valueOf(b_int));
        }

        if(Q != null)
        {
            Q = roundAvoid(Q,1);
            airflow_et.setText(String.valueOf(Q));
        }

        if(VV != null)
        {
            VV = roundAvoid(VV, 1);
            velocity_et.setText(String.valueOf(VV));
        }

        if(P != null)
        {
            P = roundAvoid(P,1);
            friction_et.setText(String.valueOf(P));
        }

        if(D != null)
        {
            int D_int = (int) roundAvoid(D,0);
            diameter_et.setText(String.valueOf(D_int));
        }

        if(a1 != null)
        {
            int a1_int = (int) roundAvoid(a1,0);
            a1_et.setText(String.valueOf(a1_int));
        }

        if(b1 != null)
        {
            int b1_int = (int) roundAvoid(b1,0);
            b1_et.setText(String.valueOf(b1_int));
        }

        if(a2 != null)
        {
            int a2_int = (int) roundAvoid(a2,0);
            a2_et.setText(String.valueOf(a2_int));
        }

        if(b2 != null)
        {
            int b2_int = (int) roundAvoid(b2,0);
            b2_et.setText(String.valueOf(b2_int));
        }

    }

    private void clearValues()
    {
        a_et.setText("");
        b_et.setText("");
        airflow_et.setText("");
        velocity_et.setText("");
        friction_et.setText("");
        diameter_et.setText("");
        a1_et.setText("");
        b1_et.setText("");
        a2_et.setText("");
        b2_et.setText("");
    }

    public  double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    private void showHelp()
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View v = null;
        if(lang.equals("eng"))
        {
            v = getLayoutInflater().inflate(R.layout.help_bottom_sheet,null);
        }else{
            v = getLayoutInflater().inflate(R.layout.help_bottom_sheet_es,null);
        }

        bottomSheetDialog.setContentView(v);
        bottomSheetDialog.show();
    }
}