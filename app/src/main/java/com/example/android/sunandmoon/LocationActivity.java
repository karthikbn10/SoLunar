package com.example.android.sunandmoon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class LocationActivity extends AppCompatActivity {

    DatabaseHelper dbHeplper;
    //ListView lvUsers;
    //ListAdapter adapter;
    public static  AutoCompleteTextView lvUsers;
    ArrayAdapter adapter;
    TextView location_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        dbHeplper = new DatabaseHelper(getApplicationContext());
        try {
            dbHeplper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //lvUsers = (ListView)findViewById(lvUsers);
        lvUsers = (AutoCompleteTextView) findViewById(R.id.autocomplete);

        List<String> listUsers = dbHeplper.getAllUsers();


        if (listUsers != null) {
            adapter = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, listUsers);
            lvUsers.setAdapter(adapter);
            lvUsers.setThreshold(1);

        }

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }


        });
    }

    public  void go_back(View view)
    {
        String str;
        str = lvUsers.getText().toString();

        MainMenuDisplay.location_txt.setText(str);


        finish();
    }


}
