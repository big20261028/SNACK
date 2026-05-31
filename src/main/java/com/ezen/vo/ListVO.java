/*
 * 모듈명 : ajax 데이터를 전달하기 위한 클래스
 * 작성일 : 2025.04.14
 * 작성자 : 백인기
 */
package com.ezen.vo;

import java.util.List;

public class ListVO<T> 
{
	private int 	 total;
	private int 	 maxpage;
	private int 	 startbk;
	private int      endbk;
	private SearchVO searchVO;
	private List<T>  list;
	
	public int getTotal() {return total;}
	public int getMaxpage() {return maxpage;}
	public int getStartbk() {return startbk;}
	public int getEndbk() {return endbk;}
	public SearchVO getSearchVO() {return searchVO;}
	public List<T> getList() {return list;}
	
	public void setTotal(int total) {this.total = total;}
	public void setMaxpage(int maxpage) {this.maxpage = maxpage;}
	public void setStartbk(int startbk) {this.startbk = startbk;}
	public void setEndbk(int endbk) {this.endbk = endbk;}
	public void setSearchVO(SearchVO searchVO) {this.searchVO = searchVO;}
	public void setList(List<T> list) {this.list = list;}
	
	
}
