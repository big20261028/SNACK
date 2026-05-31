<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../include/head.jsp"%>
<script>
	$(function(){
		if(${ sessionScope.login.usernum == userVO.usernum })
			$("#menuBtn03").addClass("choosenBtn");
		
		$("#subscribe").click(function(){
			if(${ sessionScope.login == null }) return;
			 doSubscribe();
		});
		$("#userFollow").click(function(){
			document.location = "/control/UserPage/follow.do?usernum=${ userVO.usernum }";
		});
		$("#userFollower").click(function(){
			document.location = "/control/UserPage/follower.do?usernum=${ userVO.usernum }";
		});
		
		$("#mypageBoardList").click(function(){
			loadUserBoardList(1);
		})
		$("#mypageReplyList").click(function(){
			loadUserReplyList(1);
		})
		$("#mypageGuestBook").click(function(){
			loadUserGuestList(1);
		})
		
	})
	
	function doSubscribe()
	{
		$.ajax({
			type : "post",
			url  : "/control/subscribe.do",
			data :{
				fromnum : "${ sessionScope.login.usernum }",
				tonum   : "${ userVO.usernum }"
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
	
	function loadUserBoardList(page) {
	    $.ajax({
	        url : "board.do",
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
	            html += '<th width="50px">번호</th>';
	            html += '<th>제목</th>';
	            html += '<th width="100px">작성일자</th>';
	            html += '<th width="50px">조회수</th>';
	            html += '<th width="50px">추천수</th>';
	            html += '</tr>';

	            if (data.list == null || data.list.length === 0) {
	                html += '<tr><td colspan="5">조회 결과가 없습니다.</td></tr>';
	            } else {
	                var seqno = data.total - ((data.searchVO.pageno - 1) * 10);
	                $.each(data.list, function(index, board) {
	                    html += '<tr>';
	                    html += '<td>' + seqno-- + '</td>';
	                    html += '<td class="textLt paddingL"><a href="/control/view.do?no=' + board.no + '">' + board.title + '</a>';
	                    if (board.repCount != 0) {
	                        html += '<span style="color:#ff6600">(' + board.repCount + ')</span>';
	                    }
	                    html += '</td>';
	                    html += '<td>' + board.wdate + '</td>';
	                    html += '<td>' + board.hit + '</td>';
	                    html += '<td>' + board.recCount + '</td>';
	                    html += '</tr>';
	                });
	            }

	            $("#ajaxContentBox").html(html);
	            
	            $(".mypageBtn").removeClass("choosenBtn");

	    		$("#mypageBoardList").addClass("choosenBtn");

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
	            $("#pageNumber").html(pageHtml);
	        },
	        error: function(xhr, status, error) {
	            console.error("게시글 목록 AJAX 요청 실패:", error);
	        }
	    });
	}
	
	function loadUserReplyList(page){
		$.ajax({
	        url : "reply.do",
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
	            html += '<th width="60px">게시글<br>번호</th>';
	            html += '<th width="60px">댓글<br>번호</th>';
	            html += '<th>내용</th>';
	            html += '<th width="100px">작성일자</th>';
	            html += '</tr>';

	            if (data.list == null || data.list.length === 0) {
	                html += '<tr><td colspan="4">조회 결과가 없습니다.</td></tr>';
	            } else {
	                $.each(data.list, function(index, reply) {
	                    html += '<tr>';
	                    html += '<td>' + reply.no + '</td>';
	                    html += '<td>' + reply.rno + '</td>';
	                    html += '<td class="textLt paddingL"><a href="/control/view.do?no=' + reply.no + '">' + reply.rnote + '</a></td>';
	                    html += '<td>' + reply.rwdate + '</td>';
	                    html += '</tr>';
	                });
	            }

	            $("#ajaxContentBox").html(html);
	            
	            $(".mypageBtn").removeClass("choosenBtn");
	            $("#mypageReplyList").addClass("choosenBtn");

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
	            $("#pageNumber").html(pageHtml);
	        },
	        error: function(xhr, status, error) {
	            console.error("게시글 목록 AJAX 요청 실패:", error);
	        }
		})
	}
	
	function loadUserGuestList(page)
	{
		$.ajax({
	        url : "guest.do",
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
	        	if(${ sessionScope.login != null })
        		{
	        		html += '<tr>';
		            html += '<th width="100px">${ sessionScope.login.userid }</th>';
		            html += '<th><input type="text" class="input inputM" name="gnote" id="gnote"></th>';
		            html += '<th width="100px" class="tdBtn" id="guestOkBtn" onclick="guestInsert();">등록하기</th>';
		            html += '</tr>';
        		}
	        	
				if (data.list == null || data.list.length === 0) {
				       html += '<tr><td colspan="3">조회 결과가 없습니다.</td></tr>';
				} else {
					$.each(data.list, function(index, guest) {
				   		html += '<tr>';
				   		html += '<td width="100px"><a href="/control/UserPage/userpage.do?usernum=' + guest.guestnum + '">' + guest.userid + '</a></td>';
				   		html += "<td>" + guest.gnote;
				   		if(${ sessionScope.login.isadmin == "Y" } || guest.guestnum == "${ sessionScope.login.usernum }")
			   			{
				   			html += " <div class='smBtn guestDelBtn' onclick='guestDelete(" + guest.gno + ",${ sessionScope.login.usernum });'>삭제</div>";
			   			}
				   		html += "</td>";
				   		html += '<td width="100px">' + guest.gwdate + '</td>';
						html += '</tr>';
					});
				}
				
				$("#ajaxContentBox").html(html);
				 
				$(".mypageBtn").removeClass("choosenBtn");
				$("#mypageGuestBook").addClass("choosenBtn");
				
				// 페이지 번호 업데이트
	            var pageHtml = "";
	            if (data.startbk > 10) {
	                pageHtml += '<a href="javascript:loadUserGuestList(' + (data.startbk - 1) + ')">◀</a>';
	            }
	            for (var i = data.startbk; i <= data.endbk; i++) {
	                pageHtml += '<a href="javascript:loadUserGuestList(' + i + ')">' + i + '</a> ';
	            }
	            if (data.endbk < data.maxpage) {
	                pageHtml += '<a href="javascript:loadUserGuestList(' + (data.endbk + 1) + ')">▶</a>';
	            }
	            $("#pageNumber").html(pageHtml);
	        },
	        error: function(xhr, status, error) {
	            console.error("게시글 목록 AJAX 요청 실패:", error);
	        }
		});
	}
	
	function guestInsert()
	{
		if($("#gnote").val() == "")
		{
			alert("방명록을 입력해주세요.");
			$("#gnote").focus();
			return;
		}
		if(!confirm("방명록을 등록하시겠습니까?")) return;
		$.ajax({
			url : "guestInsert.do",
			type : "post",
			data : {
				hostnum  : "${ userVO.usernum }",
				guestnum : "${ sessionScope.login.usernum }",
				gnote    : $("#gnote").val()
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "Y")
				{
					alert("방명록이 등록되었습니다.");
					loadUserGuestList(1);
				}
				if(data == "N")
				{
					alert("방명록 등록에 실패했습니다.");
				}
			}
		})
	}
	
	function guestDelete(gno,guestnum)
	{
		if(!confirm("댓글을 삭제하시겠습니까?")) return;
		$.ajax({
			url : "guestDelete.do",
			type : "post",
			data : {
				gno      : gno,
				guestnum : guestnum
			},
			success : function(data){
				console.log(data);
				data = data.trim();
				if(data == "Y")
				{
					alert("방명록이 삭제되었습니다.");
					loadUserGuestList(1);
				}
				if(data == "N")
				{
					alert("방명록 삭제에 실패했습니다.");
				}
			}
		});
	}
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<h2 class="subTitle">
		<c:if test="${ sessionScope.login.usernum == userVO.usernum }">
      		마이페이지
      	</c:if>
      	<c:if test="${ sessionScope.login.usernum != userVO.usernum }">
      		${ userVO.userid } 님의 페이지
      	</c:if>
	</h2>
	<div class="marginL">
		<div class="textBox" id="userid">${ userVO.userid }</div>
		<br>
		<div class="textBox pfIntroBox">${ userVO.intro }</div>
		<br>
		<div class="textBox">
			<table>
				<tr>
					<td class="Btn" id="userFollow">팔로우</td>
				</tr>
				<tr>
					<td class="red borderBS">${ userVO.followCnt }</td>
				</tr>
			</table>
		</div>
		<div class="textBox">
			<table>
				<tr>
					<td class="Btn" id="userFollower">팔로워</td>
				</tr>
				<tr>
					<td class="red borderBS" id="followerCount">${ userVO.followerCnt }</td>
				</tr>
			</table>
		</div>
		<div class="textBox" id="mypagePfImg">
			<table>
				<tr>
					<td>
						<c:if test="${ userVO.profileimgf == null || userVO.profileimgf == ''  }">
							<img src="/control/resources/image/sample.png" width="100px" class="profileImg">
						</c:if>
						<c:if test="${ userVO.profileimgf != null && userVO.profileimgf != '' }">
							<img src="/control/profileImgByName.do?profileimgp=${ userVO.profileimgp }&profileimgf=${ userVO.profileimgf }" width="100px" class="profileImg">
						</c:if>
					</td>
				</tr>
				<tr><td></td></tr>
				<tr>
					<c:if test="${ sessionScope.login.usernum == userVO.usernum }">
		      			<td class="Btn" id="profileConfig">프로필 편집</td>
			      	</c:if>
			      	<c:if test="${ sessionScope.login != null && sessionScope.login.usernum != userVO.usernum }">
			      		<td class="Btn" id="subscribe">구독하기</td>
			      	</c:if>
				</tr>
			</table>
		</div>
	</div>
	<div id="mypageBtnBox">
		<div class="whiteBtn mypageBtn choosenBtn" id="mypageBoardList">작성한
			글</div>
		<div class="whiteBtn mypageBtn" id="mypageReplyList">작성한 댓글</div>
		<div class="whiteBtn mypageBtn" id="mypageGuestBook">방명록</div>
	</div>
	<div class="box">
		<table class="fullTb" border="1" id="ajaxContentBox">
			<tr>
				<th width="50px">번호</th>
				<th>제목</th>
				<th width="100px">작성일자</th>
				<th width="50px">조회수</th>
				<th width="50px">추천수</th>
			</tr>
			<c:if test="${ total == 0 }">
				<td colspan="5">조회 결과가 없습니다.</td>
		    </c:if>
		    <c:set var="seqno" value="${ total - ((searchVO.pageno - 1) * 10) + 1 }"></c:set>
            <c:forEach var="item" items="${ boardList }">
          		<tr>
					<td>${ seqno = seqno - 1 }</td>
					<td class="textLt paddingL"><a href='/control/view.do?no=${ item.no }'>${ item.title }</a>
					<c:if test="${ item.repCount != 0 }">
						<span style="color:#ff6600">(${ item.repCount })</span>
					</c:if>
					</td>
					<td>${ item.wdate }</td>
					<td>${ item.hit }</td>
					<td>${ item.recCount }</td>
				</tr>
	        </c:forEach>
		</table>
		 <div id="pageNumber">
			<c:if test="${ startbk > 10 }"> 
				<a href="javascript:loadUserBoardList(${startbk - 1})">◀</a>
			</c:if> 	
		 	<c:forEach var="page" begin="${startbk}" end="${endbk}">
		 		<a href="javascript:loadUserBoardList(${ page })">${page}</a>
		 	</c:forEach>
		 	<c:if test="${ endbk < maxpage }"> 
		 		<a href="javascript:loadUserBoardList(${endbk + 1})">▶</a>
		 	</c:if>
		</div>
		<br>
	</div>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="../include/tail.jsp"%>