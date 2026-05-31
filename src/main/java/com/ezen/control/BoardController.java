/*
 * 모듈명 : 사용자 참여형 소셜 네트워크 서비스를 위한 control class
 * 작성일 : 2025.04.07
 * 작성자 : 백인기
 */

package com.ezen.control;

import java.awt.Color;
import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import com.ezen.dto.*;
import com.ezen.util.WebUtil;
import com.ezen.vo.*;

import nl.captcha.Captcha;
import nl.captcha.Captcha.Builder;
import nl.captcha.backgrounds.FlatColorBackgroundProducer;

@Controller
public class BoardController 
{
	@Autowired
	UserDTO userDTO;
	
	@Autowired
	BoardDTO boardDTO;
	
	@Autowired
	RecommendDTO recommendDTO;
	
	@Autowired
	ReplyDTO replyDTO;
	
	@Autowired
	FollowDTO followDTO;
	
	@Autowired
	GuestDTO guestDTO;
	
	//파일 업로드 경로
	final static String uploadPath = "D:\\Big\\Spring\\Ateamproject04\\upload\\";
	
	//프로필 이미지 파일 경로
	final static String IMG_UPLOAD_PATH = "D:\\Big\\Spring\\Ateamproject04\\img_upload\\";
	
	//테스트용 Mapper
	@RequestMapping(value = "/test.do")
	@ResponseBody
	public String test()
	{
//		if(guestDTO.delete(1, 25)) return "success";
		return "";
	}
	
	
	//메인화면(글 목록)
	@RequestMapping(value = "/list.do")//, method = RequestMethod.GET
	public String List(@RequestParam(defaultValue = "1")int pageno,
			@RequestParam(defaultValue = "1")int listFilter,
			@RequestParam(defaultValue = "0")int searchFilter,
			@RequestParam(defaultValue = "")String keyword,
			Model model)
	{
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(pageno);
		searchVO.setListFilter(listFilter);
		searchVO.setSearchFilter(searchFilter);
		searchVO.setKeyword(keyword);
		
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
		int startbk = (pageno - 1)  - (( pageno - 1) % 10) + 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
				
//		//테스트 구문
//		System.out.println("전체 갯수: " + total);
//		System.out.println("최대 페이징: " + maxpage);
//		for(BoardVO boardVO : boardList)
//		{
//			System.out.println(boardVO.toString());
//		}
		
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("boardList", boardList);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		
		return "list";
	}
	//팔로우 유저 글 목록
	@RequestMapping(value = "/flist.do")
	public String Flist(@RequestParam(defaultValue = "1")int pageno,
			@RequestParam(defaultValue = "1")int listFilter,
			@RequestParam(defaultValue = "0")int searchFilter,
			@RequestParam(defaultValue = "")String keyword,
			HttpServletRequest request, 
			Model model)
	{
		HttpSession session = request.getSession();
		UserVO userVO = (UserVO)session.getAttribute("login");
		
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(pageno);
		searchVO.setListFilter(listFilter);
		searchVO.setSearchFilter(searchFilter);
		searchVO.setKeyword(keyword);
		
		searchVO.setUsernum(userVO.getUsernum());
		
		//전체 갯수
		int total = boardDTO.getFollowTotal(searchVO);
		
		//최대 페이징
		int maxpage = total / 10;
		if( total % 10 != 0) maxpage++;
		
		//목록 조회
		List<BoardVO> boardList = boardDTO.followList(searchVO);
		
		for(BoardVO boardVO : boardList)
		{
			boardVO.setTitle(WebUtil.Text2HTML(boardVO.getTitle()));
		}
		
		//시작 블럭
		int startbk = (pageno - 1)  - (( pageno - 1) % 10) + 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
		
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("boardList", boardList);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		
		return "flist";
	}
	//글쓰기
	@RequestMapping(value = "/write.do", method = RequestMethod.GET)
	public String write(HttpServletRequest request)
	{
		if( request.getSession().getAttribute("login") == null)
		{
			//로그인하지 않음.
			return "list";
		}
		return "write";
	}
	//글쓰기 처리
	@RequestMapping(value = "/write.do", method = RequestMethod.POST)
	public void writeOk(BoardVO boardVO,
			@RequestParam("attach") MultipartFile file,
			HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		UserVO login = (UserVO)request.getSession().getAttribute("login");
		if( login == null)
		{
			//로그인하지 않음.
			response.sendRedirect("list.do");
			return;
		}	
		
		// 원본 파일 이름 알아오기		
		if(file != null && !file.isEmpty()) // file이 null이 아니고 비어있지 않은 경우
		{			
			String originalFileName = file.getOriginalFilename();
			System.out.println("originalFileName:" + originalFileName);
			
			//파일 이름이 중복되지 않도록 파일 이름 변경 : 서버에 저장할 이름
			// UUID 클래스 사용
			UUID uuid = UUID.randomUUID();
			String savedFileName = uuid.toString();
			
			//파일 생성
			File newFile = new File(uploadPath + savedFileName);
			
			//서버로 전송
			file.transferTo(newFile);
			
			boardVO.setFname(originalFileName);
			boardVO.setPname(savedFileName);
		}else
		{
			System.out.println("File not upload..");
		}
		boardVO.setUserid(login.getUserid());
		
		boardDTO.insert(boardVO);
		response.sendRedirect("view.do?no=" + boardVO.getNo());	
	}
	//글보기
	@RequestMapping(value = "/view.do")
	public String view(@RequestParam(required = true) int no, 
			HttpServletResponse response,
			Model model) throws IOException
	{
		//System.out.println("글보기 controller 실행됨");
		BoardVO boardVO = boardDTO.view(no, true);
		
		if( boardVO == null )
		{
			response.sendRedirect("list.do");	
		}
		
		boardVO.setNote(WebUtil.Text2HTML(boardVO.getNote()));
		boardVO.setTitle(WebUtil.Text2HTML(boardVO.getTitle()));
		boardVO.setIntro(WebUtil.Text2HTML(boardVO.getIntro()));
		
		System.out.println(boardVO.toString());
		
		//전체 갯수
		int total = replyDTO.getTotal(no);
		
		//최대 페이징
		int maxpage = total / 10;
		if( total % 10 != 0) maxpage++;
		
		//검색정보(pageno, offset, no)
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(1);
		searchVO.setNo(no);
		List<ReplyVO> replyList = replyDTO.list(searchVO);
		
		for(ReplyVO replyVO : replyList)
		{
			replyVO.setRnote(WebUtil.Text2HTML(replyVO.getRnote()));
		}
		
		//시작 블럭
		int startbk = 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
		
		model.addAttribute("replyList", replyList );
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		model.addAttribute("boardVO", boardVO );	
		
		return "view";
	}
	
