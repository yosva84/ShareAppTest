package com.cubanengineer.test.sharefiles.payload.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String pusername) {
		this.username = pusername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String ppassword) {
		this.password = ppassword;
	}
}
