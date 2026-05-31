<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="./include/head.jsp"%>
<script>
	$(function(){
		if( ${ sessionScope.login.isadmin == "Y"})
		{
			if( ${ sessionScope.login == null && boardVO.usernum == sessionScope.login.usernum } )
			{
				document.location ="/control/list.do"
			}
		}
			
				
		
		$("#modifyOkBtn").click(function(){
			if(confirm("수정 하시겠습니까?"))
			{
				$("#modifyFrm").submit();
			}
				
		})
		$("#cancelBtn").click(function(){
			if(confirm("수정을 취소하시겠습니까?"))	
				document.location = "/control/view.do?no=${ boardVO.no }";
		})
	})
</script>
<section>
	<!-- 이곳에 컨텐츠 입력 -->
	<div class="viewBoxOuter">
		<div class="viewBoxInner">
			<form name="modifyFrm" id="modifyFrm" method="post" action="modifyOk.do" enctype="multipart/form-data">
				<input type="hidden" name="no" id="no" value="${ boardVO.no }">
				<table border="1" class="viewTb">
					<tr>
						<td width="100px">제목</td>
						<td><input type="text" class="input inputM" name="title" id="title"
							placeholder="제목을 입력해주세요." value="${ boardVO.title }"></td>
						<td width="80px">${ boardVO.userid }</td>
						<td width="100px">${ boardVO.wdate }</td>
					</tr>
					<tr>
						<td width="80px">내용</td>
						<td colspan="3"><textarea name="note" id="note" class="note"
								placeholder="내용을 입력해주세요.">${ boardVO.note }</textarea></td>
					</tr>
					<tr>
						<td width="80px" rowspan="2">첨부파일</td>
						<td colspan="3" class="textLt">
						<c:if test="${ boardVO.fname == null || boardVO.fname == ''  }">
							등록된 첨부파일 없음
						</c:if> 
						<c:if test="${ boardVO.fname != null && boardVO.fname != ''  }">
							<a href="down.do?no=${ boardVO.no }">${ boardVO.fname }</a>
						</c:if>
						</td>
					</tr>
					<tr>
						<td colspan="3" class="textLt">
							<input type="file" name="attach" id="attach">
						</td>
					</tr>
				</table>
			</form>
			<div class="bigBtnBox marginT">
				<div class="bigBtn" id="modifyOkBtn">수정완료</div>
				<div class="bigBtn" id="cancelBtn">취소</div>
			</div>
		</div>
	</div>
	<!-- 이곳에 컨텐츠 입력 -->
</section>
<%@ include file="./include/tail.jsp"%>