package app.com.HungryEnglish.Model.Rate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rujul on 10/2/2017.
 */

public class RateInfo {

    @SerializedName("stu_username")
    @Expose
    private String stuUsername;
    @SerializedName("stu_id")
    @Expose
    private String stuId;
    @SerializedName("stu_email")
    @Expose
    private String stuEmail;
    @SerializedName("tea_username")
    @Expose
    private String teaUsername;
    @SerializedName("tea_id")
    @Expose
    private String teaId;
    @SerializedName("tea_email")
    @Expose
    private String teaEmail;
    @SerializedName("rate_id")
    @Expose
    private String rateId;
    @SerializedName("rate")
    @Expose
    private String rate;

    @SerializedName("is_approved")
    @Expose
    private String is_approved;

    public String getIs_approved() {
        return is_approved;
    }

    public void setIs_approved(String is_approved) {
        this.is_approved = is_approved;
    }

    public String getStuUsername() {
        return stuUsername;
    }

    public void setStuUsername(String stuUsername) {
        this.stuUsername = stuUsername;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuEmail() {
        return stuEmail;
    }

    public void setStuEmail(String stuEmail) {
        this.stuEmail = stuEmail;
    }

    public String getTeaUsername() {
        return teaUsername;
    }

    public void setTeaUsername(String teaUsername) {
        this.teaUsername = teaUsername;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getTeaEmail() {
        return teaEmail;
    }

    public void setTeaEmail(String teaEmail) {
        this.teaEmail = teaEmail;
    }

    public String getRateId() {
        return rateId;
    }

    public void setRateId(String rateId) {
        this.rateId = rateId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
