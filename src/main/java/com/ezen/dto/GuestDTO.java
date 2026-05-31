/*
 * 모듈명 : guestVO 클래스에 대한 동작을 처리하는 클래스
 * 작성일 : 2025.04.10.
 * 작성자 : 백인기
 */
package com.ezen.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.vo.BoardVO;
import com.ezen.vo.GuestVO;
import com.ezen.vo.ReplyVO;
import com.ezen.vo.SearchVO;
import com.ezen.vo.UserVO;

@Repository
public class GuestDTO 
{
	@Autowired
	private SqlSession session;
	
	@Autowired
	private UserDTO userDTO;
	
	private final static String namespace = "com.ezen.guest";
	
	/**
	 * 유저 페이지에 출력될 정보를 받아오는 메소드
	 * @param usernum - 유저 번호
	 * @return UserVO - 유저 정보, null - 유저 정보 없음
	 */
	public UserVO userData(int usernum)
	{
		UserVO userVO = session.selectOne(namespace + ".userData", usernum);
		if(!userVO.getStatus().equals("Y")) return null;
		return userVO;
	}
	
	/**
	 * 유저가 작성한 글의 갯수를 받아오는 메소드
	 * @param usernum - 유저 번호
	 * @return int - 작성 글 갯수, 0 - 작성 글 없음
	 */
	public int getTotalBoard(int usernum)
	{
		int total = session.selectOne(namespace + ".totalBoard", usernum);
		return total;
	}
	
	/**
	 * 유저가 작성한 글의 번호 리스트를 받아오는 메소드
	 * @param usernum - 유저 번호
	 * @param pageno - 페이지 번호
	 * @return boardNoList - 유저가 작성한 글 번호 리스트
	 */
	public List<Integer> boardNoList(int usernum, int pageno)
	{
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(pageno);
		searchVO.setUsernum(usernum);
		List<Integer> boardNoList = session.selectList(namespace + ".listBoardNo", searchVO);
		return boardNoList;
	}
	
	/**
	 * 유저가 작성한 글 리스트를 받아오는 메소드
	 * @param usernum - 유저 번호
	 * @param pageno - 페이지 번호
	 * @return boardList - 유저가 작성한 글 리스트
	 */
	public List<BoardVO> boardList(int usernum, int pageno)
	{
		List<BoardVO> boardList = new ArrayList<BoardVO>();
		List<Integer> boardNoList = boardNoList(usernum,pageno);
		for(int no : boardNoList)
		{
			BoardVO boardVO = session.selectOne(namespace + ".listBoard", no);
			boardList.add(boardVO);
		}
		return boardList;
	}
	
	/**
	 * 유저가 작성한 댓글의 갯수를 받아오는 메소드
	 * @param usernum - 유저 번호
	 * @return int - 작성 댓글 갯수, 0 - 작성 댓글 없음
	 */
	public int getTotalReply(int usernum)
	{
		int total = session.selectOne(namespace + ".totalReply", usernum);
		return total;
	}
	
	/**
	 * 유저가 작성한 댓글 리스트를 받아오는 메소드
	 * @param usernum - 유저 번호
	 * @param pageno - 페이지 번호
	 * @return replyList - 유저가 작성한 댓글 리스트
	 */
	public List<ReplyVO> replyList(int usernum, int pageno)
	{
		SearchVO searchVO = new SearchVO();
		searchVO.setUsernum(usernum);
		searchVO.setPageno(pageno);
		List<ReplyVO> replyList = session.selectList(namespace + ".listReply", searchVO);
		return replyList;
	}
	
	/**
	 * 입력된 방명록 데이터를 데이터베이스에 입력하는 메소드
	 * @param guestVO - 방명록 데이터
	 * @return true - 입력 성공, false - 실패
	 */
	public boolean insert(GuestVO guestVO)
	{
		try 
		{
			session.insert(namespace + ".insert", guestVO);
			return true;
		} catch(Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 유저의 방명록에 등록된 글 갯수를 받아오기
	 * @param usernum - 유저 번호
	 * @return int - 등록된 방명록 갯수, 0 - 등록된 방명록 없음
	 */
	public int getTotalGuest(int usernum)
	{
		int total = session.selectOne(namespace + ".totalGuest", usernum);
		return total;
	}
	
	/**
	 * 유저의 방명록 리스트 받아오기
	 * @param usernum - 유저 번호
	 * @param pageno - 페이지 번호
	 * @return guestList - 유저 방명록 리스트
	 */
	public List<GuestVO> guestList(int usernum, int pageno)
	{
		SearchVO searchVO = new SearchVO();
		searchVO.setUsernum(usernum);
		searchVO.setPageno(pageno);
		List<GuestVO> guestList = session.selectList(namespace + ".listGuest", searchVO);
		return guestList;
	}
	
	/**
	 * 로그인 유저가 작성한 방명록인지 확인하기
	 * @param usernum - 유저 번호
	 * @param gno - 방명록 번호
	 * @return true - 유저가 작성함, false - 유저가 작성하지 않음
	 */
	public boolean isYourGuest(int usernum, int gno)
	{
		GuestVO guestVO = new GuestVO();
		guestVO.setGuestnum(usernum);
		guestVO.setGno(gno);
		int total = session.selectOne(namespace + ".isYourGuest", guestVO);
		if( total == 1 ) return true;
		return false;
	}
	
	/**
	 * 방명록 삭제
	 * @param usernum - 유저 번호
	 * @param gno - 방명록 번호
	 * @return true - 삭제 성공, false - 실패
	 */
	public boolean delete(int usernum, int gno)
	{
		if(isYourGuest(usernum, gno) || userDTO.isAdmin(usernum))
		{
			session.delete(namespace + ".delete", gno);
			return true;
		}
		return false;
	}
}
