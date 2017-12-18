package app.com.HungryEnglish.Activity.Teacher;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Activity.TimePickerActivity;
import app.com.HungryEnglish.Interface.OnDialogEvent;
import app.com.HungryEnglish.MapActivity;
import app.com.HungryEnglish.Model.Address;
import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.Model.RemoveTeacher.BasicResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherProfileMain;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.GPSTracker;
import app.com.HungryEnglish.Util.Mail;
import app.com.HungryEnglish.Util.RestConstant;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.ActivityTeacherProfileBinding;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static app.com.HungryEnglish.R.id.usernameStudentEdit;
import static app.com.HungryEnglish.Util.RestConstant.BASEURL;
import static app.com.HungryEnglish.Util.RestConstant.ROLE_TEACHER;
import static app.com.HungryEnglish.Util.RestConstant.SHARED_PREFS.KEY_IS_ACTIVE;
import static app.com.HungryEnglish.Util.RestConstant.SHARED_PREFS.KEY_USER_ID;
import static app.com.HungryEnglish.Util.RestConstant.SHARED_PREFS.KEY_USER_ROLE;
import static app.com.HungryEnglish.Util.RestConstant.TIMEPICKER_REQUEST_CODE;
import static app.com.HungryEnglish.Util.Utils.alert;
import static app.com.HungryEnglish.Util.Utils.getPath;
import static app.com.HungryEnglish.Util.Utils.getRealPathFromURI;


/**
 * Created by Rujul on 6/30/2017.
 */

