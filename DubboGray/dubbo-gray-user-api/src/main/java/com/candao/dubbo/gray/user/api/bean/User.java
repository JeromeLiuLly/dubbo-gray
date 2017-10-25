package com.candao.dubbo.gray.user.api.bean;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -2741112503223057473L;

	/** 用户id*/
	public int uid;
	public String userName = "";
	
	/** 用户昵称，原系统username */
	public String nickName = "";
	
	/** 手机号码 */
	public String phone = "";

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
