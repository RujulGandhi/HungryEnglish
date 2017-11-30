package app.com.HungryEnglish.Model.StudentList;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.com.HungryEnglish.Model.Teacher.InfoResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;

public class InfoWithTeacherList {

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<TeacherListResponse> data;

    @SerializedName("status")
    private String status;

    @SerializedName("info")
    private InfoResponse info;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(List<TeacherListResponse> data) {
        this.data = data;
    }

    public List<TeacherListResponse> getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setInfo(InfoResponse info) {
        this.info = info;
    }

    public InfoResponse getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return
                "InfoWithTeacherList{" +
                        "msg = '" + msg + '\'' +
                        ",data = '" + data + '\'' +
                        ",status = '" + status + '\'' +
                        ",info = '" + info + '\'' +
                        "}";
    }
}