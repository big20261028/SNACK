<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	var emailCheck = "N"; //X - 이메일 일치 데이터 있음, 그외 없음

	$(function(){
		$("#email").focus();
		
		$("#idSearchBtn").click(function(){
			idSearch();
		})
	})
		
	function idSearch()
	{
		if($("#email").val() == "")
		{
			alert("이메일을 입력해주세요.");
			$("#email").focus();
			return;
		}
		$.ajax({
			url  : "emailcheck.do",
			type : "get",
            async: false,
			data : {
				email : $("#email").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				ary = data.split(":");
				emailCheck = ary[0];
				if(emailCheck != "X")
				{
					alert("입력하신 이메일과 일치하는 계정이 없습니다. 입력 내용을 다시 확인해주세요.");
					$("#noticeBox").css("color","red");
					$("#noticeBox").html("입력하신 이메일과 일치하는 계정이 없습니다.<br> 입력내용을 다시 확인해주세요.");
					return;
				};
			}
		})
		if(emailCheck != "X") return;
		$.ajax({
			url  : "mailSendUserid.do",
			type : "get",
			data : {
				email : $("#email").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "N")
				{
					alert("이메일 발송에 실패했습니다.");
					return;
				}
				alert("입력하신 이메일로 가입된 아이디를 메일로 발송했습니다.");
				$("#noticeBox").css("color","blue");
				$("#noticeBox").html("입력하신 이메일로 가입된 아이디를 메일로 발송했습니다.");
			}
		})
	}
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->

	<h2 class="subTitle">아이디찾기</h2>
	<div class="contentBox">
		<div class="box">
			가입하신 계정에 등록된 이메일을 입력해주세요</br> 계정에 이메일이 등록되어 있다면 메일으로 아이디가 발송됩니다
		</div>
		<div class="inputBox">
			<table border="1" class="tb" width="400px">
				<tr>
					<th>이메일</th>
					<td width="300px">
						<input type="text" class="input inputM" name="email" id="email" placeholder="이메일을 입력해주세요">
					</td>
				</tr>
			</table>
		</div>
		<br>
		<div class="Btn loginBtn" id="idSearchBtn">아이디 찾기</div>
		<div class="box" id="noticeBox"></div>
	</div>

	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>