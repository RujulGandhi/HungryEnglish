package app.com.HungryEnglish.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rujul on 9/13/2017.
 */

public class Address {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("address")
    @Expose
    private String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Address{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
