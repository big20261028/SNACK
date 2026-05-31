<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>프레임(예시)</title>
  <style>
   @import url("../css/common.css");

  </style>
  <script src="../js/jquery-3.7.1.min.js"></script>
  <script src="../js/main.js"></script>
</head>
<body>
  <header>
    <div id="logo"></div>
    <div id="headerBtnGroup">
      <div class="headerBtn" id="logout">로그아웃</div>
    </div>
  </header>
  <div class="contentarea">
    <nav>
      <div id="profile">
        <div class="loginContent" id="proContent01">
          <img src="../image/sample.png" class="profileImg" width="150px">
        </div>
        <div class="loginContent" id="proContent02">hong 님</div>
        <div class="loginContent" id="proContent03">
          <div class="smallBtn follow" id="follow">팔로우 <span>37</span></div>
          <div class="smallBtn follow" id="follower">팔로워 <span>25</span></div>
        </div>
        <div class="noLoginContent" id="noLogin">
          비회원
        </div>
      </div>
      <div class="menuBtn" id="menuBtn01">
        전체 글
      </div>
      <div class="menuBtn loginContent" id="menuBtn02">
        팔로우 작가 글
      </div>
      <div class="menuBtn loginContent" id="menuBtn03">
        마이페이지
      </div>
      <div class="menuBtn loginContent" id="menuBtn04">
        설정
      </div>
      <div class="menuBtn adminMenu" id="menuBtn05">
        회원관리
      </div>
      <br><br>
    </nav>
    <section>
      <!-- 이곳에 컨텐츠 입력 -->


      <!-- 이곳에 컨텐츠 입력 -->
    </section>
  </div>
</body>
</html>