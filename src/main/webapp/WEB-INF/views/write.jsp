<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="javaDate" class="java.util.Date" />
<script>
	$(function(){
		if(${ sessionScope.login == null }) 
		{
			document.location = "/control/list.do";
			return;
		}
		
		
		$("#writeOkBtn").click(function(){
			DoSubmit();
		})
		$("#cancelBtn").click(function(){
			if(confirm("글쓰기를 취소하시겠습니까?"))	
				document.location = "/control/list.do";
		})
		
		function DoSubmit()
		{
			if($("#title").val() == "")
			{
				alert("제목을 입력하세요.");
				$("#title").focus();
				return;
			}
			if($("#note").val() == "")
			{
				alert("내용을 입력하세요.");
				$("#note").focus();
				return;
			}
			if(confirm("게시물을 등록하시겠습니까?"))
			{
				$("#writeForm").submit();
			}
		}
	})
</script>
    <section>
      <!-- 이곳에 컨텐츠 입력 -->
      <div class="viewBoxOuter">
        <div class="viewBoxInner">
        	<form name="writeForm" id="writeForm" method="post" action="write.do" enctype="multipart/form-data">
        	  <input type="hidden" name="usernum" value="${ sessionScope.login.usernum }">
	          <table border="1" class="viewTb">
	            <tr>
	              <td width="100px">제목</td>
	              <td>
	                <input type="text" class="input inputM" name="title" id="title" placeholder="제목을 입력해주세요.">
	              </td>
	              <td width="80px">${ sessionScope.login.userid }</td>
	              <td width="100px">
	              	<fmt:formatDate value="${javaDate}" pattern="yyyy-MM-dd" var="now" />
					<c:out value="${now}" />
	              </td>
	            </tr>
	            <tr>
	              <td width="80px">내용</td>
	              <td colspan="3">
	                <textarea name="note" id="note" class="note" placeholder="내용을 입력해주세요."></textarea>
	              </td>
	            </tr>
	            <tr>
	              <td width="80px">첨부파일</td>
	              <td colspan="3" class="textLt">
	                <input type="file" name="attach" id="attach">
	              </td>
	            </tr>
	          </table>
          </form>
          <div class="bigBtnBox marginT">
            <div class="bigBtn" id="writeOkBtn">등록</div>
            <div class="bigBtn" id="cancelBtn">취소</div>
          </div>
        </div>
      </div>
      <!-- 이곳에 컨텐츠 입력 -->
    </section>
<%@ include file="./include/tail.jsp" %>