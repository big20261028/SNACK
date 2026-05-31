<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	$(function(){
		$("#login").addClass("choosenBtn");
		
		$("#userid").focus();
		$("#userid").attr("value","admin");
		$("#userpw").attr("value","1234");
		
		$("#loginBtn").click(function(){
			if( $("#userid").val() =="" )
			{
				alert("아이디를 입력해주세요.");
				$("#userid").focus();
				return;
			}
			if( $("#userpw").val() =="" )
			{
				alert("비밀번호를 입력해주세요.");
				$("#userpw").focus();
				return;
			}
			$.ajax({
				type : "post",
				url : "loginOk.do",
				data : {
					userid : $("#userid").val(),
					userpw : $("#userpw").val()
				},
				success : function(data){
					data = data.trim();
					if(data == "N")
					{
						alert("회원 탈퇴 처리된 계정입니다.");
						return;
					}
					if(data == "B")
					{
						alert("회원 정지된 계정입니다.");
						return;
					}
					
					if(data == "X")
					{
						alert("아이디 또는 비밀번호가 일치하지 않습니다.");
						$("#loginMsg").html("아이디 또는 비밀번호가 일치하지 않습니다.<br> 입력하신 내용을 다시 확인해주세요.");
					}else
					{
						alert("로그인이 완료되었습니다.");
						document.location = "/control/list.do";
					}
				}
			})
		})
	})
</script>
<section>
	<h2 class="subTitle">로그인</h2>
	<div class="contentBox">
		<div class="inputBox">
			<table border="1" class="tb" width="400px">
				<tr>
					<th>아이디</th>
					<td width="300px"><input class="input userDataInputL" name="userid" id="userid"
						type="text" placeholder="아이디를 입력해 주세요"></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input class="input userDataInputL" type="password" name="userpw" id="userpw"
						placeholder="비밀번호를 입력해 주세요"></td>
				</tr>
			</table>
		</div>
		<div class="Btn loginBtn" id="loginBtn">로그인</div>
		<div id="box">
			<a href="/control/idsearch.do" id="idsearch">아이디찾기</a> | <a
				href="/control/pwsearch.do" id="pwsearch">비밀번호찾기</a> | <a
				href="/control/join.do" id="join">회원가입</a>
		</div>

		<div class="box red" id="loginMsg">
			
		</div>
	</div>
</section>
<%@ include file="./include/tail.jsp"%>