public class TeacherProfileActivity extends BaseActivity implements
        View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private ImageView idProofImage, ivCVFileStatus, ivAudioFileStatus, ivViewCv, ivViewAudio;
    final int SELECT_PHOTO = 100;
    final int SELECT_ID_PROOF = 200;
    final int SELECT_FILE = 300;
    final int SELECT_AUDIO = 400;
    final int SELECT_ADDRESS = 500;

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    private EditText btnCvUpload, btnAudioFile, userNameEdit, emailEdit, fullNameTeacherEdit, specialSkillTeacherEdit, etMobileOrWechatId;
    // GPSTracker class
    GPSTracker gps;
    private String pathProfilePic = null, pathCvDoc = null, pathIdProofPic = null, pathAudioFile = null;
    private Button btnSubmiTeacherProfile;
    private String id = "", role = "";
    String cvFileName, audioFileName, audioPath, resumePath;
    ProgressDialog mProgressDialog;
    String CallFrom = "";
    Context mContext;
    LinearLayout layoutIdProof;
    LinearLayout layoutCV;
    EditText btn_id_proof;
    TextView avaibilityTimeTv;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private ActivityTeacherProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_profile);
        mContext = TeacherProfileActivity.this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            role = extras.getString("role");
            CallFrom = extras.getString("callFrom");
        }
        checkPermissions();
        idMapping();
        getDataFromIntent();
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


    private void getDataFromIntent() {
        if (read(KEY_USER_ROLE).equalsIgnoreCase(ROLE_TEACHER) && read(KEY_IS_ACTIVE).equalsIgnoreCase("1")) {
            synchronized (this) {
                getProfile();
                gps = new GPSTracker(this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    getAddress(latitude, longitude);
                }
            }
        } else {
            getProfile();
        }
    }

    private void idMapping() {
        idProofImage = (ImageView) findViewById(R.id.idProofImage);
        ivCVFileStatus = (ImageView) findViewById(R.id.ivCVFileStatus);
        ivAudioFileStatus = (ImageView) findViewById(R.id.ivAudioFileStatus);
        ivViewCv = (ImageView) findViewById(R.id.ivViewCv);
        ivViewAudio = (ImageView) findViewById(R.id.ivViewAudio);
        etMobileOrWechatId = (EditText) findViewById(R.id.etMobileOrWechatId);
//        currnetPlaceEdit = (EditText) findViewById(R.id.currnetPlaceEdit);
        fullNameTeacherEdit = (EditText) findViewById(R.id.fullNameTeacherEdit);
        avaibilityTimeTv = (TextView) findViewById(R.id.avaibilityDateTeacherTv);
        specialSkillTeacherEdit = (EditText) findViewById(R.id.specialSkillTeacherEdit);
        btnAudioFile = (EditText) findViewById(R.id.btn_audio_file);
        btnCvUpload = (EditText) findViewById(R.id.btn_cv_file);
        userNameEdit = (EditText) findViewById(usernameStudentEdit);
        layoutCV = (LinearLayout) findViewById(R.id.layout_cv);
        layoutIdProof = (LinearLayout) findViewById(R.id.layout_idproof);
        btn_id_proof = (EditText) findViewById(R.id.btn_id_proof);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        btnSubmiTeacherProfile = (Button) findViewById(R.id.btnSubmiTeacherProfile);
        String currentRole = read(RestConstant.SHARED_PREFS.KEY_USER_ROLE);
        if (currentRole.equalsIgnoreCase("admin")) {
            ivViewCv.setVisibility(View.VISIBLE);
            ivViewAudio.setVisibility(View.VISIBLE);
        }
        if (CallFrom.equalsIgnoreCase("student")) {
            btnSubmiTeacherProfile.setText(getApplicationContext().getString(R.string.requst_teacher));
            TextInputLayout wechatLayout = (TextInputLayout) findViewById(R.id.wechat_id_textinput);
            TextInputLayout cvLayout = (TextInputLayout) findViewById(R.id.text_input_layout_uploadCV);
            TextInputLayout idProof = (TextInputLayout) findViewById(R.id.text_input_layout_uploadIdProof);
            TextInputLayout emailLayout = (TextInputLayout) findViewById(R.id.layout_email);

            binding.hourlyRateTextinput.setVisibility(View.GONE);
            binding.currnetPlaceTv.setVisibility(View.GONE);

            btnSubmiTeacherProfile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setVisibility(View.VISIBLE);
            ivViewAudio.setOnClickListener(this);
            btnAudioFile.setVisibility(View.VISIBLE);

            binding.etNearestStation.setKeyListener(null);
            binding.radioTrue.setKeyListener(null);
            binding.radioFalse.setKeyListener(null);

            btnCvUpload.setKeyListener(null);
            btn_id_proof.setKeyListener(null);
            btnAudioFile.setKeyListener(null);
            userNameEdit.setKeyListener(null);
            emailEdit.setKeyListener(null);
            binding.currnetPlaceTv.setKeyListener(null);
            fullNameTeacherEdit.setKeyListener(null);
            avaibilityTimeTv.setKeyListener(null);
            specialSkillTeacherEdit.setKeyListener(null);
            etMobileOrWechatId.setKeyListener(null);
            wechatLayout.setVisibility(View.GONE);
            cvLayout.setVisibility(View.GONE);
            ivCVFileStatus.setVisibility(View.GONE);
            ivViewCv.setVisibility(View.GONE);
            layoutIdProof.setVisibility(View.GONE);
            layoutCV.setVisibility(View.GONE);
            emailLayout.setVisibility(View.GONE);
            binding.studentRatingLinear.setVisibility(View.VISIBLE);

        } else if (CallFrom.equalsIgnoreCase("Admin")) {
            btnSubmiTeacherProfile.setText(getApplicationContext().getString(R.string.submit));
            binding.profileImage.setOnClickListener(this);
//            idProofImage.setOnClickListener(this);
            btnCvUpload.setOnClickListener(this);
            binding.currnetPlaceTv.setOnClickListener(this);
            btnSubmiTeacherProfile.setOnClickListener(this);
            btnAudioFile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);
            btn_id_proof.setOnClickListener(this);
            binding.studentRatingLinear.setVisibility(View.GONE);
        } else {
            btnSubmiTeacherProfile.setText(getApplicationContext().getString(R.string.submit));

            binding.profileImage.setOnClickListener(this);

//            idProofImage.setOnClickListener(this);
            btnCvUpload.setOnClickListener(this);
//            binding.currnetPlaceTv.setOnClickListener(this);
            btnSubmiTeacherProfile.setOnClickListener(this);
            btnAudioFile.setOnClickListener(this);
            ivViewCv.setOnClickListener(this);
            ivViewAudio.setOnClickListener(this);
            btn_id_proof.setOnClickListener(this);
            binding.ratingBar.setClickable(false);
            binding.ratingBar.setTouchRating(false);
            binding.studentRatingLinear.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivViewCv:
                if (resumePath != "")
                    createDirectory(resumePath, RestConstant.FILE_TYPE_RESUME);
                else
                    toast(R.string.something_wrong);
                break;
            case R.id.ivViewAudio:
                if (resumePath != "")
                    createDirectory(audioPath, RestConstant.FILE_TYPE_AUDIO);
                else
                    toast(R.string.something_wrong);
                break;
            case R.id.profile_image:
                uploadImage(SELECT_PHOTO);
                break;
            case R.id.btn_id_proof:
                uploadImage(SELECT_ID_PROOF);
                break;
            case R.id.btn_cv_file:
                uploadFile();
                break;
            case R.id.btn_audio_file:
                uploadAudio();
                break;
            case R.id.btnSubmiTeacherProfile:
                if (CallFrom.equals("Student")) {
                    RequestToTeacher();
                } else {
                    if (fullNameTeacherEdit.getText().toString().equals("")) {
                        fullNameTeacherEdit.setError("Enter Name");
                        fullNameTeacherEdit.requestFocus();
                        return;
                    }
                    if (avaibilityTimeTv.getText().toString().equals("")) {
                        avaibilityTimeTv.setError("Enter Avaibility");
                        avaibilityTimeTv.requestFocus();
                        return;
                    }
                    if (specialSkillTeacherEdit.getText().toString().equals("")) {
                        specialSkillTeacherEdit.setError("Enter Special Skills");
                        specialSkillTeacherEdit.requestFocus();
                        return;
                    }

                    if (etMobileOrWechatId.getText().toString().equals("")) {
                        etMobileOrWechatId.setError("Enter Mobile No. or Wechat Id");
                        etMobileOrWechatId.requestFocus();
                        return;
                    }
                    if (binding.currnetPlaceTv.getTag() == null) {
                        toast("Enter Nearest Railway Station");
                        return;
                    }
                    updateTeacherProfile();
                    break;
                }
        }
    }


    private void uploadFile() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("*/*");
        startActivityForResult(photoPickerIntent, SELECT_FILE);
    }


    private void uploadAudio() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("audio/*");
        startActivityForResult(photoPickerIntent, SELECT_AUDIO);
    }

    private void uploadImage(int PHOTO_CONSTANT) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PHOTO_CONSTANT);
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
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case SELECT_PHOTO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathProfilePic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathProfilePic = getPath(this, data.getData());
                    }
                    Glide.with(getApplicationContext()).load(Uri.fromFile(new File(pathProfilePic))).apply(new RequestOptions().placeholder(R.drawable.ic_user_default)).into(binding.profileImage);
                    break;
                case SELECT_ID_PROOF:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathIdProofPic = getRealPathFromURI(this, data.getData());
                    } else {
                        pathIdProofPic = getPath(this, data.getData());
                    }
                    String[] spiltFileNameArray = pathIdProofPic.split("/");
                    cvFileName = spiltFileNameArray[spiltFileNameArray.length - 1];
                    btn_id_proof.setText(cvFileName);
                    Glide.with(getApplicationContext()).load(Uri.fromFile(new File(pathIdProofPic))).apply(new RequestOptions().placeholder(R.drawable.ic_user_default)).into(binding.idProofImage);
                    break;
                case SELECT_FILE:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathCvDoc = getRealPathFromURI(this, data.getData());
                    } else {
                        pathCvDoc = getPath(this, data.getData());
                    }
                    String[] spiltArray = pathCvDoc.split("/");
                    cvFileName = spiltArray[spiltArray.length - 1];
                    btnCvUpload.setText(cvFileName);
                    if (!pathCvDoc.equals("")) {
                        Glide.with(getApplicationContext()).load(R.drawable.ic_file).apply(new RequestOptions().placeholder(R.drawable.ic_user_default)).into(binding.ivCVFileStatus);
                    }
                    break;
                case SELECT_AUDIO:
                    if (Build.VERSION.SDK_INT <= 21) {
                        pathAudioFile = getRealPathFromURI(this, data.getData());
                    } else {
                        pathAudioFile = getPath(this, data.getData());
                    }
                    String[] spiltAudioArray = pathAudioFile.split("/");
                    audioFileName = spiltAudioArray[spiltAudioArray.length - 1];
                    btnAudioFile.setText(audioFileName);
                    if (!btnAudioFile.equals("")) {
                        Glide.with(getApplicationContext()).load(R.drawable.ic_file).apply(new RequestOptions().placeholder(R.drawable.ic_user_default)).into(binding.ivAudioFileStatus);
                    }
                    break;
                case TIMEPICKER_REQUEST_CODE:
                    String displayString = data.getExtras().getString("availabletime");
                    binding.avaibilityDateTeacherTv.setText(Utils.getDisplayString(displayString));
                    binding.avaibilityDateTeacherTv.setTag(displayString);
                    break;
                case SELECT_ADDRESS:
                    String address = data.getExtras().getString("address");
                    String lat = data.getExtras().getString("lat");
                    String lng = data.getExtras().getString("lng");
                    binding.currnetPlaceTv.setText(address);
                    binding.currnetPlaceTv.setTag(lat + "," + lng);
                    break;
            }
        } else {
            toast("You cancel current task.");
        }
    }

    private void getProfile() {
        if (!Utils.checkNetwork(getApplicationContext())) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        role = read(KEY_USER_ROLE);
        if (role.equalsIgnoreCase("teacher")) {
            map.put("uId", read(KEY_USER_ID));
            map.put("role", "teacher");
        } else if (role.equalsIgnoreCase("admin")) {
            map.put("uId", id);
            map.put("role", "teacher");
        } else {
            map.put("uId", id);
            map.put("role", "teacher");
            map.put("stu_id", read(KEY_USER_ID));
        }
        ApiHandler.getApiService().getTeacherProfile(map, new Callback<TeacherProfileMain>() {
            @Override
            public void success(TeacherProfileMain teacherProfileMain, Response response) {
                fullNameTeacherEdit.setText(teacherProfileMain.getData().getFullName());
                emailEdit.setText(teacherProfileMain.getData().getEmail());
                // need set tag of current tv fields.
                userNameEdit.setText(teacherProfileMain.getData().getUsername());
                etMobileOrWechatId.setText(String.valueOf(teacherProfileMain.getData().getMobNo()));
                int rating = teacherProfileMain.getData().getRating().equalsIgnoreCase("") ? 0 : Integer.parseInt(teacherProfileMain.getData().getRating());
                binding.ratingBar.setCount(rating);

                String lat = teacherProfileMain.getData().getLat();
                String lng = teacherProfileMain.getData().getLng();
                if (lat.length() > 1 && lng.length() > 1)
                    binding.currnetPlaceTv.setTag(lat + "," + lng);

                if (teacherProfileMain.getInfo() != null) {
                    String responseString = teacherProfileMain.getInfo().getAvailableTime();
                    binding.avaibilityDateTeacherTv.setTag(responseString);

                    if (teacherProfileMain.getInfo().getOnline_status().equalsIgnoreCase("1")) {
                        binding.radioTrue.setChecked(true);
                    } else {
                        binding.radioFalse.setChecked(true);
                    }

                    binding.avaibilityDateTeacherTv.setText(Utils.getDisplayString(responseString));
                    binding.currnetPlaceTv.setText(teacherProfileMain.getInfo().getAddress());
                    specialSkillTeacherEdit.setText(teacherProfileMain.getInfo().getSkills());
                    binding.ethourlyRate.setText(teacherProfileMain.getInfo().getHourly_rate());
                    binding.etNearestStation.setText(teacherProfileMain.getInfo().getNearest_station());
                    specialSkillTeacherEdit.setText(teacherProfileMain.getInfo().getSkills());

                    Glide.with(getApplicationContext()).load(BASEURL + teacherProfileMain.getInfo().getProfileImage()).apply(new RequestOptions().placeholder(R.drawable.ic_user_default)).into(binding.profileImage);
                    Glide.with(getApplicationContext()).load(BASEURL + teacherProfileMain.getInfo().getIdImage()).apply(new RequestOptions().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(binding.idProofImage);

                    resumePath = teacherProfileMain.getInfo().getResume();
                    String idProof = RestConstant.BASEURL + teacherProfileMain.getInfo().getIdImage();
                    String idImageUrl = teacherProfileMain.getInfo().getIdImage();
                    if (resumePath != null) {
                        String[] cvFileArray = resumePath.split("/");
                        if (cvFileArray.length > 1) {
                            btnCvUpload.setText(cvFileArray[cvFileArray.length - 1]);
                            Glide.with(getApplicationContext()).load(R.drawable.ic_file).apply(new RequestOptions().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(ivCVFileStatus);
                        }
                    }
                    if (idImageUrl != null) {
                        String[] idFileArray = idImageUrl.split("/");
                        if (idFileArray.length > 1) {
                            btn_id_proof.setText(idFileArray[idFileArray.length - 1]);
                            Glide.with(getApplicationContext()).load(idProof).apply(new RequestOptions().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(idProofImage);
                        } else {
                            Glide.with(getApplicationContext()).load(R.drawable.ic_file).apply(new RequestOptions().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(idProofImage);
                        }
                    }

                    audioPath = teacherProfileMain.getInfo().getAudioFile();
                    if (audioPath != null) {
                        String[] audioFileArray = audioPath.split("/");
                        if (audioFileArray.length > 1) {
                            btnAudioFile.setText(audioFileArray[audioFileArray.length - 1]);
                            Glide.with(getApplicationContext()).load(R.drawable.ic_file).apply(new RequestOptions().placeholder(R.drawable.ic_user_default).error(R.drawable.ic_user_default)).into(ivAudioFileStatus);
                        }
                    }

                    if (teacherProfileMain.getInfo().getRating() != null && teacherProfileMain.getInfo().getRating() != "") {
                        int rategivenbystudent = Integer.parseInt(teacherProfileMain.getInfo().getRating());
                        binding.studentRate.setCount(rategivenbystudent);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateTeacherProfile() {
        if (!Utils.checkNetwork(this)) {
            Utils.showCustomDialog(getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error), this);
            return;
        }

        Utils.showDialog(this);
        TypedFile proImage = null, idProof = null, resume = null, audiofile = null;
        Map<String, TypedFile> files = new HashMap<String, TypedFile>();
        if (pathProfilePic == null && pathIdProofPic == null && pathCvDoc == null && pathAudioFile == null) {
            ApiHandler.getApiService().createTeacherProfile(getTeacherProfileDetail(), new Callback<TeacherProfileMainResponse>() {
                @Override
                public void success(TeacherProfileMainResponse teacherProfileMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherProfileMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("false")) {
                        toast(teacherProfileMainResponse.getMsg());
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("true")) {
                        toast(teacherProfileMainResponse.getMsg());
                        write(KEY_IS_ACTIVE, "2");
//                        startActivity(MainActivity.class);
//                        finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    toast(getString(R.string.something_wrong));
                }
            });
        } else {
            if (pathProfilePic != null) {
                proImage = new TypedFile("multipart/form-data", new File(pathProfilePic));
                files.put("proImage", proImage);
            }

            if (pathIdProofPic != null) {
                idProof = new TypedFile("multipart/form-data", new File(pathIdProofPic));
                files.put("idProof", idProof);
            }
            if (pathCvDoc != null) {
                resume = new TypedFile("multipart/form-data", new File(pathCvDoc));
                files.put("resume", resume);
            }
            if (pathAudioFile != null) {
                audiofile = new TypedFile("multipart/form-data", new File(pathAudioFile));
                files.put("audioFile", audiofile);
            }
            ApiHandler.getApiService().createTeacherProfile(getTeacherProfileDetail(), files, new Callback<TeacherProfileMainResponse>() {

                @Override
                public void success(TeacherProfileMainResponse teacherProfileMainResponse, Response response) {
                    Utils.dismissDialog();
                    if (teacherProfileMainResponse == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus() == null) {
                        toast(getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("false")) {
                        toast(teacherProfileMainResponse.getMsg());
                        return;
                    }
                    if (teacherProfileMainResponse.getStatus().equals("true")) {
                        toast(teacherProfileMainResponse.getMsg());
                        write(KEY_IS_ACTIVE, "2");
//                        finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Utils.dismissDialog();
                    error.printStackTrace();
                    toast(getString(R.string.something_wrong));
                }
            });
        }
    }


    private void updateRating() {
        Utils.showDialog(TeacherProfileActivity.this);
        ApiHandler.getApiService().updateRating(getRatingInfo(), new Callback<BasicResponse>() {
            @Override
            public void success(BasicResponse teacherProfileMainResponse, Response response) {
                Utils.dismissDialog();
                if (teacherProfileMainResponse == null) {
                    toast(getString(R.string.something_wrong));
                    return;
                }
                if (teacherProfileMainResponse.getStatus() == null) {
                    toast(getString(R.string.something_wrong));
                    return;
                }
                if (teacherProfileMainResponse.getStatus().equals("false")) {
                    toast(getString(R.string.student_rating_sucess_msg));
                    return;
                }
                if (teacherProfileMainResponse.getStatus().equals("true")) {
                    toast(teacherProfileMainResponse.getMsg());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Utils.dismissDialog();
                error.printStackTrace();
                toast(getString(R.string.something_wrong));
            }
        });
    }

    private Map<String, String> getTeacherProfileDetail() {
        Map<String, String> map = new HashMap<>();
        if (role.equalsIgnoreCase(ROLE_TEACHER)) {
            String userId = read(KEY_USER_ID);
            map.put("uId", userId);
        } else {
            map.put("uId", id);
        }


        if (binding.radioGroup.getCheckedRadioButtonId() == R.id.radio_true) {
            map.put("online_class", "1");
        } else {
            map.put("online_class", "0");
        }
        map.put("fullname", String.valueOf(fullNameTeacherEdit.getText()));
        map.put("available_time", String.valueOf(avaibilityTimeTv.getTag()));
        map.put("address", String.valueOf(binding.currnetPlaceTv.getText()));
        map.put("skill", String.valueOf(specialSkillTeacherEdit.getText()));
        map.put("mob_no", String.valueOf(etMobileOrWechatId.getText()));
        map.put("nearest_railway", String.valueOf(binding.etNearestStation.getText()));
        map.put("hourly_rate", String.valueOf(binding.ethourlyRate.getText()));
        String latLngStr = String.valueOf(binding.currnetPlaceTv.getTag());
        String[] latLngArray = latLngStr.split(",");
        if (latLngArray.length > 1) {
            map.put("lat", String.valueOf(latLngArray[0]));
            map.put("lng", String.valueOf(latLngArray[1]));
        }
        return map;
    }

    private Map<String, String> getRatingInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("studentId", read(KEY_USER_ID));
        map.put("teacherId", id);
        map.put("rating", String.valueOf(binding.studentRate.getCount()));
        map.put("action", "rating");
        return map;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    public void logout(View view) {
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
    }


    private void RequestToTeacher() {
        alert(this, getString(R.string.requst_teacher), getString(R.string.confirm_req), getString(R.string.yes), getString(R.string.no), new OnDialogEvent() {
            @Override
            public void onPositivePressed() {
                Utils.showDialog(mContext);
                HashMap<String, String> map = new HashMap<>();
                map.put("teacherId", id);
                map.put("studentId", Utils.ReadSharePrefrence(mContext, RestConstant.SHARED_PREFS.KEY_USER_ID));
                ApiHandler.getApiService().addRequest(map, new retrofit.Callback<ForgotPasswordModel>() {
                    @Override
                    public void success(ForgotPasswordModel forgotPasswordModel, Response response) {
                        Utils.dismissDialog();
                        if (forgotPasswordModel.getStatus().toString().equals("true")) {
                            toast(forgotPasswordModel.getMsg());
                            sendEmail(Utils.ReadSharePrefrence(mContext, RestConstant.SHARED_PREFS.KEY_USER_NAME), String.valueOf(userNameEdit.getText()));
                            onBackPressed();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Utils.dismissDialog();
                        toast(getString(R.string.try_again));
                    }
                });
            }

            @Override
            public void onNegativePressed() {

            }
        });
    }


    private void getAddress(Double lat, Double lng) {
        Utils.showDialog(mContext);
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
                        final Dialog dialog = new Dialog(TeacherProfileActivity.this);
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
                                binding.currnetPlaceTv.setText(address.getData());
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


    private void sendEmail(String studentName, String TeacherName) {
        String message = "Welcome to Hungry English Club " + studentName + "Enquiry for Teacher  " + TeacherName;
        String[] recipients = {"idigi@live.com"};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
//        email.activity = mContext;
        email.m = new Mail("hungryenglishclub@gmail.com", "RuJuLgAnDhI7102");
        email.m.set_from("hungryenglishclub@gmail.com");
        email.m.setBody(message);
        email.m.set_to(recipients);
        email.m.set_subject("Hungry English CLUB");
        email.execute();
    }

    public void rateTeacher(View view) {
        binding.studentRate.getCount();
        updateRating();
    }

    public void onOpenTimePickerActivity(View view) {
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

    public void pickAddress(View view) {
        Intent in = new Intent(this, MapActivity.class);
        startActivityForResult(in, SELECT_ADDRESS);
    }

    public static void start(Context context, String id) {
        Intent intent = new Intent(context, TeacherProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("role", "teacher");
        intent.putExtra("callFrom", "Student");
        context.startActivity(intent);
    }


    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
                } else {
                }

                return true;
            } catch (AuthenticationFailedException e) {
                Log.e("EmailError", "Bad account details");
                e.printStackTrace();

//            Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (MessagingException e) {
                Log.e("EmailError", "Email failed");
                e.printStackTrace();

//            Toast.makeText(activity, "Email failed to send.", Toast.LENGTH_SHORT).show();
                return false;
            } catch (Exception e) {
                e.printStackTrace();

//            Toast.makeText(activity, "Unexpected error occured.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     * Background Async Task to download file
     */
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private String folderPath;
        private String fileName;

        public DownloadFileFromURL(String folderPath, String fileName) {
            this.folderPath = folderPath;
            this.fileName = fileName;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String fullUrl = BASEURL + f_url[0];
                URL url = new URL(fullUrl);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(folderPath + "/" + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Toast.makeText(mContext, getString(R.string.file_downloaded), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    private void createDirectory(String file_name, String type) {
        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/" + getString(R.string.app_name) + "/" + type + "/");
            if (!folder.exists()) {
                folder.mkdirs();
                new DownloadFileFromURL(folder.getAbsolutePath(), file_name.split("/")[1]).execute(file_name);
            } else {
                new DownloadFileFromURL(folder.getAbsolutePath(), file_name.split("/")[1]).execute(file_name);
            }
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
    }

}