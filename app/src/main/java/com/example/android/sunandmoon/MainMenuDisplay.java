package com.example.android.sunandmoon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import static android.R.attr.y;
import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.android.sunandmoon.SunMoonDisplay.date;

public class MainMenuDisplay extends AppCompatActivity {

    ImageButton calendar_btn;
    ImageButton location_btn;
    TextView calendar_txt;
    public static TextView location_txt;
    static final int DIALOG_ID = 0;
    int year_x,month_x,day_x;
    Intent intent;
    String string;
    public static String date;
    DatabaseHelper dbHeplper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_display);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        location_txt = (TextView) findViewById(R.id.location_textView);



        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);



        showDialogOnButtonClick();

    }

    public void showDialogOnButtonClick() {
        calendar_btn = (ImageButton) findViewById(R.id.calendar_button);
        calendar_btn.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );



    }


    @Override
    protected Dialog onCreateDialog(int id)

    {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;

             date = month_x + "/" + day_x + "/" + year_x;
            String date_display = " Selected date: " + month_x + "/" + day_x + "/" + year_x;


            calendar_txt = (TextView)findViewById(R.id.calendar_textView);
            calendar_txt.setText(date_display);




        }
    };


     public void location_display(View view) {


        Toast.makeText(this,"This may take a while. Please wait.",Toast.LENGTH_SHORT).show();
         Intent intent = new Intent(this, LocationActivity.class);
         startActivity(intent);



     }


    public void go(View view)
    {
        calendar_txt = (TextView)findViewById(R.id.calendar_textView);
        location_txt = (TextView)findViewById(R.id.location_textView) ;
        String location_textview = location_txt.getText().toString();
        String calendar_textview = calendar_txt.getText().toString();

        String comparator = "Select location";
        String comparator1= "Select date";

        if(location_textview.equals(comparator) || calendar_textview.equals(comparator1))
        {
            Toast.makeText(MainMenuDisplay.this,"please select both location and date", Toast.LENGTH_SHORT).show();

        }
        else
        {
            String info  = location_txt.getText().toString();

            String info1[] = info.split(",");



            dbHeplper = new DatabaseHelper(getApplicationContext());
            try {
                dbHeplper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[]  ltlon = dbHeplper.getInfo(info1);



            Intent mainIntent = new Intent(MainMenuDisplay.this,SunMoonDisplay.class).putExtra("year",Integer.toString(year_x));
            mainIntent.putExtra("month",Integer.toString(month_x));

            mainIntent.putExtra("day",Integer.toString(day_x));
            mainIntent.putExtra("latitude",ltlon[0]);
            mainIntent.putExtra("longitude",ltlon[1]);
            mainIntent.putExtra("time_zone",ltlon[2]);
            MainMenuDisplay.this.startActivity(mainIntent);

        }

    }




}