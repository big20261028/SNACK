<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../include/head.jsp" %>
<script>
	$(function(){
	    // 페이지 로딩 완료 후 실행
	    $(".followSub").each(function(){
	        var tonum = $(this).attr("id").replace("followSub", "");
	        isSubscribe(tonum, $(this)); // isSubscribe 함수에 jQuery 객체 전달
	    });
	});

	function isSubscribe(tonum, $button) // jQuery 객체를 파라미터로 받음
	{
	    $.ajax({
	        url : "isSubscribe.do",
	        type : "post",
	        data : {
	            fromnum : "${sessionScope.login.usernum}",
	            tonum : tonum
	        },
	        success : function(data){
	            console.log(data);
	            data = data.trim();
	            if(data == "Y")
	            {
	                $button.addClass("choosenBtn");
	                $button.html("구독중");
	            }
	            if(data == "N")
	            {
	                $button.html("구독하기");
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("팔로우 상태 확인 실패:", error);
	        }
	    });
	}

	function subscribe(fromnum, tonum)
	{
	    $.ajax({
	        type : "post",
	        url  : "/control/subscribe.do",
	        data :{
	            fromnum : fromnum,
	            tonum  : tonum
	        },
	        success : function(data){
	            console.log(data);
	            data = data.trim();
	            ary = data.split(":");
	            if(ary[0] == "O") alert("구독하셨습니다.");
	            if(ary[0] == "X") alert("구독을 취소하셨습니다.");
	            // 팔로우/언팔로우 후 버튼 텍스트 및 클래스 업데이트 (선택 사항)
	            var $button = $("#followSub" + tonum);
	            if (ary[0] == "O") {
	                $button.addClass("choosenBtn");
	                $button.html("구독중");
	            } else if (ary[0] == "X") {
	                $button.removeClass("choosenBtn");
	                $button.html("구독하기");
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("구독/취소 요청 실패:", error);
	        }
	    });
	}
	function followList(page) {
		console.log("followlist 호출됨");
	    $.ajax({
	        url: "followList.do", // 현재 요청을 보낼 URL (컨트롤러의 /UserPage/follow.do 매핑)
	        type: "post", // 또는 "POST" - 컨트롤러 매핑에 따라 결정
	        headers: {
		        "Accept": "application/json"
		    },
	        data: {
	            usernum: "${userVO.usernum}", // 현재 페이지의 사용자 번호
	            pageno: page // 클릭한 페이지 번호
	        },
	        success: function(data) {
	           	console.log(data);
	           	var html = "";
	           
	           	if (data.list == null || data.list.length === 0) {
	                html += '<div class="marginB marginT">조회 결과가 없습니다.</div>';
	            } else {
	                var seqno = data.total - ((data.searchVO.pageno - 1) * 10);
	                $.each(data.list, function(index, follow) {
	                	html += '<div class="marginB marginT">';
	                    html += '<div class="positionR">';
	                    html += '<div class="followBox">';
	                   	if(follow.profileimgF == null || follow.profileF == "")
                   		{
                   			html += '<img src="/control/resources/image/sample.png" width="60px" class="profileImg">';
                   		}else
             			{
                   			html += '<img src="/control/profileImgByName.do?profileimgp=' + follow.profileimgp + '&profileimgf=' + follow.profileimgp + '" width="60px" class="profileImg">';
             			}
	                    html += '</div>';
	                    html += '<div class="followBox" id="followId">';
	                    html += '<a href="/control/UserPage/userpage.do?usernum=' + follow.tonum + '">' + follow.followid + '</a>';
	                    html += '</div>';
	                    if(${ sessionScope.login != null } && follow.tonum != "${ sessionScope.login.usernum }" )
                    	{
                    		html += '<div class="followBox whiteBtn followSub" id="followSub' + follow.tonum + '" onclick="subscribe(${ sessionScope.login.usernum },' + follow.tonum + ');"></div>';
                    	}
	                    html += '</div>';
	                    html += '<div class="followIntroBox">' + follow.intro + '</div>';
	                    html += '</div>';
	                });
	                
	                	html += '<div id="pageNumber">';
	                
	                if (data.startbk > 10) {
	                	html += '<a href="javascript:followList(' + (data.startbk - 1) + ')">◀</a>';
		            }
		            for (var i = data.startbk; i <= data.endbk; i++) {
		            	html += '<a href="javascript:followList(' + i + ')">' + i + '</a> ';
		            }
		            if (data.endbk < data.maxpage) {
		            	html += '<a href="javascript:followList(' + (data.endbk + 1) + ')">▶</a>';
		            }
		            	html += '</div>';
		            
		            $("#followContentBox").html(html);
		            
		            $(".followSub").each(function(){
		                var tonum = $(this).attr("id").replace("followSub", "");
		                isSubscribe(tonum, $(this));
		            });
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("팔로우 목록 갱신 실패:", error);
	        }
	    });
	}
</script>
    <section>
      <!-- 이곳에 컨텐츠 입력 -->
      
      <h2 class="subTitle">
	      <c:if test="${ sessionScope.login != null && sessionScope.login.usernum == userVO.usernum }">
	      	내가 구독하는 ${ total } 명
	      </c:if> 
	      <c:if test="${ sessionScope.login == null || sessionScope.login.usernum != userVO.usernum }">
	    	${ userVO.userid } (이)가 구독하는 ${ total } 명  
	      </c:if>
      </h2>
      <div class="box" id="followContentBox">
      	<c:set var="seqno" value="${ total - ((searchVO.pageno - 1) * 10) + 1 }"></c:set>
      	<c:forEach var="item" items="${ followList }">
   			<div class="marginB marginT">
	            <div class="positionR">
		            <div class="followBox">
		            	<c:if test="${ item.profileimgf == null || item.profileimgf == ''  }">
							<img src="/control/resources/image/sample.png" width="60px" class="profileImg">
						</c:if>
						<c:if test="${ item.profileimgf != null && item.profileimgf != '' }">
							<img src="/control/profileImgByName.do?profileimgp=${ item.profileimgp }&profileimgf=${ item.profileimgf }" width="60px" class="profileImg">
						</c:if>
					</div>
		            <div class="followBox" id="followId"><a href="/control/UserPage/userpage.do?usernum=${ item.tonum }">${ item.followid }</a></div>
		            <c:if test="${ sessionScope.login != null }">
		            	<c:if test="${ sessionScope.login.usernum != item.tonum }">
			            	<div class="followBox whiteBtn followSub" id="followSub${ item.tonum }"
			            	onclick="subscribe(${ sessionScope.login.usernum },${ item.tonum });"></div>
		            	</c:if>
		            </c:if>
	        	</div>
		        <div class="followIntroBox">
	            	${ item.intro }
	            </div>
        	</div>
      	</c:forEach>
        
        <div id="pageNumber">
			<c:if test="${ startbk > 10 }">
				<a href="javascript:followList(${startbk - 1})">◀</a>
			</c:if>
			<c:forEach var="page" begin="${startbk}" end="${endbk}">
				<a href="javascript:followList(${page});">${page}</a>
			</c:forEach>
			<c:if test="${ endbk < maxpage }">
				<a href="javascript:followList(${endbk + 1});">▶</a>
			</c:if>
		</div>
      </div>
      <!-- 이곳에 컨텐츠 입력 -->
    </section>
<%@ include file="../include/tail.jsp" %>