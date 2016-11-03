package com.example.android.sunandmoon;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.Astronomy.SunMoonCalculator;

import java.util.Date;
import java.util.Locale;

import static android.R.attr.onClick;
import static android.R.attr.value;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.os.Build.VERSION_CODES.M;
import static android.view.View.Z;
import static com.example.android.Astronomy.SunMoonCalculator.getDateAsString;
import static com.example.android.sunandmoon.ClockActivityRise.hours;
import static com.example.android.sunandmoon.ClockActivityRise.minutes;
import static com.example.android.sunandmoon.ClockActivityRise.seconds;

public class SunMoonDisplay extends AppCompatActivity {

    TextView display_textview;
    ImageView imageView;
    TextView moon_phase_text;
    TextView rise_time,set_time;


    Button sun_button;
    int year_x, month_x, day_x;
    String year,month,day;
    Double latitude, longitude;
    String time_zone;
    ClockActivityRise clockActivityRise;
    ClockActivitySet clockActivitySet;
    public static TextView location,date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun_moon_display);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        location = (TextView) findViewById(R.id.location);


        location.setText(LocationActivity.lvUsers.getText().toString()+ "    " + MainMenuDisplay.date);



        Intent intent = getIntent();
        longitude = Double.parseDouble (intent.getStringExtra("longitude"));
        latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        time_zone = intent.getStringExtra("time_zone");


        year = getIntent().getStringExtra("year");
        month = getIntent().getStringExtra("month");
        day = getIntent().getStringExtra("day");
        display_sun();

        Sun_display_button();

    }


    public void Sun_display_button()

    {
        sun_button = (Button)findViewById(R.id.sun_click);
        sun_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display_sun();
            }
        });


    }

    void display_sun()


    {

        int[] x;
        int[] date;
        StringBuilder stringBuilder = new StringBuilder("");

        year_x = Integer.parseInt(year);
        month_x = Integer.parseInt(month);
        day_x = Integer.parseInt(day);
        try {


            int h = 12, m = 0, s = 0;
            double obsLon = longitude * SunMoonCalculator.DEG_TO_RAD, obsLat = latitude * SunMoonCalculator.DEG_TO_RAD;

            SunMoonCalculator smc = new SunMoonCalculator(year_x, month_x, day_x, h, m, s, obsLon, obsLat);

            String ln = Double.toString(longitude);
            String lt = Double.toString(latitude);



            smc.calcSunAndMoon();





            stringBuilder.append("Sun:" + "\n");

            date = SunMoonCalculator.getDate(smc.sunRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);
            ClockActivityRise.hours = x[3];
            ClockActivityRise.minutes = x[4];
            ClockActivityRise.seconds = x[5];

            rise_time = (TextView) findViewById(R.id.time_display_rise);
            rise_time.setText("Sunrise: " + x[3] +":" + x[4]);

            stringBuilder.append("    Sunrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");

            date = SunMoonCalculator.getDate(smc.sunSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);
            ClockActivitySet.hours = x[3];
            ClockActivitySet.minutes = x[4];
            ClockActivitySet.seconds = x[5];

            set_time =(TextView) findViewById(R.id.time_display_set);
            set_time.setText("Sunset: " + x[3]+":"+x[4]);

            stringBuilder.append("    Sunset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");
            stringBuilder.append("    Azimuth:   " + (float)(smc.sunAz * 57.29577951308232D) + "º"+"\n");
            stringBuilder.append("    Elevation:   " + (float)(smc.sunEl * 57.29577951308232D) + "º"+"\n");
            stringBuilder.append("    Dist:    " + (float)smc.sunDist + " AU"+"\n");

            date = SunMoonCalculator.getDate(smc.sunTransit);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Transit: " +x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5] + " (max. elev. " + (float)(smc.sunTransitElev * 57.29577951308232D) + "Âº)"+"\n");


            clockActivityRise = (ClockActivityRise) findViewById(R.id.analog_clockRise);
            clockActivityRise.setAutoUpdate(true);

            clockActivitySet = (ClockActivitySet) findViewById(R.id.analog_clockSet);
            clockActivitySet.setAutoUpdate(true);




            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_ASTRONOMICAL);
            smc.calcSunAndMoon();
            stringBuilder.append("\n"+"\n"+"\n");
            stringBuilder.append("Astronomical"+"\n");

            date = SunMoonCalculator.getDate(smc.sunRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Sunrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            date = SunMoonCalculator.getDate(smc.sunSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Sunset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_NAUTICAL);
            smc.calcSunAndMoon();
            stringBuilder.append("\n"+"\n"+"\n");
            stringBuilder.append("Nautical"+"\n");
            date = SunMoonCalculator.getDate(smc.sunRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Sunrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            date = SunMoonCalculator.getDate(smc.sunSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Sunset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_CIVIL);
            smc.calcSunAndMoon();
            stringBuilder.append("\n"+"\n"+"\n");
            stringBuilder.append("Civil"+"\n");
            date = SunMoonCalculator.getDate(smc.sunRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Sunrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            date = SunMoonCalculator.getDate(smc.sunSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Sunset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            display_textview = (TextView) findViewById(R.id.SunMoonDisplay_textview);
            display_textview.setText(stringBuilder.toString());

            moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
            moon_phase_text.setText("");
            imageView = (ImageView) findViewById(R.id.moon_phase_image);
            imageView.setImageResource(android.R.color.transparent);
        }
        catch (Exception e)
        {
            Toast.makeText(SunMoonDisplay.this,"Exception"+e,Toast.LENGTH_SHORT).show();
        }
    }

    public void display_moon(View view)
    {
        StringBuilder stringBuilder = new StringBuilder("");
        int[] x;
        int[] date;


        year_x = Integer.parseInt(year);
        month_x = Integer.parseInt(month);
        day_x = Integer.parseInt(day);
        try {


            int h = 12, m = 0, s = 0;
            double obsLon = longitude * SunMoonCalculator.DEG_TO_RAD, obsLat = latitude * SunMoonCalculator.DEG_TO_RAD;

            SunMoonCalculator smc = new SunMoonCalculator(year_x, month_x, day_x, h, m, s, obsLon, obsLat);

            smc.calcSunAndMoon();


            stringBuilder.append("Moon:" + "\n");

            date = SunMoonCalculator.getDate(smc.moonRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);
            ClockActivityRise.hours = x[3];
            ClockActivityRise.minutes = x[4];
            ClockActivityRise.seconds = x[5];

            stringBuilder.append("    Moonrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");

            rise_time = (TextView) findViewById(R.id.time_display_rise);
            rise_time.setText("Moonrise: " + x[3] +":" + x[4]);


            date = SunMoonCalculator.getDate(smc.moonSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);
            ClockActivitySet.hours = x[3];
            ClockActivitySet.minutes = x[4];
            ClockActivitySet.seconds = x[5];

            stringBuilder.append("    MoonSet:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");

            set_time =(TextView) findViewById(R.id.time_display_set);
            set_time.setText("Moonset: " + x[3]+":"+x[4]);


            stringBuilder.append("    Azimuth:   " + (float)(smc.moonAz * 57.29577951308232D) + "º"+"\n");
            stringBuilder.append("    Elevation:   " + (float)(smc.moonEl * 57.29577951308232D) + "º"+"\n");
            stringBuilder.append("    Dist:    " + (float)(smc.moonDist * 1.49597870691E8D) + " km"+"\n");

            date = SunMoonCalculator.getDate(smc.moonTransit);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Transit: " +x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5] + " (max. elev. " + (float)(smc.moonTransitElev * 57.29577951308232D)  + "Âº)"+"\n");



            clockActivityRise = (ClockActivityRise) findViewById(R.id.analog_clockRise);
            clockActivityRise.setAutoUpdate(true);

            clockActivitySet = (ClockActivitySet) findViewById(R.id.analog_clockSet);
            clockActivitySet.setAutoUpdate(true);







            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_ASTRONOMICAL);
            smc.calcSunAndMoon();
            stringBuilder.append("\n"+"\n"+"\n");
            stringBuilder.append("Astronomical"+"\n");


            date = SunMoonCalculator.getDate(smc.moonRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Moonrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");

            date = SunMoonCalculator.getDate(smc.moonSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Moonset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");




            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_NAUTICAL);
            smc.calcSunAndMoon();
            stringBuilder.append("\n"+"\n"+"\n");
            stringBuilder.append("Nautical"+"\n");

            date = SunMoonCalculator.getDate(smc.moonRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Moonrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");

            date = SunMoonCalculator.getDate(smc.moonSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Moonset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");




            smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_CIVIL);
            smc.calcSunAndMoon();
            stringBuilder.append("\n"+"\n"+"\n");
            stringBuilder.append("Civil"+"\n");

            date = SunMoonCalculator.getDate(smc.moonRise);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Moonrise:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");

            date = SunMoonCalculator.getDate(smc.moonSet);
            x= convertTime(date[0],date[1],date[2],date[3],date[4],date[5],time_zone);

            stringBuilder.append("    Moonset:   "+x[0]+"/"+x[1]+"/"+x[2]+"    "+ x[3] + ":" + x[4] + ":"+ x[5]+"\n");


            display_textview = (TextView) findViewById(R.id.SunMoonDisplay_textview);
            display_textview.setText(stringBuilder.toString());


            MoonCalculation moonCalculation = new MoonCalculation();
            int res = moonCalculation.moonPhase(x[0],x[1],x[2]);
            String res_string;
            switch(res)
            {
                case 0:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.new_moon);
                    break;
                case 1:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.waxing_crescent);
                    break;
                case 2:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.first_quarter);
                    break;
                case 3:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.waxing_gibbous);
                    break;
                case 4:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.full_moon);
                    break;

                case 5:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.waning_gibbous);
                    break;
                case 6:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.third_quarter);
                    break;
                case 7:
                    res_string = moonCalculation.phaseName(res);
                    moon_phase_text = (TextView) findViewById(R.id.moon_phase_text);
                    moon_phase_text.setText("Moon phase:  "+res_string);
                    imageView = (ImageView) findViewById(R.id.moon_phase_image);
                    imageView.setImageResource(R.drawable.waning_crescent);
                    break;

            }



        }
        catch (Exception e)
        {
            Toast.makeText(SunMoonDisplay.this,"Exception"+e,Toast.LENGTH_SHORT).show();
        }
    }

    public int[] convertTime(int year, int month, int day, int hours, int minutes, int seconds, String time_zone)
    {
        int[] send = new int[6];
        String format = "yyyy/MM/dd HH:mm:ss";

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(format);
        String dateInString = year+"/"+month+"/"+day+" "+hours+":"+minutes+":"+seconds;

        String zone;

        if(time_zone.equals("Mountain")||time_zone.equals("Arizona"))
            zone="America/Denver";
        else if (time_zone.equals("Central"))
            zone="America/Chicago";
        else if(time_zone.equals("Eastern"))
            zone = "America/New_York";
        else if(time_zone.equals("Pacific"))
            zone="America/Los_Angeles";
        else if(time_zone.equals("Hawaii"))
            zone="America/Anchorage";
        else
            zone=time_zone;

        formatter.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        try {


            Date localTime = formatter.parse(dateInString);


            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);


            sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Chicago"));
            Date gmtTime = new Date(sdf.format(localTime));

            String result =  localTime.toString();
            String time= result.substring(11,19);
            String dt = result.substring(8,10);
            String mon = result.substring(4,7);

            Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(mon);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int mont = cal.get(Calendar.MONTH) +1;


            Log.e("Result","-->"+result+"<--");
            Log.e("time-->",time);
            Log.e("date-->",dt);

            String res[] = time.split(":");
            int hr = Integer.parseInt(res[0]);
            int mn = Integer.parseInt(res[1]);
            int sc = Integer.parseInt(res[2]);
            int dy = Integer.parseInt(dt);




            if(zone.equals("America/Denver"))
            {
                hr = hr+1;
            }
            else if(zone.equals("America/Chicago"))
            {
                hr = hr+2;
            }
            else if(zone.equals("America/New_York"))
            {
                hr = hr+3;
            }
            else if(zone.equals("America/Chicago"))
            {
                hr = hr+2;
            }
            else if(zone.equals("America/Los_angeles"))
            {
                hr = hr+0;
            }
            else if(zone.equals("America/Anchorage"))
            {
                hr = hr-3;

            }
            else if(zone.equals("Pacific/Samoa"))
            {
                hr = hr-6;
            }
            else if(zone.equals("America/Puerto_Rico"))
            {
                hr = hr+3;
            }
            else if(zone.equals("Pacific/Guam"))
            {
                hr = hr-3;
            }
            else if(zone.equals("America/Adak"))
            {
                hr = hr-3;
            }
            else if(zone.equals("America/Virgin"))
            {
                hr = hr+3;
            }
            else if(zone.equals("Pacific/Saipan"))
            {
                hr = hr-3;
            }


            send[0] = year;
            send[1]= mont;
            send[2] = dy;
            send[3] = hr;
            send[4] = mn;
            send[5] = sc;
        }
        catch (Exception e)
        {
            Log.e("Exception is====" , e.getMessage());
        }



        return send;
    }



}