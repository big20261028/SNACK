/*
 * 모듈명 : 테이블 follow의 데이터를 표현하기 위한 클래스
 * 작성일 : 2025.04.09
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class FollowVO 
{
	private int    fno;    	//팔로우번호
	private int    fromnum;	//구독자번호
	private int    tonum;	//피구독자번호
	private String fwdate; 	//팔로우일자
	
	//추가항목
	private String profileimgp;	//프로필이미지물리명
	private String profileimgf;	//프로필이미지논리명 
	private String followid;    //팔로우유저아이디
	private String userid;      //유저아이디
	private String intro;		//자기소개
	

	//getter
	public int    getFno() 			{ return fno;	  	  }
	public int    getFromnum() 		{ return fromnum; 	  }
	public int    getTonum() 		{ return tonum;	  	  }
	public String getFwdate() 		{ return fwdate;      }
	public String getProfileimgp() 	{ return profileimgp; }
	public String getProfileimgf() 	{ return profileimgf; }
	public String getFollowid() 	{ return followid;	  }
	public String getUserid() 	    { return userid;      }
	public String getIntro() 		{ return intro;		  }
	//setter
	public void setFno(int fno) 					{ this.fno     	   = fno;     	  }
	public void setFromnum(int fromnum) 			{ this.fromnum 	   = fromnum; 	  }
	public void setTonum(int tonum) 				{ this.tonum   	   = tonum;   	  }
	public void setFwdate(String fwdate) 			{ this.fwdate      = fwdate;      }
	public void setProfileimgp(String profileimgp) 	{ this.profileimgp = profileimgp; }
	public void setProfileimgf(String profileimgf) 	{ this.profileimgf = profileimgf; }
	public void setFollowid(String followid) 		{ this.followid    = followid;    }
	public void setUserid(String userid)	        { this.userid      = userid;      }
	public void setIntro(String intro) 				{ this.intro       = intro;		  }
	//
	@Override
	public String toString() {
		return "FollowVO [fno=" + fno + ", fromnum=" + fromnum + ", tonum=" + tonum + ", fwdate=" + fwdate
				+ ", profileimgp=" + profileimgp + ", profileimgf=" + profileimgf + ", followid=" + followid
				+ ", userid=" + userid + ", intro=" + intro + "]";
	}
	
	
}
