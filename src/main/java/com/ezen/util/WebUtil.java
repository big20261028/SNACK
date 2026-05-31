/*
 * 모듈명 : WebUitl은 각종 유틸리티 기능을 제공한다.
 * 작성일 : 2025.04.17.
 * 작성자 : 백인기
 */
package com.ezen.util;

public class WebUtil 
{
	//일반 텍스트 내용를 HTML 내용으로 변환
	//매개변수 : text - 일반 텍스트 내용
	//리턴값 : 변환된 HTML 내용	
	public static String Text2HTML(String text)
	{
		String note = text;
		//< 와 >를 변경한다.
		note = note.replace("<","&lt;");
		note = note.replace(">","&gt;");

		//엔터문자를 변경한다.
		note = note.replace("\n","<br>\n");
		return note;
	}
	//인증용 번호를 생성한다.
	public static String Random(int max)
	{
		String code = "abcdefghijklmnopqrstuvwxyz1234567890";
		int    rand;
		String msg = "";
		
		for(int i = 0 ; i < max; i++)
		{
			rand  = (int)(Math.random() * 1000) % code.length();
			msg  += code.charAt(rand);	//인덱스 rand번째 글자 추출
		}
		return msg;
	}
}
