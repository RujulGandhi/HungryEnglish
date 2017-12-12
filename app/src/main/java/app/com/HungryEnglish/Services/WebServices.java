package app.com.HungryEnglish.Services;


import com.squareup.okhttp.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import app.com.HungryEnglish.Model.Address;
import app.com.HungryEnglish.Model.ForgotPassord.ForgotPasswordModel;
import app.com.HungryEnglish.Model.Profile.StudentGetProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.StudentProfileMainResponse;
import app.com.HungryEnglish.Model.Profile.TeacherProfileMainResponse;
import app.com.HungryEnglish.Model.Rate.RateBasicResponse;
import app.com.HungryEnglish.Model.RemoveTeacher.BasicResponse;
import app.com.HungryEnglish.Model.Report.ReportModel;
import app.com.HungryEnglish.Model.StudentList.InfoWithTeacherList;
import app.com.HungryEnglish.Model.StudentList.StudentDashboardMainInfo;
import app.com.HungryEnglish.Model.StudentList.StudentListMainResponse;
import app.com.HungryEnglish.Model.Teacher.InfoMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherPendingRequestMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherProfileMain;
import app.com.HungryEnglish.Model.admin.AddInfoResponse;
import app.com.HungryEnglish.Model.admin.AdminAddInfoResponse;
import app.com.HungryEnglish.Model.admin.CountListMainResponse;
import app.com.HungryEnglish.Model.admin.TeacherDashboardInfoMain;
import app.com.HungryEnglish.Model.admin.UserListResponse;
import app.com.HungryEnglish.Model.login.LoginMainResponse;
import app.com.HungryEnglish.Model.register.RegisterMainResponse;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;


public interface WebServices {

    @POST("/login.php")
    public void getLogin(@QueryMap Map<String, String> map, Callback<LoginMainResponse> callback);

    @POST("/register.php")
    public void getRegister(@QueryMap Map<String, String> map, Callback<RegisterMainResponse> callback);

    @POST("/change_password.php")
    public void resetPassword(@QueryMap Map<String, String> map, Callback<ForgotPasswordModel> callback);

    @POST("/student_profile.php")
    public void getParentProfile(@QueryMap Map<String, String> map, Callback<StudentProfileMainResponse> callback);

    @POST("/getuserbystatus.php")
    public void getTeacherList(@QueryMap Map<String, String> map, Callback<TeacherListMainResponse> callback);

    @POST("/get_count.php")
    public void getCountList(@QueryMap Map<String, String> map, Callback<CountListMainResponse> callback);

    @POST("/delete_user.php")
    public void getRemoveTeacherFromList(@QueryMap Map<String, String> map, Callback<BasicResponse> callback);

    @POST("/delete_user.php")
    public void getRemoveStudentFromList(@QueryMap Map<String, String> map, Callback<BasicResponse> callback);

    @POST("/getuserbystatus.php")
    public void getStudentList(@QueryMap Map<String, String> map, Callback<StudentListMainResponse> callback);

    @POST("/updateStatus.php")
    public void acceptTeacherPendingRequest(@QueryMap Map<String, String> map, Callback<TeacherPendingRequestMainResponse> callback);

    @POST("/get_info.php")
    public void getInfo(@QueryMap Map<String, String> map, Callback<InfoMainResponse> callback);

    @Multipart
    @POST("/add_info.php")
    public void addInfo(@QueryMap Map<String, String> map, @PartMap Map<String, TypedFile> Files, Callback<AddInfoResponse> callback);

    @POST("/add_info.php")
    public void addInfo(@QueryMap Map<String, String> map, Callback<AddInfoResponse> callback);

    @POST("/profile.php")
    public void getTeacherProfile(@QueryMap Map<String, String> map, Callback<TeacherProfileMain> callback);

    @POST("/profile.php")
    public void getStudentProfile(@QueryMap Map<String, String> map, Callback<StudentGetProfileMainResponse> callback);

    @Multipart
    @POST("/teacher_profile.php")
    public void createTeacherProfile(@QueryMap Map<String, String> map, @PartMap Map<String, TypedFile> Files, Callback<TeacherProfileMainResponse> callback);

    @POST("/teacher_profile.php")
    public void createTeacherProfile(@QueryMap Map<String, String> map, Callback<TeacherProfileMainResponse> callback);

    @GET("/add_request.php")
    public void addRequest(@QueryMap Map<String, String> map, Callback<ForgotPasswordModel> callback);

    @GET("/check_user.php")
    public void checkUser(@QueryMap Map<String, String> map, Callback<ForgotPasswordModel> callback);

    @GET("/report_list.php")
    public void getReportList(Callback<ReportModel> callback);

    @GET("/get_data.php")
    public void getAddressAdmin(@QueryMap Map<String, String> map, Callback<Address> callback);

    @GET("/get_data.php")
    public void getAddress(@QueryMap Map<String, String> map, Callback<Address> callback);

    @POST("/get_data.php")
    public void updateRating(@QueryMap Map<String, String> map, Callback<BasicResponse> callback);


    @POST("/rate.php")
    public void getAllRating(@QueryMap Map<String, String> map, Callback<RateBasicResponse> callback);

    @POST("/rate.php")
    public void updateRate(@QueryMap HashMap<String, String> hashMap, Callback<BasicResponse> callback);

    @POST("/get_users.php")
    public void getUserList(Callback<UserListResponse> callback);

    @POST("/get_info.php")
    public void getInfoWithTeacherInfo(@QueryMap Map<String, String> map, Callback<InfoWithTeacherList> callback);

    @POST("/user_isactive.php")
    public void inactiveUser(@QueryMap HashMap<String, String> hashMap, Callback<BasicResponse> callback);

    @POST("/add_links.php")
    public void addTitleLinkInfo(@QueryMap HashMap<String, String> hashMap, Callback<AdminAddInfoResponse> callback);

    @Multipart
    @POST("/add_links.php")
    public void addImageLinkInfo(@PartMap Map<String, TypedFile> Files, @QueryMap HashMap<String, String> hashMap, Callback<AdminAddInfoResponse> callback);

    @POST("/links_byrole.php")
    void getTeacherDashBoard(@QueryMap HashMap<String, String> hashMap, Callback<TeacherDashboardInfoMain> aTrue);

    @POST("/mail.php")
    void sendMail(@QueryMap HashMap<String, String> hashMap, Callback<BasicResponse> aTrue);

    @POST("/links_byrole.php")
    void getStudentDashBorad(@QueryMap HashMap<String, String> hashMap, Callback<StudentDashboardMainInfo> callBack);
}








