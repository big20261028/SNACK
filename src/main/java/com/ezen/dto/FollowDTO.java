/*
 * 모듈명 : followVO 클래스에 대한 동작을 처리하는 클래스
 * 작성일 : 2025.04.10.
 * 작성자 : 백인기
 */
package com.ezen.dto;

import java.util.*;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.vo.*;

@Repository
public class FollowDTO 
{
	@Autowired
	private SqlSession session;
	
	private final static String namespace = "com.ezen.follow";
	
	/**
	 * 유저 팔로우 여부 검사
	 * @param fromnum - 사용자 번호
	 * @param tonum - 유저 번호
	 * @return 1 - 팔로우함, 0 - 하지않음
	 */
	public int isFollow(int fromnum, int tonum)
	{
		FollowVO followVO = new FollowVO();
		followVO.setFromnum(fromnum);
		followVO.setTonum(tonum);
		return session.selectOne(namespace + ".isFollow", followVO);
	}
	
	/**
	 * 유저 팔로우 추가
	 * @param fromnum - 사용자 번호
	 * @param tonum - 유저 번호
	 */ 
	public void insert(int fromnum, int tonum)
	{
		FollowVO followVO = new FollowVO();
		followVO.setFromnum(fromnum);
		followVO.setTonum(tonum);
		session.insert(namespace + ".insert", followVO);
	}
	
	/**
	 * 유저 팔로우 제거
	 * @param fromnum - 사용자 번호
	 * @param tonum - 유저 번호
	 */
	public void delete(int fromnum, int tonum)
	{
		FollowVO followVO = new FollowVO();
		followVO.setFromnum(fromnum);
		followVO.setTonum(tonum);
		session.insert(namespace + ".delete", followVO);
	}
	
	/**
	 * 팔로우 여부를 검사, 팔로우했다면 취소, 팔로우하지 않았다면 추천 
	 * @param fromnum - 사용자 번호
	 * @param tonum - 유저 번호
	 * @return true - 팔로우 추가, false - 팔로우 제거
	 */
	public boolean follow(int fromnum, int tonum)
	{
		if(isFollow(fromnum, tonum)==0)
		{
			insert(fromnum, tonum);
			return true;
		}else
		{
			delete(fromnum, tonum);
			return false;
		}
	}
	
	/**
	 * 사용자가 팔로우한 유저 갯수 얻기
	 * @param usernum - 사용자 번호
	 * @return int - 팔로우 유저 갯수, 0 - 팔로우 유저 없음
	 */
	public int getTotalFollow(int usernum)
	{
		int total = session.selectOne(namespace + ".followTotal", usernum);
		return total;
	}
	
	/**
	 * 사용자의 팔로우 리스트 얻기
	 * @param searchVO - 검색 정보(pageno, offset, usernum)
	 * @return followList - 팔로우 리스트
	 */
	public List<FollowVO> followList(SearchVO searchVO)
	{
		List<FollowVO> followList = session.selectList(namespace + ".followList", searchVO);
		return followList;
	}
	
	/**
	 * 사용자를 팔로우한 유저 갯수 얻기
	 * @param usernum - 사용자 번호
	 * @return int - 팔로워 유저 갯수, 0 - 팔로워 없음
	 */
	public int getTotalFollower(int usernum)
	{
		int total = session.selectOne(namespace + ".followerTotal", usernum);
		return total;
	}
	
	/**
	 * 사용자의 팔로워 리스트 얻기
	 * @param searchVO - 검색 정보(pageno, offset, usernum) 
	 * @return followerList - 팔로워 리스트
	 */
	public List<FollowVO> followerList(SearchVO searchVO)
	{
		List<FollowVO> followerList = session.selectList(namespace + ".followerList", searchVO);
		return followerList;
	}
	
}