	//댓글 받아오기
	@RequestMapping(value = "/reply.do")
	@ResponseBody
	public List<ReplyVO> getReplyList(@RequestParam(required = true) int no,
			@RequestParam(defaultValue = "1")int pageno)
	{
		System.out.println("reply.do 실행됨");
		
		//검색정보(pageno, offset, no)
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(pageno);
		searchVO.setNo(no);
		List<ReplyVO> replyList = replyDTO.list(searchVO);
		
		for(ReplyVO replyVO : replyList)
		{
			replyVO.setRnote(WebUtil.Text2HTML(replyVO.getRnote()));
		}
		
//		for(int i = 0; i < replyList.size(); i++)
//		{
//			ReplyVO replyVO = replyList.get(i);
//			replyVO.setRnote(WebUtil.Text2HTML(replyVO.getRnote()));
//			System.out.println(replyVO.getRnote());
//			replyList.add(i, replyVO);
//		}
		
		System.out.println(replyList);
		return replyList;
	}
	
	//댓글 쓰기
	@RequestMapping(value = "/replyOk.do")
	@ResponseBody
	public String replyOk(ReplyVO replyVO)
	{
		if(replyDTO.insert(replyVO)) return "Y";
		return "N";
	}
	
	//댓글 삭제
	@RequestMapping(value = "delReply.do")
	@ResponseBody
	public String delReply(ReplyVO replyVO)
	{
		if(replyDTO.delete(replyVO)) return "Y";
		return "N";
	}
	
