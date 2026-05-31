create database ateam;
use ateam;
create table user
(
	usernum int primary key auto_increment comment '회원번호',
	userid varchar(50) not null unique comment '아이디',
	email varchar(100) not null unique comment '이메일',
	userpw varchar(100) comment '비밀번호',
	name varchar(50) comment '이름',
	gender varchar(1) comment '성별',
	status varchar(1) default 'Y' comment '상태',
	joindate datetime default now() comment '가입일자',
	isadmin varchar(1) default 'N' comment '관리자권한',
	intro varchar(100) default '기본 자기소개입니다.' comment '자기소개',
	profileimgp varchar(200) comment '프로필이미지물리명',
	profileimgf varchar(200) comment '프로필이미지논리명'
) comment '회원정보';

create table board
(
	no int primary key auto_increment comment '게시물번호',
	usernum int comment '회원번호',
	title varchar(200) not null comment '제목',
	note text not null comment '내용',
	pname varchar(200) comment '물리파일명',
	fname varchar(200) comment '논리파일명',
	wdate datetime default now() comment '작성일자',
	hit int default 0 comment '조회수',
	foreign key (usernum) references user(usernum)
) comment '게시물정보';

create table recommend
(
	recno int primary key auto_increment comment '추천번호',
	usernum int comment '회원번호',
	no int comment '게시물번호',
	foreign key (usernum) references user(usernum),
	foreign key (no) references board(no)
) comment '추천정보';
alter table recommend add unique (usernum, no);

create table reply
(
	rno int primary key auto_increment comment '댓글번호',
	usernum int comment '회원번호',
	no int comment '게시물번호',
	rnote text not null comment '댓글내용',
	rwdate datetime default now() comment '댓글작성일자',
	foreign key (usernum) references user(usernum),
	foreign key (no) references board(no)
) comment '댓글정보';

create table guest
(
	gno int primary key auto_increment comment '방명록번호',
	hostnum int comment '소유자번호',
	guestnum int comment '방문자번호',
	gnote text not null comment '방명록내용',
	gwdate datetime default now() comment '방명록작성일자',
	foreign key (hostnum) references user(usernum),
	foreign key (guestnum) references user(usernum)
) comment '방명록정보';

create table follow
(
	fno int primary key auto_increment comment '팔로우번호',
	fromnum int comment '구독자번호',
	tonum int comment '피구독자번호',
	fwdate datetime default now() comment '팔로우일자',
	foreign key (fromnum) references user(usernum),
	foreign key (tonum) references user(usernum)
) comment '팔로우정보';
alter table follow add unique (fromnum, tonum);


insert into user (userid,email,userpw,name,gender,isadmin) 
values ('admin', 'dlsrl200@naver.com', 'ezen','관리자','Y');


insert into follow (fromnum, tonum) values (1, 3);
