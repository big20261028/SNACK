package com.ezen.control;

import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.dto.*;
import com.ezen.util.WebUtil;
import com.mysql.cj.xdevapi.Type;


@Controller
public class MailController 
{
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserDTO userDTO;
	
	@RequestMapping(value = "/mailsend.do")
	@ResponseBody
	public String MailSend()
	{
		String code    = WebUtil.Random(5);
		String msg     = "mail send ok...";
		String from    = "dlsrl2025@naver.com";
		String to      = "dlsrl2025@naver.com";
		String title   = "인증 정보입니다.";
		String content = "인증번호 : " + code;
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(from);     // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(to);         // 받는사람 이메일
			messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
			messageHelper.setText(content);  // 메일 내용

			mailSender.send(message);
		} catch (Exception e) 
		{
			//아이디 or 비밀번호가 틀리면
			e.printStackTrace();
			msg = e.toString();
		}
		return msg;
	}
	
	@RequestMapping(value = "/mailSendUserid.do")
	@ResponseBody
	public String mailSendUserid(@RequestParam("email")String email)
	{
		String userid = userDTO.searchUserid(email);
		
		//String code    = WebUtil.Random(5);
		String msg     = "Y";
		String from    = "dlsrl2025@naver.com";
		String to      = email;
		String title   = "아이디 정보입니다.";
		String content = "아이디 : " + userid;
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(from);     // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(to);         // 받는사람 이메일
			messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
			messageHelper.setText(content);  // 메일 내용

			mailSender.send(message);
		} catch (Exception e) 
		{
			//아이디 or 비밀번호가 틀리면
			e.printStackTrace();
			msg = "N";
		}
		return msg;
	}
	
	@RequestMapping(value = "/mailSendUserpw.do")
	@ResponseBody
	public String mailSendUserpw(@RequestParam("email")String email,@RequestParam("userid")String userid)
	{
		String code    = WebUtil.Random(5);
		
		userDTO.userPwUpdate(userid, email, code);
		
		String msg     = "Y";
		String from    = "dlsrl2025@naver.com";
		String to      = email;
		String title   = "임시 비밀번호입니다.";
		String content = "비밀번호 : " + code;
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(from);     // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(to);         // 받는사람 이메일
			messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
			messageHelper.setText(content);  // 메일 내용

			mailSender.send(message);
		} catch (Exception e) 
		{
			//아이디 or 비밀번호가 틀리면
			e.printStackTrace();
			msg = "N";
		}
		return msg;
	}
	
	@RequestMapping(value = "/mailSendCode.do")
	@ResponseBody
	public String mailSendCode(@RequestParam("email")String email, HttpServletRequest request)
	{
		String code    = WebUtil.Random(5);
		String msg     = code;
		String from    = "dlsrl2025@naver.com";
		String to      = email;
		String title   = "이메일 인증번호입니다.";
		String content = "인증번호 : " + code;
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(from);     // 보내는사람 생략하거나 하면 정상작동을 안함
			messageHelper.setTo(to);         // 받는사람 이메일
			messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
			messageHelper.setText(content);  // 메일 내용

			mailSender.send(message);
			
			request.getSession().setAttribute("code", code);	//인증번호 세션에 저장
		} catch (Exception e) 
		{
			//아이디 or 비밀번호가 틀리면
			e.printStackTrace();
			msg = null;
		}
		return msg;
	}
}
