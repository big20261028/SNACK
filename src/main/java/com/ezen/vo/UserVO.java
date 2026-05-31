/*
 * 모듈명 : 테이블 user의 데이터를 표현하기 위한 클래스
 * 작성일 : 2025.04.09
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class UserVO 
{
	private int    usernum; 	//회원번호
	private String userid; 		//아이디
	private String email;		//이메일
	private String userpw;		//비밀번호
	private String name;		//이름
	private String gender;		//성별
	private String status;		//상태
	private String joindate;	//가입일자
	private String isadmin;		//관리자권한
	private String intro;		//자기소개
	private String profileimgp;	//프로필이미지물리명
	private String profileimgf;	//프로필이미지논리명
	
	private int    followCnt;   //팔로우유저수
	private int    followerCnt; //팔로워유저수
	
	
	
	//getter
	public int    getUsernum() 		{ return usernum;	  }
	public String getUserid() 		{ return userid;	  }
	public String getEmail() 		{ return email;		  }
	public String getUserpw() 		{ return userpw;	  }
	public String getName() 		{ return name;		  }
	public String getGender() 		{ return gender;	  }
	public String getStatus() 		{ return status;	  }
	public String getJoindate() 	{ return joindate;	  }
	public String getIsadmin() 		{ return isadmin;	  }
	public String getIntro() 		{ return intro;		  }
	public String getProfileimgp() 	{ return profileimgp; }
	public String getProfileimgf() 	{ return profileimgf; }
	public int    getFollowCnt()    { return followCnt;   }
	public int    getFollowerCnt()  { return followerCnt; }
	//setter
	public void setUsernum(int usernum) 			{ this.usernum     = usernum;	  }
	public void setUserid(String userid) 			{ this.userid      = userid;	  }
	public void setEmail(String email) 				{ this.email       = email;		  }
	public void setUserpw(String userpw) 			{ this.userpw      = userpw;	  }
	public void setName(String name) 				{ this.name        = name;		  }
	public void setGender(String gender) 			{ this.gender      = gender;	  }
	public void setStatus(String status) 			{ this.status      = status;	  }
	public void setJoindate(String joindate) 		{ this.joindate    = joindate;	  }
	public void setIsadmin(String isadmin) 			{ this.isadmin     = isadmin;	  }
	public void setIntro(String intro) 				{ this.intro       = intro;		  }
	public void setProfileimgp(String profileimgp) 	{ this.profileimgp = profileimgp; }
	public void setProfileimgf(String profileimgf) 	{ this.profileimgf = profileimgf; }
	public void setFollowCnt(int followCnt)         { this.followCnt   = followCnt;   }
	public void setFollowerCnt(int followerCnt)     { this.followerCnt = followerCnt; }
	
	@Override
	public String toString() {
		return "UserVO [usernum=" + usernum + ", userid=" + userid + ", email=" + email + ", userpw=" + userpw
				+ ", name=" + name + ", gender=" + gender + ", status=" + status + ", joindate=" + joindate
				+ ", isadmin=" + isadmin + ", intro=" + intro + ", profileimgp=" + profileimgp + ", profileimgf="
				+ profileimgf + ", followCnt=" + followCnt + ", followerCnt=" + followerCnt + "]";
	}
	
	
	
}
