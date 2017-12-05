package app.com.HungryEnglish.Model.admin;


import com.google.gson.annotations.SerializedName;

public class AdminAddInfoDetail {

    @SerializedName("image")
    private String images;

    @SerializedName("role")
    private String role;

    @SerializedName("cd_id")
    private String cdId;

    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    public void setImages(String images) {
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }

    public String getCdId() {
        return cdId;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return
                "AdminAddInfoDetail{" +
                        "images = '" + images + '\'' +
                        ",role = '" + role + '\'' +
                        ",cd_id = '" + cdId + '\'' +
                        ",link = '" + link + '\'' +
                        ",title = '" + title + '\'' +
                        "}";
    }
}