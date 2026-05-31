<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	$(function(){
		
		$("#userPageBtn").click(function(){
			document.location = "/control/UserPage/userpage.do?usernum=${ boardVO.usernum }"
		})
		$("#userFollow").click(function(){
			document.location = "/control/UserPage/follow.do?usernum=${ boardVO.usernum }"
		})
		$("#userFollower").click(function(){
			document.location = "/control/UserPage/follower.do?usernum=${ boardVO.usernum }"
		})
		$("#subscribe").click(function(){
			if(${ sessionScope.login == null }) return;
			 doSubscribe();
		})
		
		$("#recommendBtn").click(function(){
			if(${ sessionScope.login == null }) return;
			 doRecommend();
		})
		
		$("#modifyBtn").click(function(){
			if(${ sessionScope.login.isadmin == 'N' })
			{
				if(${ sessionScope.login == null || sessionScope.login.usernum != boardVO.usernum }) return;
			}
			document.location = "/control/modify.do?no=${ boardVO.no }";
		})
		$("#deleteBtn").click(function(){
			if(${ sessionScope.login == null || sessionScope.login.usernum != boardVO.usernum }) return;
			if(!confirm("글을 삭제하시겠습니까?")) return; 
			document.location = "/control/delete.do?no=${ boardVO.no }";
		})
		$("#listBtn").click(function(){
			document.location = "/control/list.do";
		})
		
		$("#replyOkBtn").click(function(){
			if(confirm("댓글을 등록하시겠습니까?"))	doReply();
		})
		
	})
	
	function doRecommend()
	{	
		$.ajax({
			type : "post",
			url  : "recommend.do",
			data :{
				usernum : "${ sessionScope.login.usernum }",
				no      : "${ boardVO.no }"
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				ary = data.split(":");
				
				if(ary[0] == "O") alert("추천하셨습니다.");
				if(ary[0] == "X") alert("추천을 취소하셨습니다.");
				$("#recommendCount").html(ary[1]);
			}
		});
	}
	
	function doSubscribe()
	{
		$.ajax({
			type : "post",
			url  : "subscribe.do",
			data :{
				fromnum : "${ sessionScope.login.usernum }",
				tonum   : "${ boardVO.usernum }"
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				ary = data.split(":");
				if(ary[0] == "O") alert("구독하셨습니다.");
				if(ary[0] == "X") alert("구독을 취소하셨습니다.");
				$("#followerCount").html(ary[1]);
			}
		});
	}
	
	function doReply()
	{
		if( $("#rnote").val() == "" )
		{
			alert("댓글을 입력해주세요.");
			$("#rnote").focus();
			return;
		}
		
		$.ajax({
			type : "post",
			url  : "/control/replyOk.do",
			data : {
				usernum : "${ sessionScope.login.usernum }",
				no      : "${ boardVO.no }",
				rnote   : $("#rnote").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "Y")
				{
					alert("댓글이 등록되었습니다.");
					replyList(1);
				}
				if(data == "N")
				{
					alert("댓글 등록에 실패했습니다.");
				}
			}
		});
	}
	
	function delReply(rno, usernum)
	{
		if( ${ sessionScope.login.isadmin == 'N' } )
		{
			if( ${ sessionScope.login.usernum == null } || "${ sessionScope.login.usernum }" != usernum  ) return;
		}
		
		if(!confirm("댓글을 삭제하시겠습니까?")) return;
		
		$.ajax({
			type : "post",
			url  : "delReply.do",
			data : {
				rno : rno,
				usernum : "${ sessionScope.login.usernum }"
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "Y")
				{
					alert("댓글이 삭제되었습니다.");
					replyList(1);
				}
				if(data == "N")
				{
					alert("댓글 삭제에 실패했습니다.");
				}
			}
		})
	}
	
	function replyList(page)
	{
		$.ajax({
			type : "post",
			url  : "reply.do",
			headers: {
		        "Accept": "application/json"
		    },
			data : {
				pageno : page,
				no     : "${ boardVO.no }"
			},
			success : function(data){
				
				html = "";
				for(let i = 0; i < data.length; i++){
					html += '<tr>';
					html += '<td width="100px">';
					html += '<a href="/control//UserPage/userpage.do?usernum=' + data[i].usernum + '">' + data[i].userid + '</a>';
					html += '</td>';
					html += '<td>' + data[i].rnote;
					flag = ${sessionScope.login.isadmin == 'Y' } || data[i].usernum == "${ sessionScope.login.usernum }";
					if( flag  )
					{
						html += '<div class="smBtn marginL" id="replyDelBtn" onclick="delReply(' + data[i].rno + ', ${ sessionScope.login.usernum });">삭제</div>';
					}
					html += '</td>';
					html += '<td width="100px">' + data[i].rwdate + '</td>';
					html += '</tr>';
				}
				$("#replyContentBox").html(html);
			}			
		});	
	}
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<div class="viewBoxOuter">
		<div class="viewBoxInner">
			<table border="1" class="viewTb" id="tb01">
				<tr>
					<td width="80px">제목</td>
					<td class="textLt paddingL">${ boardVO.title }</td>
					<td width="80px">조회수</td>
					<td width="80px">${ boardVO.hit }</td>
				</tr>
			</table>
			<table border="1" class="viewTb" id="tb02">
				<tr>
					<td width="80px">내용</td>
					<td class="textLt padding">${ boardVO.note }</td>
				</tr>
			</table>
			<table border="1" class="viewTb" id="tb03">
				<tr>
					<td width="80px">첨부파일</td>
					<td class="textLt paddingL"><c:if
							test="${ boardVO.fname == null || boardVO.fname == ''  }">
							등록된 첨부파일 없음
						</c:if> <c:if test="${ boardVO.fname != null && boardVO.fname != ''  }">
							<a href="down.do?no=${ boardVO.no }">${ boardVO.fname }</a>
						</c:if>
					</td>
				</tr>
			</table>
			<table border="1" class="viewTb" id="tb04">
				<tr>
					<td width="80px">프로필</td>
					<td class="textLt">
						<div class="positionR padding">
							<div class="Btn" id="userPageBtn">${ boardVO.userid }</div>
							<br>
							<div class="viewIntroBox">${ boardVO.intro }</div>
							<br>
							<div class="Btn" id="userFollow">
								팔로우 <span class="lightcoral" id="followCount">${ boardVO.followCnt }</span>
							</div>
							<div class="Btn" id="userFollower">
								팔로워 <span class="lightcoral" id="followerCount">${ boardVO.followerCnt }</span>
							</div>
							<div class="textBox" id="viewPfImg">
								<c:if
									test="${ boardVO.profileimgf == null || boardVO.profileimgf == ''  }">
									<img src="/control/resources/image/sample.png" width="100px"
										class="profileImg">
								</c:if>
								<c:if
									test="${ boardVO.profileimgf != null && boardVO.profileimgf != ''  }">
									<img src="/control/profileImg.do?no=${ boardVO.no }"
										width="100px" class="profileImg">
								</c:if>
								<br>
								<c:if test="${ sessionScope.login != null }">
									<div class="Btn marginT" id="subscribe">구독하기</div>
								</c:if>
							</div>
						</div>
					</td>
				</tr>
			</table>
			<div id="btnBox" class="positionR">
				<table border="1" id="tb05" class="textCt">
					<tr>
						<td width="80px" class="tdBtn" id="recommendBtn">추천</td>
						<!-- click 이벤트에서 로그인 여부 검사 -->
						<td width="80px" id="recommendCount">${ boardVO.recCount }</td>
					</tr>
				</table>
				<div id="viewBtnBox">
					<c:if test="${ sessionScope.login.isadmin == 'Y' || sessionScope.login.usernum == boardVO.usernum }">
						<div class="Btn viewBtn" id="modifyBtn">수정</div>
						<div class="Btn viewBtn" id="deleteBtn">삭제</div>
					</c:if>
					<div class="Btn viewBtn" id="listBtn">목록</div>
				</div>
			</div>
		</div>

		<div class="viewBoxInner">
			<c:if test="${ sessionScope.login != null }">
				<table border="1" class="fullTb" id="replyInsertRow">
					<tr>
						<td width="100px">${ sessionScope.login.userid }</td>
						<td><input type="text" class="input inputM" name="rnote" id="rnote"
							placeholder="댓글을 입력해주세요."></td>
						<td class="tdBtn" id="replyOkBtn" width="100px">등록하기</td>
					</tr>
				</table>
			</c:if>
			<table border="1" class="fullTb" id="replyContentBox">
				<c:set var="seqno"
					value="${ total - ((searchVO.pageno - 1) * 10) + 1 }"></c:set>
				<c:forEach var="item" items="${ replyList }">
					<tr>
						<td width="100px"><a
							href="/control/UserPage/userpage.do?usernum=${ item.usernum }">${ item.userid }</a></td>
						<td>${ item.rnote }
							<c:if test="${ item.usernum == sessionScope.login.usernum || sessionScope.login.isadmin == 'Y' }">
								<div class="smBtn marginL" id="replyDelBtn" onclick="delReply(${ item.rno }, ${ item.usernum });">삭제</div>
							</c:if>
						</td>
						<td width="100px">${ item.rwdate }</td>
					</tr>
				</c:forEach>
			</table>

			<div id="pageNumber">
				<c:if test="${ startbk > 10 }">
					<a href="javascript:replyList(${startbk - 1})">◀</a>
				</c:if>
				<c:forEach var="page" begin="${startbk}" end="${endbk}">
					<a href="javascript:replyList(${page});">${page}</a>
				</c:forEach>
				<c:if test="${ endbk < maxpage }">
					<a href="javascript:replyList(${endbk + 1});">▶</a>
				</c:if>
			</div>
		</div>
	</div>
	<!-- 이곳에 컨텐츠 입력 -->
	
</section>
<%@ include file="./include/tail.jsp"%>