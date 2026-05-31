/*
 * 모듈명 : userVO 클래스에 대한 동작을 처리하는 클래스
 * 작성일 : 2025.04.09.
 * 작성자 : 백인기
 */
package com.ezen.dto;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.vo.SearchVO;
import com.ezen.vo.UserVO;

@Repository
public class UserDTO 
{
	@Autowired
	private SqlSession session;
	
	private final static String NAMESPACE = "com.ezen.user";
	
	/**
	 * 주어진 userid와 email를 이용하여 중복된 회원아이디인지 검사
	 * @param userid - 사용자 아이디
	 * @param email - 사용자 이메일
	 * @return true - 중복 안됨, false - 중복 됨
	 */
	public boolean isDupUser(String userid, String email)
	{
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		userVO.setEmail(email);
		int total = session.selectOne(NAMESPACE + ".dupcheck", userVO);
		if(total > 0) return false;
		return true;
	}
	
	/**
	 * userid 중복 체크
	 * @param userid - 사용자 아이디
	 * @return true - 중복안됨, false - 중복됨
	 */
	public boolean isDupId(String userid)
	{
		int total = session.selectOne(NAMESPACE + ".idDupCheck", userid);
		if(total > 0) return false;
		return true;
	}
	
	/**
	 * email 중복 체크
	 * @param email - 사용자 이메일
	 * @return true - 중복안됨, false - 중복됨
	 */
	public boolean isDupEmail(String email)
	{
		int total = session.selectOne(NAMESPACE + ".emailDupCheck", email);
		if(total > 0) return false;
		return true;
	}
	
