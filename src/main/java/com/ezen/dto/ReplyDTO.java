/*
 * 모듈명 : replyVO 클래스에 대한 동작을 처리하는 클래스
 * 작성일 : 2025.04.10.
 * 작성자 : 백인기
 */
package com.ezen.dto;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.vo.BoardVO;
import com.ezen.vo.ReplyVO;
import com.ezen.vo.SearchVO;

@Repository
public class ReplyDTO 
{
	@Autowired
	private SqlSession session;
	
	private final static String namespace = "com.ezen.reply";
	
	/**
	 * 댓글 정보를 데이터 베이스에 입력한다.
	 * @param replyVO - 댓글 정보
	 * @return true - 입력 성공, false - 실패
	 */
	public boolean insert(ReplyVO replyVO)
	{
		try
		{
			session.insert(namespace + ".insert", replyVO);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 로그인한 사용자가 해당 댓글을 작성했는지 검사
	 * @param replyVO - 댓글 정보(rno,usernum)
	 * @return true - 작성, false - 비작성
	 */
	public boolean isYourRep(ReplyVO replyVO)
	{
		int isadmin = session.selectOne(namespace + ".isAdmin", replyVO.getUsernum());
		if( isadmin == 1 ) return true;
		int total = session.selectOne(namespace + ".isYourRep", replyVO);
		if(total == 1) return true;
		return false;
	}
	
	
	/**
	 * 작성자가 맞는지 확인 후 댓글 삭제
	 * @param replyVO - 댓글 정보
	 * @return true - 삭제 성공, false - 실패
	 */
	public boolean delete(ReplyVO replyVO)
	{
		if(!isYourRep(replyVO)) return false;
		session.delete(namespace + ".delete", replyVO);
		return true;
	}
	
	/**
	 * 게시물에 존재하는 댓글 갯수를 얻는다.
	 * @param no - 게시물 번호
	 * @return int - 댓글 있음, 0 - 없음
	 */
	public int getTotal(int no)
	{
		int total = session.selectOne(namespace + ".total", no);
		return total;
	}
	
	/**
	 * 게시물의 댓글 리스트를 얻는다
	 * @param searchVO - 검색정보(pageno, offset, no)
	 * @return replyList - 댓글 리스트 (값이 없어도 항상 List 리턴됨, .isEmpty() 메소드로 검사)
	 */
	public List<ReplyVO> list(SearchVO searchVO)
	{
		List<ReplyVO> replyList = session.selectList(namespace + ".list", searchVO);
		return replyList;
	}
	

	/**
	 * 유저가 작성한 댓글 갯수를 얻는다.
	 * @param usernum - 유저 번호
	 * @return int - 댓글 있음, 0 - 없음
	 */
	public int getUserReplyTotal(SearchVO searchVO)
	{
		int total = session.selectOne(namespace + ".userTotal", searchVO);
		return total;
	}
	/**
	 * 유저가 작성한 댓글 리스트를 얻는다.
	 * @param searchVO - 검색정보(
	 * @return
	 */
	public List<ReplyVO> userReplyList(SearchVO searchVO)
	{
		List<ReplyVO> replyList = session.selectList(namespace + ".userReplyList", searchVO);
		return replyList;
	}
}
