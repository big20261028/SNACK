/*
 * 모듈명 : 사용자 참여형 소셜 네트워크 서비스를 위한 userpage control class
 * 작성일 : 2025.04.07
 * 작성자 : 백인기
 */
package com.ezen.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.vo.*;
import com.ezen.dto.*;
import com.ezen.util.WebUtil;

@RequestMapping(value = "/UserPage")
@Controller
public class UserpageController 
{
	@Autowired
	UserDTO userDTO;
	
	@Autowired
	BoardDTO boardDTO;
	
	@Autowired
	ReplyDTO replyDTO;
	
	@Autowired
	GuestDTO guestDTO;
	
	@Autowired
	FollowDTO followDTO;
	
	@RequestMapping(value = "/userpage.do")
	public String Userpage(@RequestParam("usernum")int usernum,
			HttpServletRequest request,HttpServletResponse response,
			Model model)
	{
		UserVO userVO = userDTO.userInfo(usernum);
		if(userVO == null) return "list";
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(1);
		searchVO.setSearchFilter(2);
		searchVO.setKeyword(userVO.getUserid());
		
		userVO.setIntro(WebUtil.Text2HTML(userVO.getIntro()));
		
		//전체 갯수
		int total = boardDTO.getTotal(searchVO);
		
		//최대 페이징
		int maxpage = total / 10;
		if( total % 10 != 0) maxpage++;
		
		//목록 조회
		List<BoardVO> boardList = boardDTO.list(searchVO);
		
		for(BoardVO boardVO : boardList)
		{
			boardVO.setTitle(WebUtil.Text2HTML(boardVO.getTitle()));
		}
		
		//시작 블럭
		int startbk = 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
		

		model.addAttribute("userVO", userVO);
		model.addAttribute("boardList", boardList);
		
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		
		UserVO login = (UserVO)request.getSession().getAttribute("login");
		//if(login.getUsernum() == userVO.getUsernum())	return "";
		
		return "UserPage/userpage";
	}
	
	@RequestMapping(value = "/board.do")
	@ResponseBody
	public ListVO getBoardList(@RequestParam(required = true) int usernum,
	                           @RequestParam(defaultValue = "1")int pageno)
	{
	    System.out.println("board.do 실행됨");

	    UserVO userVO = userDTO.userInfo(usernum);
	    if(userVO == null) return null;
	    SearchVO searchVO = new SearchVO();
	    searchVO.setPageno(pageno);
	    searchVO.setSearchFilter(2);
	    searchVO.setKeyword(userVO.getUserid());

	    //목록 조회
	    List<BoardVO> boardList = boardDTO.list(searchVO);

	    for(BoardVO boardVO : boardList)
		{
			boardVO.setTitle(WebUtil.Text2HTML(boardVO.getTitle()));
		}
	    
	    //전체 갯수
	    int total = boardDTO.getTotal(searchVO);

	    //최대 페이징
	    int maxpage = total / 10;
	    if( total % 10 != 0) maxpage++;

	    //시작 블럭
	    int startbk = 1;
	    int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
	    if( endbk > maxpage ) endbk = maxpage;

	    ListVO<BoardVO> listVO = new ListVO<BoardVO>(); // BoardVO 타입으로 생성
	    listVO.setTotal(total);
	    listVO.setMaxpage(maxpage);
	    listVO.setStartbk(startbk);
	    listVO.setEndbk(endbk);
	    listVO.setSearchVO(searchVO);
	    listVO.setList(boardList);

	    return listVO;
	}
	
