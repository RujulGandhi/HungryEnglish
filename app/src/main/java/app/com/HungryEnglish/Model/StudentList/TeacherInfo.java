package app.com.HungryEnglish.Model.StudentList;

import com.google.gson.annotations.SerializedName;

public class TeacherInfo{

	@SerializedName("available_time")
	private String availableTime;

	@SerializedName("skills")
	private String skills;

	@SerializedName("audioFile")
	private String audioFile;

	@SerializedName("address")
	private String address;

	@SerializedName("hourly_rate")
	private String hourlyRate;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("idImage")
	private String idImage;

	@SerializedName("profileImage")
	private String profileImage;

	@SerializedName("nearest_station")
	private String nearestStation;

	@SerializedName("online_class")
	private String onlineClass;

	public void setAvailableTime(String availableTime){
		this.availableTime = availableTime;
	}

	public String getAvailableTime(){
		return availableTime;
	}

	public void setSkills(String skills){
		this.skills = skills;
	}

	public String getSkills(){
		return skills;
	}

	public void setAudioFile(String audioFile){
		this.audioFile = audioFile;
	}

	public String getAudioFile(){
		return audioFile;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setHourlyRate(String hourlyRate){
		this.hourlyRate = hourlyRate;
	}

	public String getHourlyRate(){
		return hourlyRate;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setIdImage(String idImage){
		this.idImage = idImage;
	}

	public String getIdImage(){
		return idImage;
	}

	public void setProfileImage(String profileImage){
		this.profileImage = profileImage;
	}

	public String getProfileImage(){
		return profileImage;
	}

	public void setNearestStation(String nearestStation){
		this.nearestStation = nearestStation;
	}

	public String getNearestStation(){
		return nearestStation;
	}

	public void setOnlineClass(String onlineClass){
		this.onlineClass = onlineClass;
	}

	public String getOnlineClass(){
		return onlineClass;
	}

	@Override
 	public String toString(){
		return 
			"TeacherInfo{" + 
			"available_time = '" + availableTime + '\'' + 
			",skills = '" + skills + '\'' + 
			",audioFile = '" + audioFile + '\'' + 
			",address = '" + address + '\'' + 
			",hourly_rate = '" + hourlyRate + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",idImage = '" + idImage + '\'' + 
			",profileImage = '" + profileImage + '\'' + 
			",nearest_station = '" + nearestStation + '\'' + 
			",online_class = '" + onlineClass + '\'' + 
			"}";
		}
}