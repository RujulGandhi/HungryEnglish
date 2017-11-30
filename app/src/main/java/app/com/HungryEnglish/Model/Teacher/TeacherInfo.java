package app.com.HungryEnglish.Model.Teacher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by R'jul on 7/6/2017.
 */

public class TeacherInfo {

    @SerializedName("uId")
    @Expose
    private String uId;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("idImage")
    @Expose
    private String idImage;
    @SerializedName("resume")
    @Expose
    private String resume;
    @SerializedName("audioFile")
    @Expose
    private String audioFile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("available_time")
    @Expose
    private String availableTime;
    @SerializedName("skills")
    @Expose
    private String skills;
    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("hourly_rate")
    @Expose
    private String hourly_rate;

    @SerializedName("nearest_station")
    @Expose
    private String nearest_station;

    @SerializedName("online_class")
    @Expose
    private String online_status;

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getHourly_rate() {
        return hourly_rate;
    }

    public void setHourly_rate(String hourly_rate) {
        this.hourly_rate = hourly_rate;
    }

    public String getNearest_station() {
        return nearest_station;
    }

    public void setNearest_station(String nearest_station) {
        this.nearest_station = nearest_station;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getIdImage() {
        return idImage;
    }

    public void setIdImage(String idImage) {
        this.idImage = idImage;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
