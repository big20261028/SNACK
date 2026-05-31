/*
 * 모듈명 : 테이블 guest의 데이터를 표현하기 위한 클래스
 * 작성일 : 2025.04.09
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class GuestVO 
{
	private int    gno; 	 //방명록번호
	private int    hostnum;  //소유자번호
	private int    guestnum; //방문자번호
	private String gnote; 	 //방명록내용
	private String gwdate; 	 //방명록작성일자
	
	private String userid;   //방문자아이디
		
	//getter
	public int    getGno() 		{ return gno;	   }
	public int    getHostnum() 	{ return hostnum;  }
	public int    getGuestnum() { return guestnum; }
	public String getGnote() 	{ return gnote;	   }
	public String getGwdate() 	{ return gwdate;   }
	public String getUserid() 	{ return userid;   }
	//setter
	public void setGno(int gno) 		  { this.gno      = gno;	  }
	public void setHostnum(int hostnum)   { this.hostnum  = hostnum;  }
	public void setGuestnum(int guestnum) { this.guestnum = guestnum; }
	public void setGnote(String gnote) 	  { this.gnote    = gnote;	  }
	public void setGwdate(String gwdate)  { this.gwdate   = gwdate;	  }
	public void setUserid(String userid)  { this.userid   = userid;   }
	
	@Override
	public String toString() {
		return "GuestVO [gno=" + gno + ", hostnum=" + hostnum + ", guestnum=" + guestnum + ", gnote=" + gnote
				+ ", gwdate=" + gwdate + ", userid=" + userid + "]";
	}
	
	
	
}