	@RequestMapping(value = "/reply.do")
	@ResponseBody
	public ListVO Reply(@RequestParam(required = true) int usernum,
            @RequestParam(defaultValue = "1")int pageno)
	{
		System.out.println("reply.do 실행됨");
		
	    SearchVO searchVO = new SearchVO();
	    searchVO.setPageno(pageno);
	    searchVO.setUsernum(usernum);
	    
	    //목록 조회
	    List<ReplyVO> replyList = replyDTO.userReplyList(searchVO);
	    
	    for(ReplyVO replyVO : replyList)
		{
			replyVO.setRnote(WebUtil.Text2HTML(replyVO.getRnote()));
		}
	    
	    //전체 갯수
	    int total = replyDTO.getUserReplyTotal(searchVO);

	    //최대 페이징
	    int maxpage = total / 10;
	    if( total % 10 != 0) maxpage++;

	    //시작 블럭
	    int startbk = 1;
	    int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
	    if( endbk > maxpage ) endbk = maxpage;

	    ListVO<ReplyVO> listVO = new ListVO<ReplyVO>(); // ReplyVO 타입으로 생성
	    listVO.setTotal(total);
	    listVO.setMaxpage(maxpage);
	    listVO.setStartbk(startbk);
	    listVO.setEndbk(endbk);
	    listVO.setSearchVO(searchVO);
	    listVO.setList(replyList);
		 
		return listVO;
	}
	@RequestMapping(value = "/guest.do")
	@ResponseBody
	public ListVO Guest(@RequestParam(required = true) int usernum,
            @RequestParam(defaultValue = "1")int pageno)
	{
		System.out.println("reply.do 실행됨");
	    
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(pageno);
		searchVO.setUsernum(usernum);
		
		//목록 조회
	    List<GuestVO> guestList = guestDTO.guestList(usernum, pageno);
	    
	    for(GuestVO guestVO : guestList)
		{
	    	guestVO.setGnote(WebUtil.Text2HTML(guestVO.getGnote()));
		}
	    
	    //전체 갯수
	    int total = guestDTO.getTotalGuest(usernum);
	    
	    //최대 페이징
	    int maxpage = total / 10;
	    if( total % 10 != 0) maxpage++;

	    //시작 블럭
	    int startbk = 1;
	    int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
	    if( endbk > maxpage ) endbk = maxpage;
	    
	    ListVO<GuestVO> listVO = new ListVO<GuestVO>(); // ReplyVO 타입으로 생성
	    listVO.setTotal(total);
	    listVO.setMaxpage(maxpage);
	    listVO.setStartbk(startbk);
	    listVO.setEndbk(endbk);
	    listVO.setSearchVO(searchVO);
	    listVO.setList(guestList);
	    
		return listVO;
	}
	//방명록 쓰기
	@RequestMapping(value = "/guestInsert.do")
	@ResponseBody
	public String guestInsert(GuestVO guestVO)
	{
		if(guestDTO.insert(guestVO)) return "Y";
		return "N";
	}
	//방명록 삭제
	@RequestMapping(value = "/guestDelete.do")
	@ResponseBody
	public String guestDelete(@RequestParam("gno")int gno, @RequestParam("guestnum")int guestnum)
	{
		if(guestDTO.delete(guestnum, gno)) return "Y";
		return "N";
	}
	//팔로우 조회
	@RequestMapping(value = "/follow.do")
	public String Follow(@RequestParam("usernum")int fromnum,Model model)
	{
		UserVO userVO = userDTO.userInfo(fromnum);
		if(userVO == null) return "list";
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(1);
		searchVO.setUsernum(fromnum);
		
		//전체 갯수
		int total = followDTO.getTotalFollow(fromnum);
		
		//최대 페이징
		int maxpage = total / 10;
		if( total % 10 != 0) maxpage++;

		//시작 블럭
		int startbk = 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
		
		//목록 조회
		List<FollowVO> followList = followDTO.followList(searchVO) ;
		
		for(FollowVO followVO : followList)
		{
			followVO.setIntro(WebUtil.Text2HTML(followVO.getIntro()));
		}
		
		model.addAttribute("userVO", userVO);
		model.addAttribute("followList", followList);
		
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		System.out.println(total);
		return "UserPage/follow";
	}

