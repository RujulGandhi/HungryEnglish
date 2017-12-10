package app.com.HungryEnglish.Model.StudentList;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.Model.admin.AdminAddInfoDetail;

public class StudentDashboardMainInfo {

    @SerializedName("msg")
    private String msg;

    @SerializedName("teacher_info")
    private ArrayList<TeacherListResponse> teacherInfo;

    @SerializedName("data")
    private ArrayList<AdminAddInfoDetail> data;

    @SerializedName("status")
    private String status;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setTeacherInfo(ArrayList<TeacherListResponse> teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public ArrayList<TeacherListResponse> getTeacherInfo() {
        return teacherInfo;
    }

    public void setData(ArrayList<AdminAddInfoDetail> data) {
        this.data = data;
    }

    public ArrayList<AdminAddInfoDetail> getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return
                "StudentDashboardMainInfo{" +
                        "msg = '" + msg + '\'' +
                        ",teacher_info = '" + teacherInfo + '\'' +
                        ",data = '" + data + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}