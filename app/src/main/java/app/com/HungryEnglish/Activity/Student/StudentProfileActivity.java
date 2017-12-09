package app.com.HungryEnglish.Activity.Student;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.Teacher.TeacherListActivity;
import app.com.HungryEnglish.Activity.TimePickerActivity;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.MapActivity;
import app.com.HungryEnglish.Model.Address;
import app.com.HungryEnglish.Model.Profile.StudentGetProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.StudentProfileMainResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.GPSTracker;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityStudentProfileBinding;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static app.com.HungryEnglish.Util.RestConstant.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static app.com.HungryEnglish.Util.RestConstant.ROLE_STUDENT;
import static app.com.HungryEnglish.Util.RestConstant.SEX_FEMALE;
import static app.com.HungryEnglish.Util.RestConstant.SEX_MALE;
import static app.com.HungryEnglish.Util.RestConstant.SHARED_PREFS.KEY_IS_ACTIVE;
import static app.com.HungryEnglish.Util.RestConstant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.RestConstant.SHARED_PREFS.KEY_USER_ROLE;
import static app.com.HungryEnglish.Util.RestConstant.TIMEPICKER_REQUEST_CODE;


/**
 * Created by Bhadresh Chavada on 27-06-2017.
 */

public class StudentProfileActivity extends BaseActivity {

