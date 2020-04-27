package com.sign.util;

import java.io.Serializable;

public class MpWxAccessToken implements Serializable{

	private static final long serialVersionUID = 461520601052886567L;
	private String appid;
	
	private String secret;
	
	private String accessToken;
	
	private long createTime;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public MpWxAccessToken(String appid, String secret, String accessToken,
                           long createTime) {
		super();
		this.appid = appid;
		this.secret = secret;
		this.accessToken = accessToken;
		this.createTime = createTime;
	}

	public MpWxAccessToken() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MpWxAccessToken{" +
				"appid='" + appid + '\'' +
				", accessToken='" + accessToken + '\'' +
				", createTime=" + createTime +
				'}';
	}
}
