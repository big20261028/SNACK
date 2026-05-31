/*
 * 모듈명 : 테이블 recommend의 데이터를 표현하기 위한 클래스
 * 작성일 : 2025.04.09
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class RecommendVO 
{
	private int recno; 	 //추천번호
	private int usernum; //회원번호
	private int no; 	 //게시물번호
	
	//getter
	public int getRecno() 	{ return recno;	  }
	public int getUsernum()	{ return usernum; }
	public int getNo() 		{ return no;      }
	//setter
	public void setRecno(int recno) 	{ this.recno   = recno;   }
	public void setUsernum(int usernum)	{ this.usernum = usernum; }
	public void setNo(int no) 			{ this.no      = no;      }
	
	@Override
	public String toString() {
		return "RecommendVO [recno=" + recno + ", usernum=" + usernum + ", no=" + no + "]";
	}
	
	
}
