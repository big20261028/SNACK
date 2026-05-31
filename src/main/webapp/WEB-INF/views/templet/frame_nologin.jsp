<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>프레임(비로그인)</title>
  <style>
   @import url("../css/common.css");
    .loginContent{
      display: none;
    }
    .noLoginContent{
      display: block;
    }
    #noLogin{
      height: 280px;
      line-height: 280px;
      font-size: 2.5em;
    }
  </style>
  <script src="../js/jquery-3.7.1.min.js"></script>
  <script src="../js/main.js"></script>
</head>
<body>
  <header>
    <div id="logo"></div>
    <div id="headerBtnGroup">
      <div class="headerBtn" id="join">회원가입</div>
      <div class="headerBtn" id="login">로그인</div>
    </div>
  </header>
  <div class="contentarea">
    <nav>
      <div id="profile">
        <div class="loginContent" id="proContent01">
          <img src="../image/sample.png" class="profileImg" width="150px">
        </div>
        <div class="loginContent" id="proContent02">admin 님</div>
        <div class="loginContent" id="proContent03">
          <div class="smallBtn follow" id="follow">팔로우 <span>7</span></div>
          <div class="smallBtn follow" id="follower">팔로워 <span>5</span></div>
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