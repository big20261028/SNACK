<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	$(function(){
		if(${ sessionScope.login == null || sessionScope.login.isadmin != 'Y' })
			document.location = "/control/list.do";
		
		$("#userStatusFilter0" + ${ searchVO.userStatusFilter }).attr("selected","selected");
		$("#userSearchFilter0" + ${ searchVO.userSearchFilter }).attr("selected","selected");
		
		$("#menuBtn05").addClass("choosenBtn");
		
		$("#userStatusFilter").change(function(){
			$("#userListFrm").submit();
		})
		
		$("#searchBtn").click(function(){
			$("#userListFrm").submit();
		})
		console.log(${startbk});
		console.log(${endbk});
		
		
		$("#userDelete").click(function(){
			if(!isChecked()) return;
			if(!confirm("선택한 회원을 탈퇴 처리하시겠습니까?")) return;
			$("#status").attr("value","N");
			$("#userStatusFrm").submit();
		})
		$("#userBan").click(function(){
			if(!isChecked()) return; 
			if(!confirm("선택한 회원을 계정정지 처리하시겠습니까?")) return;
			$("#status").attr("value","B");
			$("#userStatusFrm").submit();
		})
		$("#userRestore").click(function(){
			if(!isChecked()) return;
			if(!confirm("선택한 회원을 복구 처리하시겠습니까?")) return;
			$("#status").attr("value","Y");
			$("#userStatusFrm").submit();
		})
	})
	
	function userData(usernum)
	{
		document.location = "/control/userdata.do?usernum=" + usernum;
	}
	
	function isChecked()
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
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<h2 class="subTitle">회원관리</h2>
	<form action="userlist.do" method="post" name="userListFrm" id="userListFrm">
		<select class="mainFilter" name="userStatusFilter" id="userStatusFilter">
			<option id="userStatusFilter00" value="0">[상태]</option>
			<option id="userStatusFilter01" value="1">정상</option>
			<option id="userStatusFilter02" value="2">탈퇴</option>
			<option id="userStatusFilter03" value="3">정지</option>
		</select>
		<div class="searchBox">
			<select class="searchFilter" name="userSearchFilter" id="userSearchFilter">
				<option id="userSearchFilter01" value="1">아이디</option>
				<option id="userSearchFilter02" value="2">이름</option>
				<option id="userSearchFilter03" value="3">이메일</option>
			</select> <input type="text" name="keyword" class="searchInput" size="60px"
				placeholder="검색어를 입력해주세요." value="${ searchVO.keyword }">
			<div class="searchBtn" id="searchBtn">검색</div>
		</div>
	</form>
	<div class="box">
		<div class="textRt">
			<div class="Btn" id="userDelete">회원탈퇴</div>
			<div class="Btn" id="userBan">회원정지</div>
			<div class="Btn" id="userRestore">계정복구</div>
		</div>
	</div>
	<div class="box">
		<form action="userStatus.do" method="post" name="userStatusFrm" id="userStatusFrm">
			<input type="hidden" name="status" id="status">
			<table border="1" class="fullTbSm">
				<tr>
					<th width="40px"></th>
					<th>회원번호</th>
					<th>아이디</th>
					<th>이름</th>
					<th width="40px">성별</th>
					<th>이메일</th>
					<th width="40px">상태</th>
					<th width="80px">링크</th>
				</tr>
				<c:if test="${ total == 0 }">
					<td colspan="8">조회 결과가 없습니다.</td>
				</c:if>
				<c:forEach var="item" items="${ userList }">
					<tr>
						<td><input type="checkbox" class="checkBoxClass" name="usernum" value="${ item.usernum }"></td>
						<td>${ item.usernum }</td>
						<td>${ item.userid }</td>
						<td>${ item.name }</td>
						<td>${ item.gender }</td>
						<td>${ item.email }</td>
						
						<c:if test="${ item.status == 'Y' }">
							<td class="status">정상</td>
						</c:if>
						<c:if test="${ item.status == 'N' }">
							<td class="status" style="color: blue">탈퇴</td>
						</c:if>
						<c:if test="${ item.status == 'B' }">
							<td class="status" style="color: red">정지</td>
						</c:if>
						<td class="tdBtn" id="userDataBtn" onclick="userData(${ item.usernum })">회원정보</td>
					</tr>
				</c:forEach>
			</table>
		</form>
		<div id="pageNumber">
			<c:if test="${ startbk > 10 }"> 
				<a href="userlist.do?pageno=${startbk - 1}&userStatusFilter=${ searchVO.userStatusFilter }&userSearchFilter=${ searchVO.userSearchFilter }&keyword=${ searchVO.keyword }">◀</a>
			</c:if> 
		 	<c:forEach var="page" begin="${startbk}" end="${endbk}">
		 		<a href="userlist.do?pageno=${page}&userStatusFilter=${ searchVO.userStatusFilter }&userSearchFilter=${ searchVO.userSearchFilter }&keyword=${ searchVO.keyword }">${page}</a>
		 	</c:forEach>
		 	<c:if test="${ endbk < maxpage }"> 
		 		<a href="userlist.do?pageno=${endbk + 1}&luserStatusFilter=${ searchVO.userStatusFilter }&userSearchFilter=${ searchVO.userSearchFilter }&keyword=${ searchVO.keyword }">▶</a>
		 	</c:if>
		</div><br>
	</div>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>