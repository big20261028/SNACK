1. Spring Legacy Project 로 BoardEx07 프로젝트 생성

2. web.xml에 한글 필터를 설정

3. pom.xml에 라이브러리 설정
   (1) mybatis 라이브러리 설정
   (2) 첨부파일 업다운 라이브러리 설정
   
4. jsp 파일 
   (1) include 처리
   (2) *.jsp를 *.do로 링크 변경

5. Database 테이블 설계 및 Create SQL 작성
   
6. database 연결 설정
   root-context.xml 에서
   (1) namespace 설정
   (2) beans 설정 
     dataSource / sqlSessionFactory / sqlSession

7. MyBatis Mapper 작성
   /src/main/resource/mapper 폴더 생성 후
   mapper.xml 파일 제작
   
8. com.ezen.vo 패키지 작성 후 
   boardVO, replyVO, userVO 클래스 제작

9. com.ezen.control 패키지에
   BoardController 클래스 작성 후
     목록(list.do), 회원가입(join.do) .. 등의 
   @RequestMapping 작성
   
10. com.ezen.dto 패키지 작성 후
   boardDTO, replyDTO, userDTO 클래스 작성
   
11. jsp 파일에서 프론트엔드 UI 작성
   자료입력 검증, jQuery 구문 등...
      
12. BoardController 클래스내에서 DTO 클래스 호출하여
   프로그램 구현
   






   
   