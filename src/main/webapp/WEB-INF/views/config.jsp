<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	let emailCheck = "N"; //N - 이메일 유효하지 않음, X - 중복된 이메일, Y - 사용가능한 이메일
	let codeCheck = "N"; //N - 인증번호 미확인, Y - 확인 완료
	let code = "";

	$(function(){
		if(${ sessionScope.login == null }) 
		{
			document.location = "/control/list.do";
			return;
		}
		
		$("#menuBtn04").addClass("choosenBtn");
		
		$("#configOkBtn").click(function(){
			doConfig();
		});
		
		//이메일 변경 시, 이메일 유효성 및 인증번호 확인 초기화
		$("#email").change(function(){
			emailCheck = "N";
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
		
		$("#configCancelBtn").click(function(){
			if(confirm("취소하시겠습니까?")) 
				document.location = "/control/list.do"
		});
		
		$("#deleteUserBtn").click(function(){
			if(!confirm("회원 탈퇴하시겠습니까?")) return;
			document.location = "/control/userDelete.do?usernum=${ sessionScope.login.usernum }";
		});
		
		$("#pfImgChBtn").click(function(){
			$("#attach").click();
		});
	});
	
	function setProfileImage()
	{
		img_file = $("#attach").val();
		
		//파일 확장자가 이미지 인지 체크한다.
		fileName = img_file.slice(img_file.indexOf(".") + 1).toLowerCase();
		if(fileName != "jpg" && fileName != "png" &&  fileName != "gif" &&  fileName != "bmp")
		{
			alert("이미지 파일은 (jpg, png, gif, bmp) 형식만 등록 가능합니다.");
			return;
		}
		
		var formData = new FormData();
		var inputFile = $("input[name='attach']");
		var files = inputFile[0].files;
		console.log(files);
		
		for(var i =0;i<files.length;i++)
		{			
			formData.append("attach", files[i]);
		}
		
		$.ajax({
			url: 'profileImgWrite.do',
			processData : false,
			contentType : false,
			data : formData,
			type : "POST",
			success : function(result)
			{
				//alert("Uploaded ok");
				img_name = result.trim();
				
				$(".profileImg").attr("src","profileImgByName.do?profileimgp=" + img_name + "&profileimgf=" + img_name );
				
			}
		});		
		//$(".profileImg").attr("src",img_src);
	}
	
	function DupCheckEmail()
	{
		email = $("#email").val();
		
		$.ajax({
			type: "get",
			url : "emailcheck.do?email="+email,
			dataType: "html",
            async: false,
			success : function(data){
				// 통신이 성공적으로 이루어졌을때 이 함수를 타게된다.
				data = data.trim();
				ary = data.split(":");
				emailCheck = ary[0];
			},
			error: function(xhr, status, error){
				// 통신 오류 발생시	
			},
			complete : function(){
				// 통신이 성공하거나 실패했어도 마지막으로 이 함수를 타게된다.
			}			
		});	
	}
	
	function doConfig()
	{
		if($("#email").val() != "")
		{
			if(codeCheck != "Y")
			{
				alert("이메일 인증을 완료해주세요.");
				return;
			}
		}
		if($("#intro").val() == "")
		{
			alert("자기소개를 입력해주세요.");
			$("#intro").focus();
			return;
		}
		if( $("#userpwN").val() != $("#pwcheck").val())
		{
			alert("비밀번호가 일치하지 않습니다.");
			$("#userpwN").focus();
			return;
		}
		
		$.ajax({
			url  : "pwCheck.do",
			type : "post",
			data : {
				usernum : "${ sessionScope.login.usernum }",
				userpw  : $("#userpw").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "N")
				{
					alert("비밀번호가 일치하지 않습니다.");
					$("#userpw").focus();
					return;
				}
				if(confirm("수정하시겠습니까?")) $("#configFrm").submit();
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
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<h2 class="subTitle">설정</h2>
	<form action="userConfig.do" method="post" name="configFrm" id="configFrm">
	<input type="hidden" name="usernum" value="${ sessionScope.login.usernum }">
	<div class="box">
		<div class="textBox" id="userid">${ sessionScope.login.userid }</div>
		<br>
		<textarea class="pfIntroNote" placeholder="자기소개를 입력해주세요." name="intro" id="intro" >${ sessionScope.login.intro }</textarea>
		<br>
		<div class="textBox" id="pfPicture">
			<table>
				<tr>
					<td>
						<input type="file" id="attach" name="attach" style="display:none" onchange="setProfileImage();">
						<c:if test="${ sessionScope.login.profileimgf == null || sessionScope.login.profileimgf == ''  }">
							<img src="./resources/image/sample.png" width="100px" class="profileImg">
						</c:if>
						<c:if test="${ sessionScope.login.profileimgf != null && sessionScope.login.profileimgf != ''  }">
							<img src="/control/profileImgByName.do?profileimgp=${ sessionScope.login.profileimgp }&profileimgf=${ sessionScope.login.profileimgf }" width="100px" class="profileImg">
						</c:if>
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td class="Btn" id="pfImgChBtn">프로필 사진 변경</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="box">
		<table border="1" class="fullTb">
			<tr>
				<td width="200px">이메일</td>
				<td style="position:relative;">
					<input type="text" placeholder="변경할 이메일을 입력해주세요." name="email" id="email" class="configInput">
					<div class="smBtn " style="position:absolute; top:9px; right:10px" id="sendMail">인증번호 발송</div>
				</td>
			</tr>
			<tr>
				<td>이메일 인증 번호</td>
				<td style="position:relative;">
					<input type="text" placeholder="인증번호를 발송해주세요." name="emailOkNum" id="emailOkNum" class="configInput">
					<div class="smBtn " style="position:absolute; top:9px; right:10px" id="confirmEmail">인증번호 확인</div>
				</td>
			</tr>
			<tr>
				<td>변경할 비밀번호</td>
				<td><input type="password" placeholder="변경할 비밀번호를 입력해주세요." name="userpwN" id="userpwN"
					class="configInput"></td>
			</tr>
			<tr>
				<td>비밀번호 확인</td>
				<td><input type="password" placeholder="비밀번호를 확인해주세요." name="pwcheck" id="pwcheck"
					class="configInput"></td>
			</tr>
			<tr>
				<td>현재 비밀번호</td>
				<td><input type="text" placeholder="현재 비밀번호를 입력해주세요." name="userpw" id="userpw"
					class="configInput"></td>
			</tr>
		</table>
	</div>
	</form>
	<div class="bigBtnBox" id="BtnGroup">
		<div class="bigBtn" id="configOkBtn">설정 완료</div>
		<div class="bigBtn" id="configCancelBtn">취소</div>
	</div>
	<div class="bigBtn" id="deleteUserBtn">회원탈퇴</div>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>