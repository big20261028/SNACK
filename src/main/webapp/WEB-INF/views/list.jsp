<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- 받아올것 : total maxpage startbk endbk boardList searchVO -->
<script>
	$(function(){
		$("#menuBtn01").addClass("choosenBtn");
		
		$("#listFilter0" + ${ searchVO.listFilter }).attr("selected","selected");
		
		if(${ searchVO.searchFilter != 0 })
			$("#searchFilter0" + ${ searchVO.searchFilter }).attr("selected","selected");
		
		//console.log(${ searchVO.listFilter });
		$(".mainFilter").change(function(){
			$("#list").submit();
		})
		
		$("#searchBtn").click(function(){
			$("#list").submit();
		})
		$("#writeBtn").click(function(){
			document.location = "/control/write.do";
		})
		
		if(${ sessionScope.login == null }) 
			$("#writeBtn").addClass("hidden");
		
	})
</script>
<section>
	<h2 class="subTitle">전체 글</h2>
	
	<div class="box">
		<table class="fullTb" border="1">
			<tr>
				<th width="50px">번호</th>
				<th>제목</th>
				<th width="100px">작성일자</th>
				<th width="80px">작성자</th>
				<th width="50px">조회수</th>
				<th width="50px">추천수</th>
			</tr>
			<c:if test="${ total == 0 }">
				<td colspan="6">조회 결과가 없습니다.</td>
			</c:if>
			<c:set var="seqno" value="${ total - ((searchVO.pageno - 1) * 10) + 1 }"></c:set>
			<c:forEach var="item" items="${ boardList }">
				<tr>
					<td>${ seqno = seqno - 1 }</td>
					<td class="textLt paddingL"><a href='view.do?no=${ item.no }'>${ item.title }</a>
					<c:if test="${ item.repCount != 0 }">
						<span style="color:#ff6600">(${ item.repCount })</span>
					</c:if>
					</td>
					<td>${ item.wdate }</td>
					<td>${ item.userid }</td>
					<td>${ item.hit }</td>
					<td>${ item.recCount }</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	<form id="list" name="list" method="post" action="list.do">
		<select class="mainFilter" name="listFilter" id="listFilter">
			<option id="listFilter01" value="1">최신순</option>
			<option id="listFilter02" value="2">오래된순</option>
			<option id="listFilter03" value="3">추천순</option>
			<option id="listFilter04" value="4">조회순</option>
		</select>
		<div class="searchBox">
			<select class="searchFilter" name="searchFilter" id="searchFilter">
				<option id="searchFilter01" value="1">제목</option>
				<option id="searchFilter02" value="2">작성자</option>
				<option id="searchFilter03" value="3">내용</option>
				<option id="searchFilter04" value="4">댓글</option>
			</select> 
			<input type="text" name="keyword" class="searchInput" size="50px"
				placeholder="검색어를 입력해주세요." value="${ searchVO.keyword }">
			<div class="searchBtn" id="searchBtn">검색</div>
			<div class="searchBtn" id="writeBtn">글쓰기</div>
		</div>
	</form>
	<div id="pageNumber">
		<c:if test="${ startbk > 10 }"> 
			<a href="list.do?pageno=${startbk - 1}&listFilter=${ searchVO.listFilter }&searchFilter=${ searchVO.searchFilter }&keyword=${ searchVO.keyword }">◀</a>
		</c:if> 
	 	<c:forEach var="page" begin="${startbk}" end="${endbk}">
	 		<a href="list.do?pageno=${page}&listFilter=${ searchVO.listFilter }&searchFilter=${ searchVO.searchFilter }&keyword=${ searchVO.keyword }">${page}</a>
	 	</c:forEach>
	 	<c:if test="${ endbk < maxpage }"> 
	 		<a href="list.do?pageno=${endbk + 1}&listFilter=${ searchVO.listFilter }&searchFilter=${ searchVO.searchFilter }&keyword=${ searchVO.keyword }">▶</a>
	 	</c:if>
	</div><br>
</section>
<%@ include file="./include/tail.jsp" %>