    private EditText fullNameStudentEdit, ageEdit, wechatEdt, userNameEdt, skillEdt, emailEdt;
    private RadioGroup radioSex;
    private RadioButton radioMale, radioFemale;
    private RadioButton radioButton;
    private Button login_register;
    private String sex = "";
    private String id = "", role = "";
    private ActivityStudentProfileBinding binding;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private GPSTracker gps;
    final int SELECT_ADDRESS = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_profile);
        idMapping();
        getIntentData();
        if (read(KEY_USER_ROLE) == ROLE_STUDENT && read(KEY_IS_ACTIVE).equalsIgnoreCase("0")) {
            setHomeLocation();
        } else {
            getProfile();
        }

    }

    private void setHomeLocation() {
        synchronized (this) {
            getProfile();
            gps = new GPSTracker(this);
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                getAddress(latitude, longitude);
            }
        }
    }


    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        // When role is admin.
        if (extras != null) {
            id = extras.getString("id");
            role = extras.getString("role");
        } else {
            checkPermissions();
        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 0);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast(R.string.sucess_external_storage_msg);
                } else {
                    toast(R.string.error_permission_msg);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }

    private void idMapping() {
        fullNameStudentEdit = (EditText) findViewById(R.id.fullNameStudentEdit);
        wechatEdt = (EditText) findViewById(R.id.wechatEdit);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        login_register = (Button) findViewById(R.id.login_register);
        radioSex = (RadioGroup) findViewById(R.id.radioSex);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        userNameEdt = (EditText) findViewById(R.id.usernameStudentEdit);
        skillEdt = (EditText) findViewById(R.id.skillStudentEdit);
        emailEdt = (EditText) findViewById(R.id.emailEdit);
    }

    private void callParentProfileApi() {
        if (!Utils.checkNetwork(StudentProfileActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), StudentProfileActivity.this);
            return;
        }

        //  SHOW PROGRESS DIALOG
        Utils.showDialog(StudentProfileActivity.this);
        ApiHandler.getApiService().getParentProfile(getParentProfileDetail(), new retrofit.Callback<StudentProfileMainResponse>() {
            @Override
            public void success(StudentProfileMainResponse parentProfileMainResponse, Response response) {
                Utils.dismissDialog();
                if (parentProfileMainResponse == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (parentProfileMainResponse.getStatus() == null) {
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (parentProfileMainResponse.getStatus().equals("false")) {
                    Toast.makeText(getApplicationContext(), "" + parentProfileMainResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (parentProfileMainResponse.getStatus().equals("true")) {
                    Utils.WriteSharePrefrence(StudentProfileActivity.this, KEY_IS_ACTIVE, "1");
                    if (!role.equals("")) {
                        finish();
                    } else {
                        startActivity(new Intent(StudentProfileActivity.this, TeacherListActivity.class));
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                error.getMessage();
                Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Map<String, String> getParentProfileDetail() {
        Map<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            String userId = Utils.ReadSharePrefrence(StudentProfileActivity.this, RestConstant.SHARED_PREFS.KEY_USER_ID);
            map.put("uId", "" + userId);
        } else {
            map.put("uId", id);
        }

        map.put("fullname", String.valueOf(fullNameStudentEdit.getText()));
        map.put("available_time", String.valueOf(binding.avaibilityStudentEdit.getTag()));
        map.put("age", String.valueOf(ageEdit.getText()));
        map.put("mobile", String.valueOf(wechatEdt.getText()));
        map.put("sex", String.valueOf(sex));
        map.put("station", String.valueOf(binding.nearRailwayStationEdit.getText()));
        map.put("skill", String.valueOf(skillEdt.getText().toString()));
        map.put("username", String.valueOf(userNameEdt.getText().toString()));
        String latLngStr = String.valueOf(binding.nearRailwayStationEdit.getTag());
        String[] latLngArray = latLngStr.split(",");
        if (latLngArray.length > 1) {
            map.put("lat", String.valueOf(latLngArray[0]));
            map.put("lng", String.valueOf(latLngArray[1]));
        }
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.alert(this, getString(R.string.logout), getString(R.string.logout_note), getString(R.string.logout), getString(R.string.cancel), new OnDialogEvent() {
                    @Override
                    public void onPositivePressed() {
                        clear();
                        Utils.logout(getApplicationContext());
                        finish();
                    }

                    @Override
                    public void onNegativePressed() {

                    }
                });
                break;
        }
        return true;
    }

    public void pickAddress(View view) {
        Intent in = new Intent(this, MapActivity.class);
        startActivityForResult(in, SELECT_ADDRESS);
    }

    private void getProfile() {
        if (!Utils.checkNetwork(StudentProfileActivity.this)) {
            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), StudentProfileActivity.this);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase("")) {
            map.put("uId", read(KEY_USER_ID));
            map.put("role", read(KEY_USER_ROLE));
        } else {
            map.put("uId", id);
            map.put("role", role);
        }
        ApiHandler.getApiService().getStudentProfile(map, new Callback<StudentGetProfileMainResponse>() {
            @Override
            public void success(StudentGetProfileMainResponse studentGetProfileMainResponse, Response response) {
                fullNameStudentEdit.setText(studentGetProfileMainResponse.getData().getFullName());
                wechatEdt.setText(studentGetProfileMainResponse.getData().getMobNo());
                userNameEdt.setText(studentGetProfileMainResponse.getData().getUsername());
                emailEdt.setText(studentGetProfileMainResponse.getData().getEmail());
                if (studentGetProfileMainResponse.getInfo() != null) {
                    String availableTimeString = studentGetProfileMainResponse.getInfo().getAvailableTime();
                    binding.avaibilityStudentEdit.setTag(availableTimeString);
                    String lat = studentGetProfileMainResponse.getData().getLat();
                    String lng = studentGetProfileMainResponse.getData().getLng();
                    if (lat.length() > 1 && lng.length() > 1)
                        binding.nearRailwayStationEdit.setTag(lat + "," + lng);
                    binding.avaibilityStudentEdit.setText(Utils.getDisplayString(availableTimeString));
                    ageEdit.setText(studentGetProfileMainResponse.getInfo().getAge());

                    if (studentGetProfileMainResponse.getInfo().getSex().equalsIgnoreCase(SEX_MALE)) {
                        radioMale.setChecked(true);
                    } else {
                        radioFemale.setChecked(true);
                    }
                    binding.nearRailwayStationEdit.setText(studentGetProfileMainResponse.getInfo().getStation());
                    skillEdt.setText(studentGetProfileMainResponse.getInfo().getSkills());
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void onAvailablity(View view) {
        Intent in = new Intent(this, TimePickerActivity.class);
        String availableTimeString;
        if (((TextView) view).getTag() != null)
            availableTimeString = ((TextView) view).getTag().toString();
        else {
            availableTimeString = "";
        }
        try {
            Type listType = new TypeToken<ArrayList<TimePickerActivity.TimerModel>>() {
            }.getType();
            ArrayList<TimePickerActivity.TimerModel> arrayTimer = new Gson().fromJson(availableTimeString, listType);
        } catch (JsonSyntaxException e) {
            availableTimeString = "";
        }
        in.putExtra("availableTimeString", availableTimeString);
        startActivityForResult(in, TIMEPICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TIMEPICKER_REQUEST_CODE:
                String displayString = data.getExtras().getString("availabletime");
                binding.avaibilityStudentEdit.setText(Utils.getDisplayString(displayString));
                binding.avaibilityStudentEdit.setTag(displayString);
                break;
            case SELECT_ADDRESS:
                String address = data.getExtras().getString("address");
                String lat = data.getExtras().getString("lat");
                String lng = data.getExtras().getString("lng");
                binding.nearRailwayStationEdit.setText(address);
                binding.nearRailwayStationEdit.setTag(lat + "," + lng);
                Log.d("address", String.valueOf(binding.nearRailwayStationEdit.getTag()));
                break;
        }
    }

    public void onUpdateProfile(View view) {
        if (fullNameStudentEdit.getText().toString().equalsIgnoreCase("")) {
            fullNameStudentEdit.setError("Enter Full Name");
            fullNameStudentEdit.requestFocus();
            return;
        }

        if (ageEdit.getText().toString().equalsIgnoreCase("")) {
            ageEdit.setError("Enter Age");
            ageEdit.requestFocus();
            return;
        }

        if (binding.nearRailwayStationEdit.getText().toString().equalsIgnoreCase("")) {
            toast("Enter Nearest Railway Station");
            return;
        }


        if (binding.nearRailwayStationEdit.getTag() == null) {
//            String latLngStr = String.valueOf(binding.nearRailwayStationEdit.getTag());
//            String[] latLngArray = latLngStr.split(",");
            toast("Enter Nearest Railway Station");
            return;
        }


        if (binding.avaibilityStudentEdit.getText().toString().equalsIgnoreCase("")) {
            binding.avaibilityStudentEdit.setError("Enter Avaibility");
            binding.avaibilityStudentEdit.requestFocus();
            return;
        }

        // get selected radio button from radioGroup
        int selectedId = radioSex.getCheckedRadioButtonId();
        if (selectedId == R.id.radioMale) {
            sex = SEX_MALE;
        } else {
            sex = SEX_FEMALE;
        }
        callParentProfileApi();

    }


    private void getAddress(Double lat, Double lng) {
        Utils.showDialog(StudentProfileActivity.this);
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "get_address");
        map.put("lat", String.valueOf(lat));
        map.put("lng", String.valueOf(lng));

//        http://smartsquad.16mb.com/HungryEnglish/api/add_request.php?teacherId=1&studentId=2
        ApiHandler.getApiService().getAddress(map, new retrofit.Callback<Address>() {
            @Override
            public void success(final Address address, Response response) {
                if (address != null) {
                    if (!address.getData().equalsIgnoreCase("false")) {
                        final Dialog dialog = new Dialog(StudentProfileActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.address_confirm);
                        dialog.setCancelable(false);
                        TextView tvLogout = (TextView) dialog.findViewById(R.id.address_confirm_msg);
                        tvLogout.setText("Do you want to set " + address.getData() + " address as Home location ?");
                        TextView tvOkay = (TextView) dialog.findViewById(R.id.address_positive);
                        TextView tvCancel = (TextView) dialog.findViewById(R.id.address_negative);
                        tvOkay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                binding.nearRailwayStationEdit.setText(address.getData());
                                dialog.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }

                }

                Utils.dismissDialog();


            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                toast(getString(R.string.try_again));
            }
        });

    }
}
