package com.example.dhruvik.barcodealarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SimpleOffScreen extends AppCompatActivity {


    Button stop,snooz;
    TextView  showTime,qus;
    EditText uans;
    Calendar calNow,calAlarm;
    Button check;
    Random random;
    int counter = 0;


    long timeLmit = 10 * 60 * 1000;
    long timeInterval = 1000;
    int tar,rans;
    int RequestCode;
static boolean boundary = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_off_screen);
        initiallixation();

Date date = new Date();
calNow.setTime(date);
calAlarm.setTime(date);

check.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String strans = uans.getText().toString();
        if(checkAns(strans)){
            setNext();
            counter++;
        }
    }
});

tar = calNow.get(Calendar.HOUR_OF_DAY) * 100 + calNow.get(Calendar.MINUTE);

        new CountDownTimer(timeLmit,timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(boundary) {

                    if (AlarmReceiver.ring.isPlaying()) {
                        //nothing to do
                    } else {
                        AlarmReceiver.ring.play();
                    }
                }
            }
            @Override
            public void onFinish() {

            }
        }.start();

        showTime.setText(calNow.get(Calendar.HOUR_OF_DAY) + " : " + calNow.get(Calendar.MINUTE));
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelAlarm cancelAlarm = new CancelAlarm(getApplicationContext());
                cancelAlarm.cancelAction();
            }
        });

        snooz.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                calAlarm.set(Calendar.HOUR_OF_DAY,calNow.get(Calendar.HOUR));
                calAlarm.set(Calendar.MINUTE,calNow.get(Calendar.MINUTE));
                calAlarm.set(Calendar.SECOND,0);

                if (calAlarm.before(calNow)) {
                    calAlarm.add(Calendar.DATE, 1);
                }

                Intent io = new Intent(getApplicationContext(),AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),new DealData(getApplicationContext()).getRequestCode(tar),io,0);

                am.setRepeating(AlarmManager.RTC_WAKEUP,calAlarm.getTimeInMillis(),10 * 60 * 1000,pi );

            }
        });
    }

    private void setNumber(){
        snooz.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        int num1 = random.nextInt(100);
        int num2 = random.nextInt(100);

        rans = num1+num2;

        qus.setText(Integer.toString(num1) + " " + "+" + " " + Integer.toString(num2));

    }

    private boolean checkAns(String uuans){
        int checkAns;
        try{
            checkAns = Integer.parseInt(uuans);
             if(checkAns == rans){
                 Toast.makeText(getApplicationContext(),"Answer is Right...",Toast.LENGTH_LONG).show();

             }else{
                 Toast.makeText(getApplicationContext(),"Answer is Wrong...",Toast.LENGTH_LONG).show();
             }
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            uans.setText(" ");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Please enter valid input : " + uuans,Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void setNext(){
        if(counter < 3){
           setNumber();
        }else{
            snooz.setVisibility(View.VISIBLE);
            stop.setVisibility(View.VISIBLE);
        }
    }


    private void initiallixation(){
        stop = (Button)this.findViewById(R.id.stop);
        snooz = (Button)this.findViewById(R.id.snooz);
        showTime = (TextView)this.findViewById(R.id.showTime);
        calNow = Calendar.getInstance();
        calAlarm = Calendar.getInstance();
        qus=(TextView)this.findViewById(R.id.question);
        uans=(EditText)this.findViewById(R.id.ans);
        check=(Button)this.findViewById(R.id.check);
        random=new Random();
        setNext();
        snooz.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
    }
}
