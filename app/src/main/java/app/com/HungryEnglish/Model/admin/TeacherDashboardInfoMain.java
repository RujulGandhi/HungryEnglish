package app.com.HungryEnglish.Model.admin;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class TeacherDashboardInfoMain {

    @SerializedName("msg")
    private String msg;

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
                "TeacherDashboardInfoMain{" +
                        "msg = '" + msg + '\'' +
                        ",data = '" + data + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}