package com.bbs_photo;

public class PhotoDTO {
 private int num;
 private String subject;
 private String userId;
 private String userName;
 private String content;
 private String register_date;
 private int hitCount;
 private String fileName; 
 private int memberClass;
 
 
public int getMemberClass() {
	return memberClass;
}
public void setMemberClass(int memberClass) {
	this.memberClass = memberClass;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public int getNum() {
	return num;
}
public void setNum(int num) {
	this.num = num;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getRegister_date() {
	return register_date;
}
public void setRegister_date(String register_date) {
	this.register_date = register_date;
}
public int getHitCount() {
	return hitCount;
}
public void setHitCount(int hitCount) {
	this.hitCount = hitCount;
}
public String getfileName() {
	return fileName;
}
public void setfileName(String fileName) {
	this.fileName = fileName;
}
 
 
}
