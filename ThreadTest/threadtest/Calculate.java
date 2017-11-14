package com.example.sunwoo.threadtest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import java.math.BigDecimal;

public class Calculate extends AppCompatActivity implements Runnable {

    private String pi_string;
    private TextView tv;
    private ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        tv = (TextView) findViewById(R.id.main);
        tv.setText("Press any key to start calculation");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        pd = ProgressDialog.show(this, "Working...", "Calculating Pi", true, false);
        Thread th = new Thread(this);
        th.start();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void run() {
        pi_string = Pi.computePi(800).toString();
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pd.dismiss();
            tv.setText(pi_string);
        }
    };
}

class Pi
{
    private static final BigDecimal FOUR = BigDecimal.valueOf(4);
    private static final int roundingMode = BigDecimal.ROUND_HALF_EVEN;

    public static BigDecimal computePi(int digits)
    {
        int scale = digits + 5;
        BigDecimal arctan1_5 = arctan(5, scale);
        BigDecimal arctan1_239 = arctan(239, scale);
        BigDecimal pi = arctan1_5.multiply(FOUR).subtract(arctan1_239).multiply(FOUR);

        return pi.setScale(digits, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal arctan(int inverseX, int scale)
    {
        BigDecimal result, numer, term;
        BigDecimal invX = BigDecimal.valueOf(inverseX);
        BigDecimal invX2 = BigDecimal.valueOf(inverseX * inverseX);

        numer = BigDecimal.ONE.divide(invX, scale, roundingMode);
        result = numer;

        int i = 1;
        do {
            numer = numer.divide(invX2, scale, roundingMode);
            int denom = 2 * i + 1;
            term = numer.divide(BigDecimal.valueOf(denom), scale, roundingMode);

            if((i % 2) != 0)
                result = result.subtract(term);
            else
                result = result.add(term);

            i++;
        } while (term.compareTo(BigDecimal.ZERO) != 0);

        return result;
    }
}


