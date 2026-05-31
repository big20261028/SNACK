/*
 * 모듈명 : 검색정보 클래스
 * 작성일 : 2025.04.14
 * 작성자 : 백인기
 */
package com.ezen.vo;

public class SearchVO 
{
	private int    pageno;       //페이지 번호
	private int    offset;       //limit 시작위치 
	private String keyword;      //검색어
	private int    searchFilter; //검색어 필터
	private int    listFilter;   //목록 정렬 방식
	private int    usernum;      //사용자 번호
	private int    no;			 //게시글 번호
	
	private int	   userSearchFilter; //유저 검색어 필터
	private int    userStatusFilter; //유저 상태 필터
	
	
	
	//getter
	public int    getPageno()  		{ return pageno;       } 
	public int    getOffset()  		{ return offset;       }
	public String getKeyword() 		{ return keyword;      }
	public int    getSearchFilter() { return searchFilter; }
	public int    getListFilter()   { return listFilter;   }
	public int    getUsernum()      { return usernum;      }
	public int 	  getNo() 			{ return no;           }
	public int 	  getUserSearchFilter() { return userSearchFilter; }
	public int 	  getUserStatusFilter() { return userStatusFilter; }
	
	public String getOrderkey()
	{
		switch(listFilter)
		{
			case 1  : return "no desc";
			case 2  : return "no";
			case 3  : return "recCount desc";
			case 4  : return "hit desc";
			default : return "no desc";
		}
	}
	
	public String getUserStatus()
	{
		switch(userStatusFilter)
		{
			case 1  : return "Y";
			case 2  : return "N";
			case 3  : return "B";
			default : return "";
		}
	}
	
	//setter
	public void setPageno(int pageno)      
	{ 
		this.pageno = pageno;
		this.offset = (this.pageno - 1) * 10;
	}
	public void setKeyword(String keyword) 		  { this.keyword      = keyword;      }
	public void setSearchFilter(int searchFilter) { this.searchFilter = searchFilter; }
	public void setListFilter(int listFilter)     { this.listFilter   = listFilter;   }
	public void setUsernum(int usernum)           { this.usernum      = usernum;      }
	public void setNo(int no) 					  { this.no 		  = no;			  }
	public void setUserSearchFilter(int userSearchFilter) {this.userSearchFilter = userSearchFilter;}
	public void setUserStatusFilter(int userStatusFilter) {this.userStatusFilter = userStatusFilter;}
	
}
