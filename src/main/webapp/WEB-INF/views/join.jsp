<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	var idCheck    = "X"; //N - 아이디 유효하지 않음, X - 중복된 아이디, Y - 사용가능한 아이디
	var emailCheck = "X"; //N - 이메일 유효하지 않음, X - 중복된 이메일, Y - 사용가능한 이메일
	
	let codeCheck = "N"; //N - 인증번호 미확인, Y - 확인 완료
	let code = "";

	$(function(){
		$("#join").addClass("choosenBtn");
		
		$("#userid").focus();
		
		$("#userid").on("keyup",function(){
			DupCheckID();
		});
		$("#email").on("keyup",function(){
			DupCheckEmail();
		});
		$("#email").change(function(){
			codeCheck = "N";
		});
		
		$("#sendMail").click(function(){
			if($("#email").val() == "")
			{
				alert("이메일을 입력해주세요.");
				$("#email").focus();
				return;
			}
			sendEmail();
		});
		
		$("#confirmEmail").click(function(){
			confirmEmail();
		})
		
		$("#joinOkBtn").click(function(){
			doSubmit();
		})
		$("#cancelBtn").click(function(){
			if(confirm("회원가입을 취소하시겠습니까?"))
			{
				document.location = "/control/list.do";
			}
		})
		
		function DupCheckID()
		{
			userid = $("#userid").val();
			/*
			if(userid=="")
			{
				$("#idCheckMsg").html("아이디를 입력하세요.");
				return;
			}*/
			$.ajax({
				type: "get",
				url : "idcheck.do?userid="+userid,
				dataType: "html",
				success : function(data){
					// 통신이 성공적으로 이루어졌을때 이 함수를 타게된다.
					data = data.trim();
					ary = data.split(":");
					idCheck = ary[0];
					if(idCheck == "N") $("#idCheckMsg").css("color","black");
					if(idCheck == "X") $("#idCheckMsg").css("color","red");
					if(idCheck == "Y") $("#idCheckMsg").css("color","blue");
					$("#idCheckMsg").html(ary[1]);
				},
				error: function(xhr, status, error){
					// 통신 오류 발생시	
				},
				complete : function(){
					// 통신이 성공하거나 실패했어도 마지막으로 이 함수를 타게된다.
				}			
			});	
		}
		function DupCheckEmail()
		{
			email = $("#email").val();
			/*
			if(email=="")
			{
				$("#emailCheckMsg").html("이메일을 입력하세요.");
				return;
			}*/
			$.ajax({
				type: "get",
				url : "emailcheck.do?email="+email,
				dataType: "html",
				success : function(data){
					// 통신이 성공적으로 이루어졌을때 이 함수를 타게된다.
					data = data.trim();
					ary = data.split(":");
					emailCheck = ary[0];
					if(emailCheck == "N") $("#emailCheckMsg").css("color","black");
					if(emailCheck == "X") $("#emailCheckMsg").css("color","red");
					if(emailCheck == "Y") $("#emailCheckMsg").css("color","blue");
					$("#emailCheckMsg").html(ary[1]);
				},
				error: function(xhr, status, error){
					// 통신 오류 발생시	
				},
				complete : function(){
					// 통신이 성공하거나 실패했어도 마지막으로 이 함수를 타게된다.
				}			
			});	
		}
		
		function doSubmit()
		{
			if( $("#userid").val() =="" )
			{
				alert("아이디를 입력해주세요.");
				$("#userid").focus();
				return;
			}
			if( $("#userpw").val() =="")
			{
				alert("비밀번호를 입력해주세요.");
				$("#userpw").focus();
				return;
			}
			if( $("#userpw").val() != $("#pwcheck").val())
			{
				alert("비밀번호가 일치하지 않습니다.");
				$("#userpw").focus();
				return;
			}
			if( $("#name").val() =="")
			{
				alert("이름을 입력해주세요.");
				$("#name").focus();
				return;
			}
			if( $("#email").val() =="")
			{
				alert("이메일을 입력해주세요.");
				$("#email").focus();
				return;
			}
			if(idCheck=="X")
			{
				alert("중복된 아이디는 사용할 수 없습니다.");
				$("#userid").focus();
				return;
			}
			if(emailCheck=="N")
			{
				alert("이메일이 유효하지 않습니다.");
				$("#email").focus();
				return;
			}
			if(emailCheck=="X")
			{
				alert("중복된 이메일은 사용할 수 없습니다.");
				$("#email").focus();
				return;
			}
			if(codeCheck != "Y")
			{
				alert("이메일 인증을 완료해주세요.");
				return;
			}
			if( $("#sign").val() =="")
			{
				alert("자동가입 방지코드를 입력하세요.");
				$("#sign").focus();
				return;
			}
			
			$.ajax({
				type: "get",
				url : "getsign.do",
				dataType: "html",
				success : function(data){
					// 통신이 성공적으로 이루어졌을때 이 함수를 타게된다.
					data = data.trim();
					if( data != $("#sign").val() )
					{
						alert("자동가입 방지 코드가 일치하지 않습니다.");
						$("#signMsg").html("자동가입 방지문자가 일치하지 않습니다.<br> 입력하신 내용을 다시 확인하세요.");
					}else
					{
						if(confirm("회원가입을 진행하시겠습니까?"))
						{
							$("#joinForm").submit();
						}
							
					}
				},
				error: function(xhr, status, error){
					// 통신 오류 발생시	
				},
				complete : function(){
					// 통신이 성공하거나 실패했어도 마지막으로 이 함수를 타게된다.
				}			
			});	
		}
		
		
		function sendEmail()
		{
			DupCheckEmail();
			if(emailCheck != "Y")
			{
				alert("이메일이 유효하지 않습니다.");
				$("#email").focus();
				return;
			}
			$.ajax({
				url  : "mailSendCode.do",
				type : "get",
	            async: false,
				data : {
					email : $("#email").val()
				},
				success : function(data){
					console.log(data);
					if(data == null)
					{
						alert("이메일 발송에 실패했습니다.");
						return;
					}
					data = data.trim();
					code = data;
					
					alert("입력하신 이메일로 인증번호를 발송했습니다.");
					$("#emailOkNum").attr("placeholder","인증번호를 입력해주세요.");
					$("#emailOkNum").focus();
				}
			})
		}
		
		function confirmEmail()
		{
			if(code == null || code == "")
			{
				alert("인증번호를 발송해주세요.");
				return;
			}
			if( $("#emailOkNum").val() == "" )
			{
				alert("인증번호를 입력해주세요.");
				$("#confirmEmail").focus();
				return;
			}
			if($("#emailOkNum").val() != code )
			{
				alert("인증번호가 일치하지 않습니다.");
				codeCheck = "N";
				return;
			}
			alert("인증이 완료되었습니다.");
			codeCheck = "Y";
		}
		
	})
	
	
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<h2 class="subTitle">회원가입</h2>
	<div class="contentBox">
		<div class="inputBox">
			<form name="joinForm" id="joinForm" action="joinok.do" method="post">
			<table border="1" width="500px" class="tb">
				<tr>
					<th rowspan="2">아이디</th>
					<td width="380px"><input type="text" class="input inputM"
						name="userid" id="userid" placeholder="아이디를 입력하세요"></td>
				</tr>
				<tr>
					<td id="idCheckMsg" class="textLt paddingLs">아이디를 입력하세요.</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" class="input inputM"
						name="userpw" id="userpw" placeholder="비밀번호를 입력하세요"></td>
				</tr>
				<tr>
					<th>비밀번호 확인</th>
					<td><input type="password" class="input inputM"
						name="pwcheck" id="pwcheck" placeholder="비밀번호를 확인하세요"></td>
				</tr>
				<tr>
					<th>이름</th>
					<td><input type="text" class="input inputM"
						name="name" id="name" placeholder="이름을 입력하세요"></td>
				</tr>
				<tr>
					<th>성별</th>
					<td>
						<input type="radio" name="gender" id="genderM" value="M" checked>남자</input> 
						<input type="radio" name="gender" id="genderF" value="F">여자</input>
					</td>
				</tr>
				<tr>
					<th rowspan="2">이메일</th>
					<td style="position:relative;">
						<input type="text" class="input inputM" name="email" id="email" placeholder="이메일을 입력하세요">
						<div class="smBtn " style="position:absolute; top:5px; right:10px" id="sendMail">인증번호 발송</div>
					</td>
				</tr>
				<tr>
					<td id="emailCheckMsg" class="textLt paddingLs">이메일을 입력하세요.</td>
				</tr>
				<tr>
					<th>인증 번호</th>
					<td style="position:relative;">
						<input type="text" placeholder="인증번호를 발송해주세요." name="emailOkNum" id="emailOkNum" class="input inputM">
						<div class="smBtn " style="position:absolute; top:5px; right:10px" id="confirmEmail">인증번호 확인</div>
					</td>
				</tr>
				<tr>
					<th>자동가입<br>방지문자</th>
					<td>
					<img src="/control/sign.do"> 
					<input type="text" class="input inputM" 
						name="sign" id="sign" placeholder="자동입력 방지문자를 입력하세요"></td>
				</tr>
			</table>
			</form>
			<div class="box red" id="signMsg"></div>
		</div>
		<div class="bigBtnBox">
			<div class="bigBtn" id="joinOkBtn">가입완료</div>
			<div class="bigBtn" id="cancelBtn">취소</div>
		</div>
	</div>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>