	//프로필 이미지 업로드
	@RequestMapping(value = "/profileImgWrite.do")
	@ResponseBody
	public String profileImgWrite(HttpServletRequest request,
			@RequestParam("attach") MultipartFile file) throws IllegalStateException, IOException
	{
		String originalFileName = file.getOriginalFilename();
		System.out.println("originalFileName:" + originalFileName);
		
		//파일 이름이 중복되지 않도록 파일 이름 변경 : 서버에 저장할 이름
		// UUID 클래스 사용
		UUID uuid = UUID.randomUUID();
		String savedFileName = uuid.toString();
		
		//파일 생성
		File newFile = new File(IMG_UPLOAD_PATH + savedFileName);
		
		//서버로 전송
		file.transferTo(newFile);
			
		//업로드 된 프로필 이미지를 DB에 업데이트한다.
		UserVO userVO = (UserVO)request.getSession().getAttribute("login");
		userVO.setProfileimgf(originalFileName);
		userVO.setProfileimgp(savedFileName);
		
		userDTO.updateProfileImg(userVO);
		
		return savedFileName;
	}
	
	//프로필 이미지 업로드
	@RequestMapping(value = "/profileImgWriteUser.do")
	@ResponseBody
	public String profileImgWriteUser(HttpServletRequest request,
			@RequestParam("attach") MultipartFile file,
			@RequestParam("usernum") int usernum) throws IllegalStateException, IOException
	{
		String originalFileName = file.getOriginalFilename();
		System.out.println("originalFileName:" + originalFileName);
		
		//파일 이름이 중복되지 않도록 파일 이름 변경 : 서버에 저장할 이름
		// UUID 클래스 사용
		UUID uuid = UUID.randomUUID();
		String savedFileName = uuid.toString();
		
		//파일 생성
		File newFile = new File(IMG_UPLOAD_PATH + savedFileName);
		
		//서버로 전송
		file.transferTo(newFile);
			
		//업로드 된 프로필 이미지를 DB에 업데이트한다.
		UserVO userVO = new UserVO();
		userVO.setProfileimgf(originalFileName);
		userVO.setProfileimgp(savedFileName);
		userVO.setUsernum(usernum);
		
		userDTO.updateProfileImg(userVO);
		
		return savedFileName;
	}

