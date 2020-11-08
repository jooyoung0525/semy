package com.code;

public class CodeBoardDTO {
   private int num, listNum;
   private String userId;
   private String category;
   private String subject;
   private String content;
   private String register_date;
   
   private String url;
   
   private int hitCount;
   
   private long gap;
   
   
   public String getUrl() {
	   return url;
   }
   public void setUrl(String url) {
	   this.url = url;
   }
   public String getCategory() {
	return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public long getGap() {
		return gap;
	}
	public void setGap(long gap) {
		this.gap = gap;
	}
	public int getNum() {
	      return num;
   }
   public void setNum(int num) {
      this.num = num;
   }
   public int getListNum() {
      return listNum;
   }
   public void setListNum(int listNum) {
      this.listNum = listNum;
   }
   public String getUserId() {
      return userId;
   }
   public void setUserId(String userId) {
      this.userId = userId;
   }
   public String getSubject() {
      return subject;
   }
   public void setSubject(String subject) {
      this.subject = subject;
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
   
   
}