	//팔로우 목록 페이지
	@RequestMapping(value = "/followList.do")
	@ResponseBody
	public ListVO followList(@RequestParam(required = true) int usernum,
            @RequestParam(defaultValue = "1")int pageno)
	{
		System.out.println("followList.do 실행됨");
		
	    SearchVO searchVO = new SearchVO();
	    searchVO.setPageno(pageno);
	    searchVO.setUsernum(usernum);
	    
	    //목록 조회
	    List<FollowVO> followList = followDTO.followList(searchVO);
	    
	    for(FollowVO followVO : followList)
		{
			followVO.setIntro(WebUtil.Text2HTML(followVO.getIntro()));
		}
	    
	    //전체 갯수
	    int total = followDTO.getTotalFollow(usernum);

	    //최대 페이징
	    int maxpage = total / 10;
	    if( total % 10 != 0) maxpage++;

	    //시작 블럭
	    int startbk = 1;
	    int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
	    if( endbk > maxpage ) endbk = maxpage;

	    ListVO<FollowVO> listVO = new ListVO<FollowVO>(); // ReplyVO 타입으로 생성
	    listVO.setTotal(total);
	    listVO.setMaxpage(maxpage);
	    listVO.setStartbk(startbk);
	    listVO.setEndbk(endbk);
	    listVO.setSearchVO(searchVO);
	    listVO.setList(followList);
		 
		return listVO;
	}
	
	
	
	//팔로워 조회
	@RequestMapping(value = "/follower.do")
	public String Follower(@RequestParam("usernum")int usernum,Model model)
	{
		UserVO userVO = userDTO.userInfo(usernum);
		if(userVO == null) return "list";
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(1);
		searchVO.setUsernum(usernum);
		
		//전체 갯수
		int total = followDTO.getTotalFollower(usernum);
		
		//최대 페이징
		int maxpage = total / 10;
		if( total % 10 != 0) maxpage++;

		//시작 블럭
		int startbk = 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
		
		//목록 조회
		List<FollowVO> followerList = followDTO.followerList(searchVO);
		
		for(FollowVO followVO : followerList)
		{
			followVO.setIntro(WebUtil.Text2HTML(followVO.getIntro()));
		}
		
		model.addAttribute("userVO", userVO);
		model.addAttribute("followerList", followerList);
		
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		System.out.println(total);
		return "UserPage/follower";
	}
	
	//팔로워 목록 페이지
	@RequestMapping(value = "/followerList.do")
	@ResponseBody
	public ListVO followerList(@RequestParam(required = true) int usernum,
            @RequestParam(defaultValue = "1")int pageno)
	{
		System.out.println("followerList.do 실행됨");
		
	    SearchVO searchVO = new SearchVO();
	    searchVO.setPageno(pageno);
	    searchVO.setUsernum(usernum);
	    
	    //목록 조회
	    List<FollowVO> followerList = followDTO.followerList(searchVO);
	    
	    for(FollowVO followVO : followerList)
		{
			followVO.setIntro(WebUtil.Text2HTML(followVO.getIntro()));
		}
		
	    //전체 갯수
	    int total = followDTO.getTotalFollower(usernum);

	    //최대 페이징
	    int maxpage = total / 10;
	    if( total % 10 != 0) maxpage++;

	    //시작 블럭
	    int startbk = (pageno - 1) / 10 * 10 + 1;
	    int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
	    if( endbk > maxpage ) endbk = maxpage;

	    ListVO<FollowVO> listVO = new ListVO<FollowVO>(); // ReplyVO 타입으로 생성
	    listVO.setTotal(total);
	    listVO.setMaxpage(maxpage);
	    listVO.setStartbk(startbk);
	    listVO.setEndbk(endbk);
	    listVO.setSearchVO(searchVO);
	    listVO.setList(followerList);
		 
		return listVO;
	}
	
	//팔로우 여부 확인
	@RequestMapping(value = "/isSubscribe.do")
	@ResponseBody
	public String isSubscribe(@RequestParam("fromnum")int fromnum, @RequestParam("tonum")int tonum)
	{
		int x = followDTO.isFollow(fromnum, tonum);
		if(x==1) return "Y";
		return "N";
	}
}