	//프로필 이미지 출력하기 (게시물 번호로 받기)
	@RequestMapping(value = "/profileImg.do")
	@ResponseBody
	public void getProfileImg(@RequestParam("no") int no,
			HttpServletResponse response) throws IOException
	{
		BoardVO boardVO = boardDTO.view(no, false);
		/*
		File file = new File(IMG_UPLOAD_PATH, boardVO.getProfileimgp());
	    
		String contentType = "";
        String fileName = boardVO.getProfileimgf();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".gif")) {
            contentType = "image/gif";
        }
		
		if (!contentType.isEmpty()) {
            response.setContentType(contentType);
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            fis.close();
            os.close();
        } else {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }*/
		
		File file = new File(IMG_UPLOAD_PATH, boardVO.getProfileimgp());
		// 파일명 인코딩
		String encodedFileName = new String (boardVO.getProfileimgf().getBytes("UTF-8"), "ISO-8859-1");

		// file 다운로드 설정
		response.setContentType("application/download");
		response.setContentLength((int)file.length());
		response.setHeader("Content-Disposition", "attatchment;filename=\"" + encodedFileName + "\"");
		
		// 다운로드 시 저장되는 이름은 Response Header의 "Content-Disposition"에 명시
		OutputStream os = response.getOutputStream();
		
		FileInputStream fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, os);
	}
	
	//프로필 이미지 출력하기 (파일명으로 받기)
	@RequestMapping(value = "/profileImgByName.do")
	public void getProfileImgByName(@RequestParam("profileimgp") String profileimgp, 
			@RequestParam("profileimgf") String profileimgf,
			HttpServletResponse response) throws IOException
	{
		/*
		File file = new File(IMG_UPLOAD_PATH, profileimgp);
	    
		String contentType = "";
        String fileName = profileimgf;
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".gif")) {
            contentType = "image/gif";
        }
		
		if (!contentType.isEmpty()) {
			
			//response.setContentType("application/download");
			response.setContentType(contentType);
			response.setContentLength((int)file.length());
			response.setHeader("Content-Disposition", "attatchment;filename=\"" + profileimgp + "\"");
			
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            fis.close();
            os.close();
        } else {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        }
        */
		File file = new File(IMG_UPLOAD_PATH, profileimgp);
		// 파일명 인코딩
		String encodedFileName = new String (profileimgf.getBytes("UTF-8"), "ISO-8859-1");

		// file 다운로드 설정
		response.setContentType("application/download");
		response.setContentLength((int)file.length());
		response.setHeader("Content-Disposition", "attatchment;filename=\"" + encodedFileName + "\"");
		
		// 다운로드 시 저장되는 이름은 Response Header의 "Content-Disposition"에 명시
		OutputStream os = response.getOutputStream();
		
		FileInputStream fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, os);
		
		// fis.close();
		// os.close();		
	}
	
	//추천 처리하기
	@RequestMapping(value = "/recommend.do")
	@ResponseBody
	public String recommend(@RequestParam("no") int no,
			@RequestParam("usernum")int usernum)
	{
		boolean flag = recommendDTO.recommend(usernum, no);
		int recCount = recommendDTO.recCount(no);
		
		if(flag) return "O:" + recCount;
		return "X:" + recCount;
	}
	
	//구독 처리하기
	@RequestMapping(value = "/subscribe.do")
	@ResponseBody
	public String subscribe(@RequestParam("fromnum") int fromnum,
			@RequestParam("tonum") int tonum, HttpServletRequest request)
	{
		boolean flag = followDTO.follow(fromnum, tonum);
		int followerCount = followDTO.getTotalFollower(tonum);
		
		//로그인 정보 갱신
		UserVO userVO = (UserVO)request.getSession().getAttribute("login");
		userVO.setFollowCnt(followerCount);
		request.getSession().setAttribute("login",userVO);
		
		if(flag) return "O:" + followerCount;
		return "X:" + followerCount;
	}
	
	//글 수정
	@RequestMapping(value = "/modify.do")
	public String Modify(@RequestParam(required = true) int no,
			HttpServletRequest request,
			Model model)
	{	
		UserVO userVO = (UserVO)request.getSession().getAttribute("login");
		
		if( userVO == null )
			return "redirect:list.do";
		
		BoardVO boardVO = boardDTO.view(no, false);
		
		model.addAttribute("boardVO", boardVO);
		
		return "modify";
	}
	
	//글 수정완료
	@RequestMapping(value = "/modifyOk.do")
	public void modifyOk(BoardVO boardVO,
			@RequestParam("attach") MultipartFile file,
			HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		UserVO login = (UserVO)request.getSession().getAttribute("login");
		if( login == null)
		{
			//로그인하지 않음.
			response.sendRedirect("list.do");
			return;
		}	
		
		// 원본 파일 이름 알아오기		
		if(file != null && !file.isEmpty()) // file이 null이 아니고 비어있지 않은 경우
		{			
			String originalFileName = file.getOriginalFilename();
			System.out.println("originalFileName:" + originalFileName);
			
			//파일 이름이 중복되지 않도록 파일 이름 변경 : 서버에 저장할 이름
			// UUID 클래스 사용
			UUID uuid = UUID.randomUUID();
			String savedFileName = uuid.toString();
			
			//파일 생성
			File newFile = new File(uploadPath + savedFileName);
			
			//서버로 전송
			file.transferTo(newFile);
			
			boardVO.setFname(originalFileName);
			boardVO.setPname(savedFileName);
		}else
		{
			System.out.println("File not upload..");
		}
		boardVO.setUserid(login.getUserid());
		
		if(boardDTO.update(boardVO)) System.out.println("update success");
		else System.out.println("update fail");
		
		response.sendRedirect("view.do?no=" + boardVO.getNo());	
	}
	
	//글 삭제
	@RequestMapping(value = "delete.do")
	public void delBoard(@RequestParam("no")int no,
			HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		UserVO userVO = (UserVO)request.getSession().getAttribute("login");
		BoardVO boardVO = boardDTO.view(no, false);
		if( userVO == null || userVO.getUsernum() != boardVO.getUsernum() )
		{
			System.out.println("삭제 실패");
			response.sendRedirect("list.do");
			return;
		}
		if(boardDTO.delete(no)) System.out.println("삭제 성공");
		response.sendRedirect("list.do");
	}
	
	//개인 페이지
	@RequestMapping(value = "personalpage.do")
	public String personalPage(@RequestParam("usernum")int usernum,
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
		
		
		return "PersonalPage/personalpage";
	}
	//개인정보 설정
	@RequestMapping(value = "/config.do")
	public String Config(HttpServletRequest request)
	{	
		UserVO login = (UserVO)request.getSession().getAttribute("login");
		if( login == null ) return "redirect:list.do";
		return "config";
	}
	//비밀번호 검사
	@RequestMapping(value = "/pwCheck.do")
	@ResponseBody
	public String pwCheck(@RequestParam("usernum")int usernum, @RequestParam("userpw")String userpw)
	{
		if(userDTO.isPwCollect(usernum, userpw)) return "Y";
		return "N";
	}
	//개인정보 변경
	@RequestMapping(value = "/userConfig.do")
	public String userConfig(HttpServletRequest request,
			@RequestParam("usernum")int usernum,
			@RequestParam("email")String email, 
			@RequestParam("intro")String intro, 
			@RequestParam("userpw")String userpw,
			@RequestParam("userpwN")String userpwN)
	{
		UserVO userVO = userDTO.userInfo(usernum);
		userDTO.intro(usernum, intro);
		if(email!=null && !email.equals(""))	userDTO.emailUpdate(usernum, email);
		if(userpwN!=null && !userpwN.equals(""))
		{
			userDTO.userPwUpdate(usernum, userpwN);
			userVO = userDTO.login(userVO.getUserid(), userpwN);
			request.getSession().setAttribute("login",userVO);
		}else{
			userVO = userDTO.login(userVO.getUserid(), userpw);
			request.getSession().setAttribute("login",userVO);
		}
		
		request.getSession().setAttribute("code", null);
		
		return "redirect:config.do";
	}
	
	//사용자 관리(리스트)
	@RequestMapping(value = "/userlist.do")
	public String UserList(HttpServletRequest request,
			@RequestParam(defaultValue = "1")int pageno,
			@RequestParam(defaultValue = "0")int userStatusFilter,
			@RequestParam(defaultValue = "1")int userSearchFilter,
			@RequestParam(defaultValue = "")String keyword,
			Model model)
	{
		UserVO login = (UserVO)request.getSession().getAttribute("login");
		if(login == null || !login.getIsadmin().equals("Y"))	return "redirect:list.do";
		
		SearchVO searchVO = new SearchVO();
		searchVO.setPageno(pageno);
		searchVO.setUserStatusFilter(userStatusFilter);
		searchVO.setUserSearchFilter(userSearchFilter);
		searchVO.setKeyword(keyword);
		searchVO.setUsernum(login.getUsernum());
		
		//전체 갯수
		int total = userDTO.getTotal(searchVO);
		
		//최대 페이징
		int maxpage = total / 10;
		if( total % 10 != 0) maxpage++;
		
		//목록 조회
		List<UserVO> userList = userDTO.userList(searchVO);
		System.out.println(userList);
		System.out.println(searchVO.getOffset());

		//시작 블럭
		int startbk = (pageno - 1)  - (( pageno - 1) % 10) + 1; 
		int endbk   = startbk + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbk > maxpage ) endbk = maxpage;
		
		model.addAttribute("userList", userList);
		model.addAttribute("total", total);
		model.addAttribute("maxpage", maxpage);
		model.addAttribute("startbk", startbk);
		model.addAttribute("endbk", endbk);
		model.addAttribute("searchVO", searchVO);
		
		return "userlist";
	}
	//사용자 상태 변경
	@RequestMapping(value = "userStatus.do")
	public String userStatus(String status, int[] usernum)
	{
		for(int unum : usernum)
		{
			userDTO.userStatus(status, unum);
		}
		return "redirect:userlist.do";
	}
	//사용자 관리(데이터)
	@RequestMapping(value = "/userdata.do")
	public String UserData(@RequestParam("usernum")int usernum, 
			HttpServletRequest request,
			Model model)
	{
		UserVO login = (UserVO)request.getSession().getAttribute("login");
		if(login == null || !login.getIsadmin().equals("Y"))	return "redirect:list.do";
		
		UserVO userVO = userDTO.adminUserInfo(usernum);
		
		userVO.setIntro(WebUtil.Text2HTML(userVO.getIntro()));
		
		//유저가 작성한 전체 글 갯수
		int totalB = guestDTO.getTotalBoard(usernum);
		
		//최대 페이징
		int maxpageB = totalB / 10;
		if( totalB % 10 != 0) maxpageB++;

		//시작 블럭
		int startbkB = 1; 
		int endbkB   = startbkB + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbkB > maxpageB ) endbkB = maxpageB;
		//유저가 작성한 글 리스트
		List<BoardVO> boardList = guestDTO.boardList(usernum, 1);
		
		for(BoardVO boardVO : boardList)
		{
			boardVO.setTitle(WebUtil.Text2HTML(boardVO.getTitle()));
		}
		
		//유저가 작성한 댓글 갯수
		int totalR = guestDTO.getTotalReply(usernum);
		
		//최대 페이징
		int maxpageR = totalR / 10;
		if( totalR % 10 != 0 ) maxpageR++;
		
		//시작블럭
		int startbkR = 1; 
		int endbkR   = startbkR + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbkR > maxpageR ) endbkR = maxpageR;
		//유저가 작성한 댓글 리스트
		List<ReplyVO> replyList = guestDTO.replyList(usernum, 1);
		
		for(ReplyVO replyVO : replyList)
		{
			replyVO.setRnote(WebUtil.Text2HTML(replyVO.getRnote()));
		}
		
		model.addAttribute("userVO", userVO);
		
		model.addAttribute("totalB", totalB);
		model.addAttribute("maxpageB", maxpageB);
		model.addAttribute("startbkB", startbkB);
		model.addAttribute("endbkB", endbkB);
		model.addAttribute("boardList", boardList);

		model.addAttribute("totalR", totalR);
		model.addAttribute("maxpageR", maxpageR);
		model.addAttribute("startbkR", startbkR);
		model.addAttribute("endbkR", endbkR);
		model.addAttribute("replyList", replyList);
		
		return "userdata";
	}
	//사용자 작성 글 목록 
	@RequestMapping(value = "/userBoard.do")
	@ResponseBody
	public ListVO getBoardList(@RequestParam(required = true) int usernum,
	                           @RequestParam(defaultValue = "1")int pageno)
	{
	    System.out.println("userBoard.do 실행됨");
	    
  		//유저가 작성한 전체 글 갯수
		int totalB = guestDTO.getTotalBoard(usernum);
		
		//최대 페이징
		int maxpageB = totalB / 10;
		if( totalB % 10 != 0) maxpageB++;

		//시작 블럭
		int startbkB = (pageno - 1)  - (( pageno - 1) % 10) + 1; 
		int endbkB   = startbkB + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbkB > maxpageB ) endbkB = maxpageB;
		//유저가 작성한 글 리스트
		List<BoardVO> boardList = guestDTO.boardList(usernum, pageno);

	    ListVO<BoardVO> listVO = new ListVO<BoardVO>(); // BoardVO 타입으로 생성
	    listVO.setTotal(totalB);
	    listVO.setMaxpage(maxpageB);
	    listVO.setStartbk(startbkB);
	    listVO.setEndbk(endbkB);
	    listVO.setList(boardList);

	    return listVO;
	}
	//사용자 작성 댓글 목록
	@RequestMapping(value = "/userReply.do")
	@ResponseBody
	public ListVO Reply(@RequestParam(required = true) int usernum,
            @RequestParam(defaultValue = "1")int pageno)
	{
		System.out.println("reply.do 실행됨");
		
		//유저가 작성한 댓글 갯수
		int totalR = guestDTO.getTotalReply(usernum);
		
		//최대 페이징
		int maxpageR = totalR / 10;
		if( totalR % 10 != 0 ) maxpageR++;
		
		//시작블럭
		int startbkR = (pageno - 1)  - (( pageno - 1) % 10) + 1; 
		int endbkR   = startbkR + 10 - 1; //jstl로 for문 돌리기 위해 -1
		if( endbkR > maxpageR ) endbkR = maxpageR;
		//유저가 작성한 댓글 리스트
		List<ReplyVO> replyList = guestDTO.replyList(usernum, pageno);

	    ListVO<ReplyVO> listVO = new ListVO<ReplyVO>(); // ReplyVO 타입으로 생성
	    listVO.setTotal(totalR);
	    listVO.setMaxpage(maxpageR);
	    listVO.setStartbk(startbkR);
	    listVO.setEndbk(endbkR);
	    listVO.setList(replyList);
		 
		return listVO;
	}
	//사용자 작성 글 삭제
	@RequestMapping(value = "/userBoardDelete.do")
	public String userBoardDelete(int[] no, int usernum)
	{
		for(int n : no)
		{
			boardDTO.delete(n);
		}
		return "redirect:userdata.do?usernum="+usernum;
	}
	
	//사용자 작성 댓글 삭제
	@RequestMapping(value = "/userReplyDelete.do")
	public String userReplyDelete(int[] rno,int usernum,
			HttpServletRequest request)
	{
		UserVO userVO = (UserVO)request.getSession().getAttribute("login");
		ReplyVO replyVO = new ReplyVO();
		replyVO.setUsernum(userVO.getUsernum());
		for(int n : rno)
		{
			replyVO.setRno(n);
			replyDTO.delete(replyVO);
		}
		return "redirect:userdata.do?usernum="+usernum;
	}

	//사용자 정보 수정
	@RequestMapping(value = "/userModify.do")
	public String userModify(UserVO userVO)
	{
		userDTO.adminUserModify(userVO);
		return "redirect:userdata.do?usernum="+userVO.getUsernum();
	}
	
	//로그인
	@RequestMapping(value = "/login.do")
	public String Login()
	{
		return "login";
	}
	//로그인 처리
	@RequestMapping(value = "/loginOk.do", method = RequestMethod.POST, produces = "application/text; charset=utf8")
	@ResponseBody
	public String LoginOk(HttpServletRequest request, 
			@RequestParam("userid")String userid, @RequestParam("userpw")String userpw)
	{
		HttpSession session = request.getSession();
		
		String status = userDTO.Status(userid);
		if(status.equals("N"))
		{
			request.getSession().setAttribute("login",null);
			return "N";
		}
		if(status.equals("B")) 
		{
			request.getSession().setAttribute("login",null);
			return "B";
		}
		
		UserVO userVO = userDTO.login(userid, userpw);
		if(userVO == null) 
		{	
			request.getSession().setAttribute("login",null);
			return "X";
		}else
		{
			// 세션에 로그인정보를 저장한다.
			request.getSession().setAttribute("login",userVO);				
			return "O";
		}
	}
	//로그아웃
	@RequestMapping(value = "/logout.do")
	public String Logout(HttpServletRequest req)
	{
		HttpSession session = req.getSession();
		session.invalidate();
		return "redirect:login.do";
	}
	
	//회원가입
	@RequestMapping(value = "/join.do")
	public String Join()
	{
		return "join";
	}
	//아이디 중복체크
	@RequestMapping(value = "/idcheck.do",produces = "application/text; charset=utf8")
	@ResponseBody
	public String idCheck(@RequestParam("userid")String userid)
	{
		if(userid == null || userid.equals("")) return "N:아이디를 입력하세요.";
		if(userDTO.isDupId(userid)) 			return "Y:사용가능한 아이디입니다.";
		return "X:중복된 아이디입니다.";
	}
	//이메일 중복체크
	@RequestMapping(value = "/emailcheck.do",produces = "application/text; charset=utf8")
	@ResponseBody
	public String emailCheck(@RequestParam("email")String email)
	{
		String emailRegex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
		
		if(email == null || email.equals(""))   return "N:이메일을 입력하세요.";
		if(!Pattern.matches(emailRegex, email)) return "N:유효한 이메일 형식이 아닙니다.";
		if(userDTO.isDupEmail(email))           return "Y:사용가능한 이메일입니다.";
		return "X:중복된 이메일입니다.";
	}
	//아이디,이메일과 일치하는 계정 유무
	@RequestMapping(value = "/searchUser.do")
	@ResponseBody
	public String searchUser(@RequestParam("userid")String userid, @RequestParam("email")String email)
	{
		//일치하는 계정이 있다면 false 반환됨
		if(userDTO.isDupUser(userid, email)) return "N";
		return "Y";
	}
	//회원가입 완료
	@RequestMapping(value = "/joinok.do", method = RequestMethod.POST)
	public String joinOK(UserVO userVO) 
	{
		System.out.println("회원가입 완료 메소드 실행됨");
		userDTO.join(userVO);
		return "redirect:list.do";
	}
	//회원탈퇴
	@RequestMapping(value = "/userDelete.do")
	public String userDelete(@RequestParam("usernum")int usernum,
			HttpServletRequest request)
	{
		userDTO.userStatus("N", usernum);
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		return "redirect:login.do";
	}
	//아이디 찾기
	@RequestMapping(value = "/idsearch.do")
	public String IdSearch()
	{
		return "idsearch";
	}
	//비밀번호 찾기
	@RequestMapping(value = "/pwsearch.do")
	public String PwSearch()
	{
		return "pwsearch";
	}
	

	
	
	//캡챠 이미지 출력
	@RequestMapping(value = "/sign.do")
	public void Sign(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		// 자동 등록 방지 코드를 생성할 객체들을 선언합니다
		Captcha mCaptcha;
		Builder mBuilder;
		String  mAnswer;

		// 자동 등록방지코드를 생성하고, 코드를 이미지로 변환, 이미지를 출력(반환)
		mBuilder = new Captcha.Builder(160,50);
		// 빌더에 문자열을 추가
		mBuilder.addText();
		// 빌더에 배경색을 추가
		mBuilder.addBackground(new FlatColorBackgroundProducer(Color.WHITE));
		// 선 추가
		mBuilder.addBorder();
		// 노이즈 추가
		mBuilder.addNoise();
		// 셋팅이된 내용으로 빌더에게 captcha를 생성 요청함
		mCaptcha = mBuilder.build();
		// captcha가 갖고있는 정답을 문자열로 저장해둡니다
		mAnswer  = mCaptcha.getAnswer();

		// captcha를 이미지로 반환합니다
		OutputStream mOut = response.getOutputStream();
		ImageIO.write(mCaptcha.getImage(), "jpg", mOut);
		mOut.close();

		// 세션에, 정답을 저장합니다
		request.getSession().setAttribute("sign",mAnswer);			
	}
	
	//캡챠 정답 조회
	@RequestMapping(value = "/getsign.do")
	@ResponseBody
	public String GetSign(HttpServletRequest request)
	{
		return (String)request.getSession().getAttribute("sign");
	}	
	
	//첨부파일 다운로드
	@RequestMapping(value = "/down.do", method = RequestMethod.GET)
	public void DownLoad(@RequestParam(required = true) int no, HttpServletResponse response) throws Exception
	{
		BoardVO vo = boardDTO.view(no, false);
		
		File file = new File(uploadPath, vo.getPname());
		// 파일명 인코딩
		String encodedFileName = new String (vo.getFname().getBytes("UTF-8"), "ISO-8859-1");

		// file 다운로드 설정
		response.setContentType("application/download");
		response.setContentLength((int)file.length());
		response.setHeader("Content-Disposition", "attatchment;filename=\"" + encodedFileName + "\"");
		
		// 다운로드 시 저장되는 이름은 Response Header의 "Content-Disposition"에 명시
		OutputStream os = response.getOutputStream();
		
		FileInputStream fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, os);
		
		// fis.close();
		// os.close();
	}	
}
