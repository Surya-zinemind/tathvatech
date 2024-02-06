package com.tathvatech.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tathvatech.ts.tasks.TaskDefBean;
import com.tathvatech.ts.tasks.TaskTypes;

public class UserCreateRequestTaskBean extends TaskDefBean
{
	String title;
	String userName;
	String password;
	String firstName;
	String lastName;
	String email;
	String phone;
	String userType;
	String timezone;
	Integer sitePk;
	
	public UserCreateRequestTaskBean() {
		super(TaskTypes.CreateUserRequest);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getSitePk() {
		return sitePk;
	}

	public void setSitePk(Integer sitePk) {
		this.sitePk = sitePk;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
	/**
	 * TODO:: we need to internationalize this
	 */
	@JsonIgnore
	public String getDescription()
	{
		return "New user request for " + firstName + " " + lastName;
	}
	
	public static UserCreateRequestTaskBean fromDef(String taskDef) throws Exception
	{
        ObjectMapper objectMapper = new ObjectMapper();
        UserCreateRequestTaskBean bean = objectMapper.readValue(taskDef, UserCreateRequestTaskBean.class);
        return bean;
	}
	
	
}
