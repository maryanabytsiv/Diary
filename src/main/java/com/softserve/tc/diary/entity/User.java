package com.softserve.tc.diary.entity;

import com.softserve.tc.diary.dao.util.PasswordHelper;

public class User {
    
    private String uuid;
    private String nickName;
    private String firstName;
    private String secondName;
    private String address;
    private String eMail;
    private String password;
    private Sex sex;
    private String dateOfBirth;
    private String avatar;
    private Role role;
    private String session;

    
    public User() {
    
    }
    
	public User(String nickName, String firstName, String secondName,
            String address, String eMail, String password,
            Sex sex, String dateOfBirth, String avatar, Role role) {
        this.nickName = nickName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.eMail = eMail;
        this.password = password;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
        this.role = role;
    }
    
    public User(String uuid, String nickName, String firstName, String secondName,
            String address, String eMail, String password,
            Sex sex, String dateOfBirth, String avatar, Role role) {
    	this.uuid = uuid;
        this.nickName = nickName;
        this.firstName = firstName;
        this.secondName = secondName;
        this.address = address;
        this.eMail = eMail;
        this.password = password;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.avatar = avatar;
        this.role = role;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getSecondName() {
        return secondName;
    }
    
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }
    
    public String getAddress() {
        
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password =PasswordHelper.encrypt(password);
    }
    
    public String getSex() {
        if (sex == Sex.FEMALE) {
            return "FEMALE";
        } else if (sex == Sex.MALE) {
            return "MALE";
        } else
            return null;
    }
    
    public void setSex(String sex) {
        if (sex.equals("FEMALE")) {
            this.sex = Sex.FEMALE;
        } else if (sex.equals("MALE")) {
            this.sex = Sex.MALE;
        } else
            this.sex = null;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getRole() {
    	   if (role == Role.ADMIN) {
               return "ADMIN";
           } else if (role == Role.USER) {
               return "USER";
           } else {
        	   throw new IllegalArgumentException();
           }
       }
    
    public void setRole(String role) {
        if (role.equals("ADMIN")) {
            this.role = Role.ADMIN;
        } else if (role.equals("USER")) {
            this.role = Role.USER;
        } else {
     	   throw new IllegalArgumentException();
        }
    }
    
    public String getSession() {
        return session;
    }
    
    public void setSession(String session) {
        this.session = session;
    }
    
    @Override
    public String toString() {
        return "User [uuid=" + uuid + ", nickname=" + nickName
                + ", first name=" + firstName + ", second name="
                + secondName + ", address=" + address + ", email=" + eMail
                + ", password=" + password + ", sex="
                + sex + ", date of birth=" + dateOfBirth + ", avatar="
                + avatar + ", role=" + role + ", session="
                + session + "]";
    }
    
}
