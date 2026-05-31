/*
 * 모듈명 : boardVO 클래스에 대한 동작을 처리하는 클래스
 * 작성일 : 2025.04.10.
 * 작성자 : 백인기
 */
package com.ezen.dto;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.vo.BoardVO;
import com.ezen.vo.SearchVO;
import com.ezen.vo.UserVO;

@Repository
public class BoardDTO 
{
	@Autowired
	private SqlSession session;
	
	private final static String namespace = "com.ezen.board";
	
	/**
	 *  입력된 게시물 정보 객체 vo를 데이터 베이스에 등록
	 * @param vo - 게시물 정보
	 * @return true - 등록 완료, false - 실패
	 */
	public boolean insert(BoardVO boardVO)
	{	
		try 
		{
			session.insert(namespace + ".insert", boardVO);
			return true;
		} catch(Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 검색 정보와 일치하는 게시물 갯수를 얻는다.
	 * @param searchVO - 검색 정보
	 * @return int - 게시물 갯수
	 */
	public int getTotal(SearchVO searchVO)
	{
		int total = session.selectOne(namespace + ".total",searchVO);
		
		return total;
	}	
	
	/**
	 * 검색 정보와 일치하는 게시물 리스트를 얻는다.
	 * @param searchVO - 검색 정보
	 * @return boardList - 게시물 리스트
	 */
	public List<BoardVO> list(SearchVO searchVO)
	{
		List<BoardVO> boardList = session.selectList(namespace + ".list",searchVO);
		return boardList;
	}
	
	/**
	 *  게시물 번호로 정보 조회
	 * @param no - 게시물 번호
	 * @param isHit - 조회수 증가 여부
	 * @return boardVO - 조회 결과 있음, null - 조회 결과 없음
	 */
	public BoardVO view(int no,boolean isHit)
	{
		BoardVO boardVO = session.selectOne(namespace + ".view", no);
		
		if(isHit == true)
		{
			session.update(namespace + ".hit",boardVO);
		}
		return boardVO;
	}
	
	/**
	 * 입력된 게시물 정보 객체 vo로 데이터베이스 업데이트 //첨부파일 제거 기능 추가 필요
	 * @param vo - 게시물 정보
	 * @return true - 수정 완료, false - 실패
	 */
	public boolean update(BoardVO boardVO)
	{
		try 
		{
			if(view(boardVO.getNo(),false) == null) return false;
			session.update(namespace + ".update", boardVO);
			return true;
		}catch(Exception e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 게시물 번호(no)로 데이터 삭제
	 * @param no - 게시물 번호
	 * @return true - 수정완료, false - 실패
	 */
	public boolean delete(int no)
	{
		session.delete(namespace + ".delete", no);
		return true;
	}
	
	/**
	 * 로그인한 사용자의 팔로우 유저가 작성한 게시글 중, 검색 정보와 일치하는 게시글 갯수를 얻는다.
	 * @param searchVO - 검색 정보
	 * @return int - 게시물 갯수
	 */
	public int getFollowTotal(SearchVO searchVO)
	{
		int total = session.selectOne(namespace + ".followTotal",searchVO);
		return total;
	}
	
	/**
	 * 로그인한 사용자의 팔로우 유저가 작성한 게시글 중, 검색 정보와 일치하는 게시물 리스트를 얻는다.
	 * @param searchVO - 검색 정보
	 * @return boardList - 게시물 리스트
	 */
	public List<BoardVO> followList(SearchVO searchVO)
	{
		List<BoardVO> boardList = session.selectList(namespace + ".followList", searchVO);
		return boardList;
	}
}