	/**
	 * 주어진 userVO 객체를 이용하여 회원가입
	 * @param userVO - 사용자 정보 객체
	 * @return true - 회원가입 성공, 오류 - 실패	
	 */
	public boolean join(UserVO userVO)
	{
		try {
		session.insert(NAMESPACE + ".join",userVO);
		return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 유저 계정의 상태를 확인하는 기능
	 * @param userid - 유저 아이디
	 * @return "Y" - 정상, "N" - 탈퇴, "B" - 사용정지
	 */
	public String Status(String userid)
	{
		String status = session.selectOne(NAMESPACE + ".status", userid);
		return status;
	}
	
	/**
	 * 로그인 처리 기능
	 * @param userid - 아이디
	 * @param userpw - 비밀번호 
	 * @return null - 로그인 실패, 객체 - 로그인 정보 객체
	 */
	public UserVO login(String userid, String userpw)
	{
		UserVO userVO = new UserVO();
		userVO.setUserid(userid);
		userVO.setUserpw(userpw);
		UserVO loginVO = session.selectOne(NAMESPACE + ".login",userVO);
		return loginVO;
	}
	
	/**
	 * 자기소개 변경 기능
	 * @param usernum - 유저번호
	 * @param intro - 자기소개 
	 * @return true - 변경 성공, false - 실패	
	 */
	public boolean intro(int usernum, String intro)
	{
		try 
		{
			int total = session.selectOne(NAMESPACE + ".usercheck", usernum);
			if(total == 0) return false;
			UserVO vo = new UserVO();
			vo.setUsernum(usernum);
			vo.setIntro(intro);
			session.update(NAMESPACE + ".intro", vo);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 이메일 변경 기능
	 * @param usernum - 유저번호
	 * @param email - 변경할 이메일
	 * @return true - 변경 성공, false - 실패	
	 */
	public boolean emailUpdate(int usernum, String email)
	{
		try 
		{
			int total = session.selectOne(NAMESPACE + ".usercheck", usernum);
			if(total == 0) return false;
			UserVO vo = new UserVO();
			vo.setUsernum(usernum);
			vo.setEmail(email);
			session.update(NAMESPACE + ".emailupdate", vo);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 *  비밀번호 변경 기능
	 * @param usernum - 유저번호
	 * @param userpw - 변경할 비밀번호 
	 * @return true - 변경 성공, false - 실패	
	 */
	public boolean userPwUpdate(int usernum, String userpw)
	{
		try 
		{
			int total = session.selectOne(NAMESPACE + ".usercheck", usernum);
			if(total == 0) return false; 
			UserVO vo = new UserVO();
			vo.setUsernum(usernum);
			vo.setUserpw(userpw);
			session.update(NAMESPACE + ".userupdate", vo);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 비밀번호 변경 기능(임시비밀번호)
	 * @param userid - 유저 아이디
	 * @param email - 유저 이메일
	 * @param userpw - 변경할 비밀번호
	 * @return true - 변경 성공, false - 실패	
	 */
	public boolean userPwUpdate(String userid, String email, String userpw)
	{
		try 
		{
			UserVO vo = new UserVO();
			vo.setUserid(userid);
			vo.setEmail(email);
			vo.setUserpw(userpw);
			session.update(NAMESPACE + ".pwUpdate", vo);
			return true;
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 비밀번호 검사 기능
	 * @param usernum - 유저번호
	 * @param userpw - 비밀번호 
	 * @return true - 비밀번호 일치, false - 일치하지 않음	
	 */
	public boolean isPwCollect(int usernum, String userpw)
	{
		UserVO vo = new UserVO();
		vo.setUsernum(usernum);
		vo.setUserpw(userpw);
		int total = session.selectOne(NAMESPACE + ".ispwcol", vo);
		if(total > 0) return true;
		return false;
	}
	
	/**
	 * 관리자 권한 여부 검사 기능
	 * @param usernum - 유저 번호
	 * @return 1 - 관리자, 0 - 일반 사용자
	 */
	public boolean isAdmin(int usernum)
	{
		int total = session.selectOne(NAMESPACE + ".isAdmin", usernum);
		if(total == 1) return true;
		return false;
	}
	
	/**
	 * 유저 정보 열람 기능
	 * @param usernum - 유저 번호
	 * @return userVO - 유저 정보 , null - 유저 정보 없음
	 */
	public UserVO userInfo(int usernum)
	{
		UserVO userVO = session.selectOne(NAMESPACE + ".userInfo", usernum);
		return userVO;
	}
	
	/**
	 * 등록된 계정 갯수 확인
	 * @param searchVO - 검색정보(userStatusFilter)
	 * @return int - 계정 갯수
	 */
	public int getTotal(SearchVO searchVO)
	{
		int total = session.selectOne(NAMESPACE + ".getTotal", searchVO);
		return total;
	}
	
	/**
	 * 검색 정보와 일치하는 유저 리스트를 얻는다
	 * @param searchVO - 검색 정보
	 * @return userList - 유저 리스트
	 */
	public List<UserVO> userList(SearchVO searchVO)
	{
		if(!isAdmin(searchVO.getUsernum())) return null;
		List<UserVO> userList = session.selectList(NAMESPACE + ".userList", searchVO);
		return userList;
	}
	
	/**
	 * 계정 상태를 변경한다.
	 * @param status - 변경할 상태(Y,N,B)
	 * @param usernum - 변경할 유저 번호
	 * @return true - 변경 성공, false - 실패
	 */
	public boolean userStatus(String status, int usernum)
	{
		if(status.equals("Y"))
		{
			session.update(NAMESPACE + ".statusY", usernum);
			return true;
		}
		if(status.equals("N"))
		{
			session.update(NAMESPACE + ".statusN", usernum);
			return true;
		}
		if(status.equals("B"))
		{
			session.update(NAMESPACE + ".statusB", usernum);
			return true;
		}
		return false;
	}
	
	/**
	 * 유저 정보 열람(관리자)
	 * @param usernum - 유저 번호
	 * @return userVO - 유저 정보
	 */
	public UserVO adminUserInfo(int usernum)
	{
		UserVO userVO = session.selectOne(NAMESPACE + ".adminUserInfo", usernum);
		return userVO;
	}
	/**
	 * 유저 정보 변경(관리자)
	 * @param userVO - 변경할 유저 정보
	 * @return true - 성공
	 */
	public boolean adminUserModify(UserVO userVO)
	{
		session.update(NAMESPACE + ".adminUserModify", userVO);
		return true;
	}
	
	/**
	 * 이메일로 유저 아이디 찾기
	 * @param email - 이메일
	 * @return userid - 유저아이디, null - 등록된 유저 없음.
	 */
	public String searchUserid(String email)
	{
		String userid = session.selectOne(NAMESPACE + ".searchUserid", email);
		return userid;
	}
	
	/**
	 * 사용자 프로필 이미지 변경
	 * @param userVO - 변경된 사용자 정보
	 */
	public void updateProfileImg(UserVO userVO)
	{
		session.update(NAMESPACE + ".updateProfileImg", userVO);
	}
}
