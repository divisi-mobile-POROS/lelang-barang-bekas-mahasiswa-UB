package com.example.acerpc.lelangbarangmahasiswaub;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    TextView tv;
    Calendar mCurrentDate;
    int day, month, year;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        tv = (TextView) findViewById(R.id.TvDate);
        btnSubmit = (Button) findViewById(R.id.btsubmit);

        mCurrentDate = Calendar.getInstance();

        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);

        month = month+1;

        tv.setText(day+"/"+month+"/"+year);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                       monthOfYear = monthOfYear+1;
                       tv.setText(dayOfMonth+"/"+monthOfYear+"/"+year);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
