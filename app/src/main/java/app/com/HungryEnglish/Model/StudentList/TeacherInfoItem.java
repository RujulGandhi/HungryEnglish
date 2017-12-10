package app.com.HungryEnglish.Model.StudentList;

import com.google.gson.annotations.SerializedName;

public class TeacherInfoItem{

	@SerializedName("total_ratings")
	private String totalRatings;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("teacherInfo")
	private TeacherInfo teacherInfo;

	@SerializedName("fullName")
	private String fullName;

	@SerializedName("id")
	private String id;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	@SerializedName("longitude")
	private String longitude;

	public void setTotalRatings(String totalRatings){
		this.totalRatings = totalRatings;
	}

	public String getTotalRatings(){
		return totalRatings;
	}

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setTeacherInfo(TeacherInfo teacherInfo){
		this.teacherInfo = teacherInfo;
	}

	public TeacherInfo getTeacherInfo(){
		return teacherInfo;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"TeacherInfoItem{" + 
			"total_ratings = '" + totalRatings + '\'' + 
			",device_id = '" + deviceId + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",teacherInfo = '" + teacherInfo + '\'' + 
			",fullName = '" + fullName + '\'' + 
			",id = '" + id + '\'' + 
			",email = '" + email + '\'' + 
			",username = '" + username + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}