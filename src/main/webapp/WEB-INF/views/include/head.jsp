<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>전체 글</title>
	<link rel="stylesheet" href="/control/resources/css/common.css" />
	<script src="/control/resources/js/jquery-3.7.1.min.js"></script>
	<script src="/control/resources/js/main.js"></script>
</head>
<body>
	<script>
		$(function(){
			$("#menuBtn03").click(function(){
			    document.location = "/control/UserPage/userpage.do?usernum=${sessionScope.login.usernum}";
			});	
		    $("#follow").click(function(){
			    document.location = "/control/UserPage/follow.do?usernum=${sessionScope.login.usernum}"
			});
		    $("#follower").click(function(){
		      	document.location = "/control/UserPage/follower.do?usernum=${sessionScope.login.usernum}"
	    	})
	})
	</script>
	<header>
		<div id="logo"></div>
		<div id="headerBtnGroup">
			<c:if test="${ sessionScope.login == null }">
				<div class="headerBtn" id="join">회원가입</div>
				<div class="headerBtn" id="login">로그인</div>
			</c:if>
			<c:if test="${ sessionScope.login != null }">
				<div class="headerBtn" id="logout">로그아웃</div>
			</c:if>
		</div>
	</header>
	<div class="contentarea">
		<nav>
			<div id="profile">
				<c:if test="${ sessionScope.login == null }">
					<div class="noLogin">비회원</div>
				</c:if>
				<c:if test="${ sessionScope.login != null }">
					<div class="" id="proContent01">
						<c:if test="${ sessionScope.login.profileimgf == null || sessionScope.login.profileimgf == ''  }">
							<img src="/control/resources/image/sample.png" width="150px" class="profileImg">
						</c:if>
						<c:if test="${ sessionScope.login.profileimgf != null && sessionScope.login.profileimgf != ''  }">
							<img src="/control/profileImgByName.do?profileimgp=${ sessionScope.login.profileimgp }&profileimgf=${ sessionScope.login.profileimgf }" width="150px" class="profileImg">
						</c:if>
					</div>
					<div class="" id="proContent02">${ sessionScope.login.userid } 님</div>
					<div class="" id="proContent03">
						<div class="Btn" id="follow">
							팔로우 <span class="lightcoral">${ sessionScope.login.followCnt }</span>
						</div>
						<div class="Btn" id="follower">
							팔로워 <span class="lightcoral">${ sessionScope.login.followerCnt }</span>
						</div>
					</div>
				</c:if>
			</div>
			<div class="menuBtn" id="menuBtn01">전체 글</div>
			<c:if test="${ sessionScope.login != null }">
				<div class="menuBtn" id="menuBtn02">팔로우 작가 글</div>
				<div class="menuBtn" id="menuBtn03">마이페이지</div>
				<div class="menuBtn" id="menuBtn04">설정</div>
				<c:if test="${ sessionScope.login.isadmin eq 'Y' }">
					<div class="menuBtn" id="menuBtn05">회원관리</div><!-- 관리자 권한 필요 -->
				</c:if>
			</c:if>
			<br>
			<br>
		</nav>