<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	$(function(){
		$("#menuBtn05").addClass("choosenBtn");
		
		switch("${ userVO.status }")
		{
			case "Y" : 
				$("#statusY").attr("checked",true);
				break;
			case "N" : 
				$("#statusN").attr("checked",true);
				break;
			case "B" : 
				$("#statusB").attr("checked",true);
				break;
		}
		switch("${ userVO.isadmin }")
		{
			case "Y" :
				$("#isadminY").attr("checked",true);
				break;
			case "N" :
				$("#isadminN").attr("checked",true);
				break;
		}
		switch("${ userVO.gender }")
		{
			case "M" :
				$("#genderM").attr("checked",true);
				break;
			case "F" :
				$("#genderF").attr("checked",true);
				break;
		}
		
		
		$("#pfImgChange").click(function(){
			$("#attach").click();
		})
		%
		$("#modifyOkBtn").click(function(){
			userModify();
		})
		$("#boardDelBtn").click(function(){
			if(!isBoardChecked()) return;
			if(!confirm("선택한 게시글을 삭제하시겠습니까?")) return;
			$("#userBoardFrm").submit();
		})
		$("#replyDelBtn").click(function(){
			if(!isReplyChecked()) return;
			if(!confirm("선택한 댓글을 삭제하시겠습니까?")) return;
			$("#userReplyFrm").submit();
		})
	})
	
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
		formData.append("usernum", "${ userVO.usernum }"); // usernum도 FormData에 추가
		
		$.ajax({
			url: 'profileImgWriteUser.do',
			processData : false,
			contentType : false,
			data : formData,
			type : "POST",
			success : function(result)
			{
				//alert("Uploaded ok");
				img_name = result.trim();
				
				$(".userProfileImg").attr("src","profileImgByName.do?profileimgp=" + img_name + "&profileimgf=" + img_name );
				
			}
		});		
	}
	
	function loadUserBoardList(page) {
	    $.ajax({
	        url : "userBoard.do",
	        type : "post",
	        headers: {
	            "Accept": "application/json"
	        },
	        dataType : "json", // JSON 형식으로 받도록 변경
	        data : {
	            pageno  : page,
	            usernum : "${ userVO.usernum }"
	        },
	        success : function(data) {
	            console.log(data);
	            var html = "";
	            html += '<tr>';
	            html += '<th width="40px"></th>';
	            html += '<th width="60px">게시글<br>번호</th>';
	            html += '<th>제목</th>';
	            html += '<th width="90px">작성일자</th>';
	            html += '<th width="50px">조회수</th>';
	            html += '<th width="50px">추천수</th>';
	            html += '<th width="50px">댓글수</th>';
	            html += '</tr>';
	            
	            if (data.list == null || data.list.length === 0) {
	                html += '<tr><td colspan="7">조회 결과가 없습니다.</td></tr>';
	            } else {
	                $.each(data.list, function(index, board) {
	                    html += '<tr>';
	                    html += '<td><input type="checkBox" class="checkBoxClass" name="no" value="' + board.no + '"></td>';
	                    html += '<td>' + board.no + '</td>';
	                    html += '<td><a href="/control/view.do?no=' + board.no + '">' + board.title + '</a></td>';
	                    html += '<td>' + board.wdate + '</td>';
	                    html += '<td>' + board.hit + '</td>';
	                    html += '<td>' + board.recCount + '</td>';
	                    html += '<td>' + board.repCount + '</td>';
	                    html += '</tr>';
	                });
	            }

	            $("#userBoardAjax").html(html);

	            // 페이지 번호 업데이트
	            var pageHtml = "";
	            if (data.startbk > 10) {
	                pageHtml += '<a href="javascript:loadUserBoardList(' + (data.startbk - 1) + ')">◀</a>';
	            }
	            for (var i = data.startbk; i <= data.endbk; i++) {
	                pageHtml += '<a href="javascript:loadUserBoardList(' + i + ')">' + i + '</a> ';
	            }
	            if (data.endbk < data.maxpage) {
	                pageHtml += '<a href="javascript:loadUserBoardList(' + (data.endbk + 1) + ')">▶</a>';
	            }
	            $("#pageNumberBoard").html(pageHtml);
	        },
	        error: function(xhr, status, error) {
	            console.error("게시글 목록 AJAX 요청 실패:", error);
	        }
	    });
	}
	
	function loadUserReplyList(page){
		$.ajax({
	        url : "userReply.do",
	        type : "post",
	        headers: {
	            "Accept": "application/json"
	        },
	        dataType : "json", // JSON 형식으로 받도록 변경
	        data : {
	            pageno  : page,
	            usernum : "${ userVO.usernum }"
	        },
	        success : function(data) {
	            console.log(data);
	            
	            var html = "";
	            html += '<tr>';
	            html += '<th width="40px"></th>';
	            html += '<th width="60px">게시글<br>번호</th>';
	            html += '<th width="60px">댓글<br>번호</th>';
	            html += '<th>내용</th>';
	            html += '<th width="100px">작성일자</th>';
	            html += '</tr>';

	            if (data.list == null || data.list.length === 0) {
	                html += '<tr><td colspan="5">조회 결과가 없습니다.</td></tr>';
	            } else {
	                $.each(data.list, function(index, reply) {
	                    html += '<tr>';
	                    html += '<td><input type="checkBox" class="checkBoxClassReply" name="rno" value="' + reply.rno + '"></td>';
	                    html += '<td>' + reply.no + '</td>';
	                    html += '<td>' + reply.rno + '</td>';
	                    html += '<td class="textLt paddingL"><a href="/control/view.do?no=' + reply.no + '">' + reply.rnote + '</a></td>';
	                    html += '<td>' + reply.rwdate + '</td>';
	                    html += '</tr>';
	                });
	            }

	            $("#userReplyAjax").html(html);

	            // 페이지 번호 업데이트
	            var pageHtml = "";
	            if (data.startbk > 10) {
	                pageHtml += '<a href="javascript:loadUserReplyList(' + (data.startbk - 1) + ')">◀</a>';
	            }
	            for (var i = data.startbk; i <= data.endbk; i++) {
	                pageHtml += '<a href="javascript:loadUserReplyList(' + i + ')">' + i + '</a> ';
	            }
	            if (data.endbk < data.maxpage) {
	                pageHtml += '<a href="javascript:loadUserReplyList(' + (data.endbk + 1) + ')">▶</a>';
	            }
	            $("#pageNumberReply").html(pageHtml);
	        },
	        error: function(xhr, status, error) {
	            console.error("게시글 목록 AJAX 요청 실패:", error);
	        }
		})
	}
	
	function isBoardChecked()
	{
		let count = 0;
		$(".checkBoxClass").each(function() {
		    if ($(this).prop("checked")) {
		        count++;
		    }
		});
		if(count == 0)
		{
			alert("처리할 회원을 선택해주세요.");
			return false;
		}
		return true;
	}
	function isReplyChecked()
	{
		let count = 0;
		$(".checkBoxClassReply").each(function() {
		    if ($(this).prop("checked")) {
		        count++;
		    }
		});
		if(count == 0)
		{
			alert("처리할 회원을 선택해주세요.");
			return false;
		}
		return true;
	}
	
	function userModify()
	{
		if( $("#name").val() == "" )
		{
			alert("변경할 이름을 입력해주세요.");
			$("#name").focus();
			return;
		}
		if( $("#email").val() == "" )
		{
			alert("변경할 이메일을 입력해주세요.");
			$("#email").focus();
			return;
		}
		if( $("#userpw").val() != $("#pwcheck").val())
		{
			alert("비밀번호가 일치하지 않습니다.");
			$("#userpw").focus();
			return;
		}
		if( $("#intro").val() == "" )
		{
			alert("변경할 자기소개를 입력해주세요.");
			$("#intro").focus();
			return;
		}
		if(confirm("회원 정보를 수정하시겠습니까?")) $("#userModifyFrm").submit();
	}
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<h2 class="subTitle">${ userVO.userid }님의 회원정보</h2>
	<div class="box">
		<form action="userModify.do" method="post" name="userModifyFrm" id="userModifyFrm">
			<input type="hidden" name="usernum" value="${ userVO.usernum }">
			<table border="1" class="fullTbSm">
				<tr>
					<td>아이디</td>
					<td width="200px">${ userVO.userid }</td>
					<td rowspan="3">프로필<br>사진</td>
					<td rowspan="3" width="200px">
						<c:if test="${ userVO.profileimgf == null || userVO.profileimgf == ''  }">
							<img src="/control/resources/image/sample.png" width="80px" class="profileImg marginT userProfileImg">
						</c:if>
						<c:if test="${ userVO.profileimgf != null && userVO.profileimgf != '' }">
							<img src="/control/profileImgByName.do?profileimgp=${ userVO.profileimgp }&profileimgf=${ userVO.profileimgf }" width="80px" class="profileImg marginT userProfileImg">
						</c:if>
						<br>
						<input type="file" id="attach" name="attach" style="display:none" onchange="setProfileImage();">
						<div class="Btn marginB marginT" id="pfImgChange">사진 변경</div>
					</td>
				</tr>
				<tr>
					<td>회원번호</td>
					<td>${ userVO.usernum }</td>
				</tr>
				<tr>
					<td>가입일자</td>
					<td>${ userVO.joindate }</td>
				</tr>
				<tr height="45px">
					<td>상태</td>
					<td>
						<input type="radio" value="Y" name="status" id="statusY">정상
						<input type="radio" value="N" name="status" id="statusN"><span style="color:blue">탈퇴</span>
						<input type="radio" value="B" name="status" id="statusB"><span style="color:red">정지</span>
					</td>
					<td>관리자 권한</td>
					<td>
						<input type="radio" value="Y" name="isadmin" id="isadminY">관리자
						<input type="radio" value="N" name="isadmin" id="isadminN">일반
					</td>				
				</tr>
				<tr>
					<td>이름</td>
					<td>
						<input class="input userDataInput" type="text" name="name" id="name" value="${ userVO.name }">
					</td>
					<td>성별</td>
					<td>
						<input type="radio" value="M" name="gender" id="genderM">남자
						<input type="radio" value="F" name="gender" id="genderF">여자
					</td>
				</tr>
				<tr>
					<td>이메일</td>
					<td colspan="3" width="500px">
						<input class="input userDataInputL" type="text" name="email" id="email" value="${ userVO.email }">
					</td>
				</tr>
				<tr>
					<td>변경할<br>비밀번호</td>
					<td>
						<input class="input userDataInput" type="password" name="userpw" id="userpw" 
							placeholder="변경할 비밀번호를 입력해주세요.">
					</td>
					<td>비밀번호<br>확인
					</td>
					<td>
						<input class="input userDataInput" type="password" name="pwcheck" id="pwcheck" 
							placeholder="비밀번호를 확인해주세요.">
					</td>
				</tr>
				<tr>
					<td>자기소개</td>
					<td colspan="3" width="500px">
						<input class="input userDataInputL" type="text" name="intro" id="intro" value="${ userVO.intro }">
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="box textRt borderB">
		<div class="Btn" id="modifyOkBtn">수정완료</div>
	</div>

	<div class="positionR">
		<h2 class="subTitle">작성한 글</h2>
		<div class="Btn userDataDel" id="boardDelBtn">삭제</div>
	</div>
	<div class="box borderB">
		<form action="userBoardDelete.do" method="post" name="userBoardFrm" id="userBoardFrm">
			<input type="hidden" name="usernum" value="${ userVO.usernum }">
			<table border="1" class="fullTb" id="userBoardAjax">
				<tr>
					<th width="40px"></th>
					<th width="60px">게시글<br>번호</th>
					<th>제목</th>
					<th width="90px">작성일자</th>
					<th width="50px">조회수</th>
					<th width="50px">추천수</th>
					<th width="50px">댓글수</th>
				</tr>
				<c:if test="${ totalB == 0 }">
					<tr><td colspan="7">조회 결과가 없습니다.</td></tr>
			    </c:if>
			    <c:forEach var="item" items="${ boardList }">
	          		<tr>
	          			<td><input type="checkBox" class="checkBoxClass" name="no" value="${ item.no }"></td>
						<td>${ item.no }</td>
						<td><a href='/control/view.do?no=${ item.no }'>${ item.title }</a></td>
						<td>${ item.wdate }</td>
						<td>${ item.hit }</td>
						<td>${ item.recCount }</td>
						<td>${ item.repCount }</td>
					</tr>
		        </c:forEach>
			</table>
		</form>
		<div class="textCt marginT" id="pageNumberBoard">
			<c:if test="${ startbkB > 10 }"> 
				<a href="javascript:loadUserBoardList(${startbkB - 1})">◀</a>
			</c:if> 	
		 	<c:forEach var="pageB" begin="${startbkB}" end="${endbkB}">
		 		<a href="javascript:loadUserBoardList(${ pageB })">${pageB}</a>
		 	</c:forEach>
		 	<c:if test="${ endbkB < maxpageB }"> 
		 		<a href="javascript:loadUserBoardList(${endbkB + 1})">▶</a>
		 	</c:if>
		</div>
	</div>

	<div class="positionR">
		<h2 class="subTitle">작성한 댓글</h2>
		<div class="Btn userDataDel" id="replyDelBtn">삭제</div>
	</div>
	<div class="box borderB">
		<form action="userReplyDelete.do" method="post" name="userReplyFrm" id="userReplyFrm">
			<input type="hidden" name="usernum" value="${ userVO.usernum }">
			<table border="1" class="fullTb" id="userReplyAjax">
				<tr>
					<th width="40px"></th>
					<th width="60px">게시글<br>번호</th>
					<th width="60px">댓글<br>번호</th>
					<th>내용</th>
					<th width="100px">작성일자</th>
				</tr>
				<c:if test="${ totalR == 0 }">
					<tr><td colspan="5">조회 결과가 없습니다.</td></tr>
			    </c:if>
			    <c:forEach var="item" items="${ replyList }">
	          		<tr>
	          			<td><input type="checkBox" class="checkBoxClassReply" name="rno" value="${ item.rno }"></td>
						<td>${ item.no }</td>
						<td>${ item.rno }</td>
						<td><a href='/control/view.do?no=${ item.no }'>${ item.rnote }</a></td>
						<td>${ item.rwdate }</td>
					</tr>
		        </c:forEach>
	
			</table>
		</form>
		<div class="textCt marginT" id="pageNumberReply">
			<c:if test="${ startbkR > 10 }"> 
				<a href="javascript:loadUserReplyList(${startbkR - 1})">◀</a>
			</c:if> 	
		 	<c:forEach var="pageR" begin="${startbkR}" end="${endbkR}">
		 		<a href="javascript:loadUserReplyList(${ pageR })">${pageR}</a>
		 	</c:forEach>
		 	<c:if test="${ endbkR < maxpageR }"> 
		 		<a href="javascript:loadUserReplyList(${endbkR + 1})">▶</a>
		 	</c:if>
		</div>
	</div>
	<br>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>