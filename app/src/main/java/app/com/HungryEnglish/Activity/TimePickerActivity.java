package app.com.HungryEnglish.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityTimePickerBinding;

public class TimePickerActivity extends BaseActivity {
    Calendar myCalendar = Calendar.getInstance();
    private ArrayList<TimerModel> arrayTimer;
    private String availableTime;
    private DecimalFormat formatter;
    private TimePickerDialog startTpd, endTpd;
    private int startHour, startMin;
    private ActivityTimePickerBinding binding;
    private String selectdJsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_time_picker);
        arrayTimer = new ArrayList<>();
        formatter = new DecimalFormat("00");
        if (getIntent() != null && getIntent().getExtras() != null) {
            selectdJsonArray = getIntent().getExtras().getString("availableTimeString");
            if (!selectdJsonArray.equalsIgnoreCase("")) {
                arrayTimer = getArrayFromString(selectdJsonArray);
                updateDisplayString();
            } else {
                arrayTimer = new ArrayList<>();
            }
        } else {
            arrayTimer = new ArrayList<>();
        }

    }

    private void updateDisplayString() {
        String responseString = "selected days are \"" + getDisplayString() + "\"";
        binding.responseString.setText(responseString);
    }

    private ArrayList<TimerModel> getArrayFromString(String days) {
        ArrayList<TimerModel> arrayList = new ArrayList<>();
        Type listType = new TypeToken<ArrayList<TimerModel>>() {
        }.getType();
        arrayList = new Gson().fromJson(days, listType);
        if (days.contains("Mon\"")) {
            binding.dayMon.setSelected(true);
        }
        if (days.contains("Tue\"")) {
            binding.dayTue.setSelected(true);
        }
        if (days.contains("Wed\"")) {
            binding.dayWen.setSelected(true);
        }
        if (days.contains("Thu\"")) {
            binding.dayThu.setSelected(true);
        }
        if (days.contains("Fri\"")) {
            binding.dayFri.setSelected(true);
        }
        if (days.contains("Sat\"")) {
            binding.daySat.setSelected(true);
        }
        if (days.contains("Sun\"")) {
            binding.daySun.setSelected(true);
        }
        return arrayList;
    }

    public void onSelect(View view) {
        switch (view.getId()) {
            case R.id.day_mon:
                onDate(view, 1);
                break;
            case R.id.day_tue:
                onDate(view, 2);
                break;
            case R.id.day_wen:
                onDate(view, 3);
                break;
            case R.id.day_thu:
                onDate(view, 4);
                break;
            case R.id.day_fri:
                onDate(view, 5);
                break;
            case R.id.day_sat:
                onDate(view, 6);
                break;
            case R.id.day_sun:
                onDate(view, 0);
                break;
        }
    }

    public void onDate(final View parentView, final int pos) {
        endTpd = new TimePickerDialog(this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (startHour < hourOfDay || (startHour == hourOfDay && startMin < minute)) {
                    TimerModel modelDetails = new TimerModel();
                    int index = getPositionForSetData(pos);
                    String startTimeStr = formatter.format(startHour) + ":" + formatter.format(startMin);
                    String endTimeStr = formatter.format(hourOfDay) + ":" + formatter.format(minute);
                    modelDetails.setStartTime(startTimeStr);
                    modelDetails.setEndTime(endTimeStr);
                    modelDetails.setPriority(pos);
                    modelDetails.setDayString(parentView.getTag().toString());
                    parentView.setSelected(true);
                    if (index != -1) {
                        arrayTimer.set(index, modelDetails);
                    } else {
                        arrayTimer.add(modelDetails);
                    }
                    parentView.invalidate();
                    updateDisplayString();
                } else {
                    toast(R.string.err_date_selected);
                }
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);
        startTpd = new TimePickerDialog(this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startHour = hourOfDay;
                startMin = minute;
                endTpd.setCustomTitle(getTextView(getString(R.string.end_time)));
                endTpd.show();
            }
        }, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);

        if (!parentView.isSelected()) {
            startTpd.setCustomTitle(getTextView(getString(R.string.start_time)));
            startTpd.show();
        } else {
            for (int i = 0; i < arrayTimer.size(); i++) {
                if (arrayTimer.get(i).getPriority() == pos) {
                    arrayTimer.remove(i);
                }
            }
            updateDisplayString();
            parentView.setSelected(false);
            parentView.invalidate();
        }
    }

    private int getPositionForSetData(int pos) {
        int index = -1;
        for (int i = 0; i < arrayTimer.size(); i++) {
            if (arrayTimer.get(i).getPriority() == pos) {
                index = i;
            }
        }
        return index;
    }

    @NonNull
    private TextView getTextView(String title) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(R.style.title_textview);
        } else {
            textView.setTextAppearance(getApplicationContext(), R.style.title_textview);
        }
        textView.setText(title);
        textView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        return textView;
    }


    public void submit(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("availabletime", getSelectedString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public String getSelectedString() {
        Collections.sort(arrayTimer, new Comparator<TimerModel>() {
            @Override
            public int compare(TimerModel o1, TimerModel o2) {
                return ((Integer) o1.getPriority()).compareTo((Integer) o2.getPriority());
            }
        });
        String finalString = "";
        Gson gson = new Gson();
        finalString = gson.toJson(arrayTimer);
        return finalString;
    }

    public String getDisplayString() {
        Collections.sort(arrayTimer, new Comparator<TimerModel>() {
            @Override
            public int compare(TimerModel o1, TimerModel o2) {
                return ((Integer) o1.getPriority()).compareTo((Integer) o2.getPriority());
            }
        });
        return Utils.toString(arrayTimer);
    }


    public class TimerModel {

        private String dayString, startTime, endTime;
        private int priority;

        public String getDayString() {
            return dayString;
        }

        public void setDayString(String dayString) {
            this.dayString = dayString;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

//        @Override
//        public String toString() {
//            return dayString + ":" + startTime + "-" + endTime;
//        }

        @Override
        public String toString() {
            return "TimerModel{" +
                    "dayString='" + dayString + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", priority=" + priority +
                    '}';
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("availabletime", getSelectedString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
