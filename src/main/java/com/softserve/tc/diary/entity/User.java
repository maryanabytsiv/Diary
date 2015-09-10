package com.softserve.tc.diary.entity;

public class User {
	
	private int u_id;
	private String nick_name;
	private String first_name;
	private String second_name;
	private String address;
	private String e_mail;
	private String password;
	private Sex sex; 
	private String date_of_birth;
	private String avatar;
	private String role;
	

	public User() {

	}

	
	public User(int u_id, String nick_name, String first_name, String second_name, String address, String e_mail,
			String password, Sex sex, String date_of_birth, String avatar, String role) {
		this.u_id = u_id;
		this.nick_name = nick_name;
		this.first_name = first_name;
		this.second_name = second_name;
		this.address = address;
		this.e_mail = e_mail;
		this.password = password;
		this.sex = sex;
		this.date_of_birth = date_of_birth;
		this.avatar = avatar;
		this.role = role;
	}

	public int getU_id() {
		return u_id;
	}

	public void setU_id(int u_id) {
		this.u_id = u_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getSecond_name() {
		return second_name;
	}

	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}

	public String getAddress_id() {
		return address;
	}

	public void setAddress_id(String address_id) {
		this.address = address_id;
	}

	public String getE_mail() {
		return e_mail;
	}

	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole_id() {
		return role;
	}

	public void setRole_id(String role_id) {
		this.role = role_id;
	}

	@Override
	public String toString() {
		return "User [u_id=" + u_id + ", nick_name=" + nick_name + ", first_name=" + first_name + ", second_name="
				+ second_name + ", address =" + address + ", e_mail=" + e_mail + ", password=" + password
				+ ", sex=" + sex + ", date_of_birth=" + date_of_birth + ", avatar=" + avatar + ", role_id=" + role
				+ "]";
	}
	

}
