/*
 * 모듈명 : recommendVO 클래스에 대한 동작을 처리하는 클래스
 * 작성일 : 2025.04.10.
 * 작성자 : 백인기
 */
package com.ezen.dto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.vo.RecommendVO;

@Repository
public class RecommendDTO 
{
	@Autowired
	private SqlSession session;
	
	private final static String namespace = "com.ezen.recommend";
	
	
	/**
	 * 게시글 추천 여부 검사
	 * @param recVO - 추천 정보
	 * @return 1 - 추천함, 0 - 하지 않음
	 */
	public int isRecUser(RecommendVO recVO)
	{
		return session.selectOne(namespace + ".isRecUser", recVO);
	}
	
	/**
	 * 게시글 추천
	 * @param recVO - 추천 정보
	 */
	public void insert(RecommendVO recVO)
	{
		session.insert(namespace + ".insert", recVO);
	}
	
	/**
	 * 게시글 추천 취소
	 * @param recVO - 추천 정보
	 */
	public void delete(RecommendVO recVO)
	{
		session.delete(namespace + ".delete", recVO);
	}
	
	/**
	 * 게시글 추천 여부를 검사, 추천했다면 추천 취소, 추천하지 않았다면 추천 
	 * @param usernum - 사용자 번호
	 * @param no - 게시글 번호
	 * @return true - 추천, false - 추천 취소
	 */
	public boolean recommend(int usernum, int no)
	{
		RecommendVO recVO = new RecommendVO();
		recVO.setUsernum(usernum);
		recVO.setNo(no);
		if(isRecUser(recVO)==0)
		{
			insert(recVO);
			return true;
		}else
		{
			delete(recVO);
			return false;
		}
		
	}
	public boolean recommend(RecommendVO recVO)
	{
		if(isRecUser(recVO)==0)
		{
			insert(recVO);
			return true;
		}else
		{
			delete(recVO);
			return false;
		}
	}
	
	public int recCount(int no)
	{
		int total = session.selectOne(namespace + ".recCount", no);
		return total;
	}
}
