<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	let userCheck = "N" //

	$(function(){
		$("#userid").focus();
		
		$("#pwSearchBtn").click(function(){
			 searchPw();
		})
	})
	
	function searchPw()
	{
		if($("#userid").val() == "")
		{
			alert("아이디를 입력해주세요.");
			$("#userid").focus();
			return;
		}
		if($("#email").val() == "")
		{
			alert("이메일을 입력해주세요.");
			$("#email").focus();
			return;
		}
		$.ajax({
			url  : "searchUser.do",
			type : "get",
            async: false,
			data : {
				userid : $("#userid").val(),
				email  : $("#email").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				userCheck = data;
				if(userCheck != "Y")
				{
					alert("입력하신 정보와 일치하는 계정이 없습니다.입력내용을 다시 확인해주세요.");
					$("#noticeBox").css("color","red");
					$("#noticeBox").html("입력하신 정보와 일치하는 계정이 없습니다.<br> 입력내용을 다시 확인해주세요.");
					return;
				};
			}
		})
		if(userCheck != "Y") return;
		$.ajax({
			url  : "mailSendUserpw.do",
			type : "get",
            async: false,
			data : {
				userid : $("#userid").val(),
				email  : $("#email").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "N")
				{
					alert("이메일 발송에 실패했습니다.");
					return;
				}
				alert("입력하신 이메일로 임시 비밀번호를 발송했습니다.");
				$("#noticeBox").css("color","blue");
				$("#noticeBox").html("입력하신 이메일로 임시 비밀번호를 발송했습니다.");
			}
		})
	}
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<h2 class="subTitle">비밀번호 찾기</h2>
	<div class="contentBox">
		<div class="box">
			가입하신 계정에 등록된 아이디와 이메일을 입력해주세요<br> 입력하신 정보가 올바르다면 해당 이메일로 임시
			비밀번호가 발송됩니다
		</div>
		<div class="inputBox">
			<table border="1" class="tb" width="400px">
				<tr>
					<th>아이디</th>
					<td width="300px"><input type="text" class="input inputM" name="userid" id="userid"
						placeholder="아이디를 입력해주세요"></td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input type="text" class="input inputM" name="email" id="email"
						placeholder="이메일을 입력해주세요"></td>
				</tr>
			</table>
		</div>
		<br>
		<div class="Btn loginBtn" id="pwSearchBtn">비밀번호 찾기</div>
		<div class="box" id="noticeBox"></div>
	</div>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>