package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class fees extends AppCompatActivity {

    EditText start, end;
    TextView feesView;
    Button  backBtn;

    String[] item = {"Dog", "Cat"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    Calendar startCalendar, endCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        feesView = findViewById(R.id.FeesView);
        backBtn = findViewById(R.id.backButton);
        autoCompleteTextView = findViewById(R.id.autoComplete);
        adapterItems = new ArrayAdapter<>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        setDateTimeListeners(start, startCalendar);
        setDateTimeListeners(end, endCalendar);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("uploadStart", start.getText().toString());
                intent.putExtra("uploadEnd", end.getText().toString());
                intent.putExtra("feesView", feesView.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                calculateFees();
            }
        });


    }

    private void setDateTimeListeners(final EditText editText, final Calendar calendar) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(editText, calendar);
            }
        });
    }

    private void showDateTimePicker(final EditText editText, final Calendar calendar) {
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                showTimePicker(editText, calendar);
            }
        };

        new DatePickerDialog(
                fees.this,
                dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void showTimePicker(final EditText editText, final Calendar calendar) {
        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDateTimeLabel(editText, calendar);
                calculateFees();
            }
        };

        new TimePickerDialog(
                fees.this,
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        ).show();
    }

    private void updateDateTimeLabel(EditText editText, Calendar calendar) {
        String myFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(calendar.getTime()));
    }

    private void calculateFees() {
        String selectedItem = autoCompleteTextView.getText().toString().trim();
        if (!selectedItem.isEmpty() && startCalendar.before(endCalendar)) {
            long diffInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);

            double hourlyRate;
            if (selectedItem.equals("Dog")) {
                hourlyRate = 50.0;
            } else if (selectedItem.equals("Cat")) {
                hourlyRate = 20.0;
            } else {
                hourlyRate = 0.0;
            }

            double totalFees = diffInHours * hourlyRate;
            feesView.setText(String.format(Locale.US, "Payment: Rs: %.2f", totalFees));
        } else {
            feesView.setText("Invalid selection or timeframe");
        }
    }


}
