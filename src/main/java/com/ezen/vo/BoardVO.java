/*
 * 모듈명 : 테이블 board의 데이터를 표현하기 위한 클래스
 * 작성일 : 2025.04.09
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class BoardVO 
{
	private int    no; 		//게시물번호
	private int    usernum; //회원번호
	private String title; 	//제목
	private String note; 	//내용
	private String pname; 	//물리파일명
	private String fname; 	//논리파일명
	private String wdate; 	//작성일자
	private int    hit;  	//조회수
	
	//추가항목
	private String userid;   	//작성자아이디
	private int    recCount; 	//추천수
	private int    repCount; 	//댓글수
	private String intro;	 	//자기소개
	private String profileimgp;	//프로필이미지물리명
	private String profileimgf;	//프로필이미지논리명
	private int    followCnt;   //팔로우유저수
	private int    followerCnt; //팔로워유저수
	
	//getter
	public int    getNo() 			{ return no;	  	  }
	public int    getUsernum() 		{ return usernum; 	  }
	public String getTitle() 		{ return title;	  	  }
	public String getNote() 		{ return note;	  	  }
	public String getPname() 		{ return pname;	  	  }
	public String getFname() 		{ return fname;	  	  }
	public String getWdate() 		{ return wdate;	  	  }
	public int    getHit() 			{ return hit;	  	  }
	public String getUserid() 		{ return userid;  	  }
	public int    getRecCount()		{ return recCount;	  }
	public int    getRepCount()		{ return repCount;	  }
	public String getIntro() 	    { return intro;	  	  }
	public String getProfileimgp() 	{ return profileimgp; }
	public String getProfileimgf() 	{ return profileimgf; }
	public int    getFollowCnt()    { return followCnt;   }
	public int    getFollowerCnt()  { return followerCnt; }
	//setter
	public void setNo(int no) 	    				{ this.no          = no;       	  }
	public void setUsernum(int usernum) 			{ this.usernum     = usernum;  	  }
	public void setTitle(String title) 				{ this.title       = title;    	  }
	public void setNote(String note) 				{ this.note        = note;     	  }
	public void setPname(String pname) 				{ this.pname       = pname;    	  }
	public void setFname(String fname) 				{ this.fname       = fname;    	  }
	public void setWdate(String wdate) 				{ this.wdate       = wdate;    	  }
	public void setHit(int hit) 					{ this.hit         = hit;      	  }
	public void setUserid(String userid) 			{ this.userid      = userid;   	  }
	public void setRecCount(int recCount) 			{ this.recCount    = recCount; 	  }
	public void setRepCount(int repCount) 			{ this.repCount    = repCount; 	  }
	public void setIntro(String intro) 				{ this.intro       = intro;		  }
	public void setProfileimgp(String profileimgp) 	{ this.profileimgp = profileimgp; }
	public void setProfileimgf(String profileimgf) 	{ this.profileimgf = profileimgf; }
	public void setFollowCnt(int followCnt)         { this.followCnt   = followCnt;   }
	public void setFollowerCnt(int followerCnt)     { this.followerCnt = followerCnt; }
	//
	@Override
	public String toString() {
		return "BoardVO [no=" + no + ", usernum=" + usernum + ", title=" + title + ", note=" + note + ", pname=" + pname
				+ ", fname=" + fname + ", wdate=" + wdate + ", hit=" + hit + ", userid=" + userid + ", recCount="
				+ recCount + ", repCount=" + repCount + ", intro=" + intro + ", profileimgp=" + profileimgp
				+ ", profileimgf=" + profileimgf + ", followCnt=" + followCnt + ", followerCnt=" + followerCnt + "]";
	}
	
	
	
	
	
	
	
	
}
