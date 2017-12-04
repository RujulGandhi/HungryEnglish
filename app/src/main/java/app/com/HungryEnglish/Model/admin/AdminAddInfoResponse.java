package app.com.HungryEnglish.Model.admin;

import com.google.gson.annotations.SerializedName;

public class AdminAddInfoResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private AdminAddInfoDetail adminAddInfoDetail;

	@SerializedName("status")
	private String status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setAdminAddInfoDetail(AdminAddInfoDetail adminAddInfoDetail){
		this.adminAddInfoDetail = adminAddInfoDetail;
	}

	public AdminAddInfoDetail getAdminAddInfoDetail(){
		return adminAddInfoDetail;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"AdminAddInfoResponse{" + 
			"msg = '" + msg + '\'' + 
			",adminAddInfoDetail = '" + adminAddInfoDetail + '\'' +
			",status = '" + status + '\'' + 
			"}";
		}
}