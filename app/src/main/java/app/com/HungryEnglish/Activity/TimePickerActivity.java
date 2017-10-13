package app.com.HungryEnglish.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.R;

public class TimePickerActivity extends AppCompatActivity {
    Calendar myCalendar = Calendar.getInstance();
    HashMap<String, String> hashMap;
    private String availableTime;
    private DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        hashMap = new HashMap<>();
        formatter = new DecimalFormat("00");
    }

    public void onSelect(View view) {
        onDate(view);
    }


    public void onDate(final View view) {
        new TimePickerDialog(TimePickerActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                availableTime = "";
                availableTime = formatter.format(hh) + ":" + formatter.format(mm);
                new TimePickerDialog(TimePickerActivity.this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hh, int mm) {
                        availableTime = availableTime + " - " + formatter.format(hh) + ":" + formatter.format(mm);
                        hashMap.put(String.valueOf(view.getTag()), availableTime);
                        if (!view.isSelected()) {
                            view.setSelected(true);
                        } else {
                            view.setSelected(false);
                        }
                    }
                }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show();
    }


    public void submit(View view) {
        availableTime = "";
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("availabletime", jsonObject.toString().replace("\"", "").replace("{", "(").replace("}", ")"));
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    public void setSelected() {
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {

        }
    }
}
