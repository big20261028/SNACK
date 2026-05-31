/*
 * 모듈명 : 테이블 reply의 데이터를 표현하기 위한 클래스
 * 작성일 : 2025.04.09
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class ReplyVO 
{
	private int    rno; 	//댓글번호
	private int    usernum; //회원번호
	private int    no; 		//게시물번호
	private String rnote; 	//댓글내용
	private String rwdate; 	//댓글작성일자

	//추가항목
	private String userid;  //회원아이디
	
	//getter
	public int    getRno() 		{ return rno;     }
	public int    getUsernum() 	{ return usernum; }
	public int    getNo() 		{ return no;	  }
	public String getRnote() 	{ return rnote;   }
	public String getRwdate() 	{ return rwdate;  }
	public String getUserid()   { return userid;  }
	
	//setter
	public void setRno(int rno) 			{ this.rno     = rno;     }
	public void setUsernum(int usernum) 	{ this.usernum = usernum; }
	public void setNo(int no)   			{ this.no      = no;      }
	public void setRnote(String rnote) 		{ this.rnote   = rnote;   }
	public void setRwdate(String rwdate) 	{ this.rwdate  = rwdate;  }
	public void setUserid(String userid) 	{ this.userid  = userid;  }
	//
	@Override
	public String toString() {
		return "ReplyVO [rno=" + rno + ", usernum=" + usernum + ", no=" + no + ", rnote=" + rnote + ", rwdate=" + rwdate
				+ ", userid=" + userid + "]";
	}
	
	
	
	
}
