<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://fonts.googleapis.com/css2?family=Jua&family=Pathway+Gothic+One&family=Roboto+Condensed&display=swap" rel="stylesheet">
<style type="text/css">
.container {
	width: 1080px;
	margin: 0 auto;/* 중앙정렬 */
	
}

.header{
	height: 100px;
	background-color: none;
	display:flex;
	margin: 0 auto;
	border-bottom: 3px solid silver;
	margin-bottom: 15px;
	position: relative;
	z-index: 10;
}
.header-box1 {
	width: 33%;
	margin: 0 auto;
}
.header-box2{
	width: 33%;
	margin: 0 auto;
	text-align: center;

}
.header-box3{
	width: 33%;
	margin: 0 auto;
	line-height: 100px;
	text-align: right;
	font-family: 'Jua', sans-serif;
}
.nav {
	height: 30px;
	display: flex;
	margin: 0 auto;/* 중앙정렬 */
	border-bottom: 3px solid silver;
	padding-bottom: 13px;
	margin-bottom: 10px;
	
}
.nav-item-dropbtn1{
	cursor: pointer;
	border: 0;
	outline: 0;
	background: none;
	font-family: 'Jua', sans-serif;
	font-size: 18px;
}
.nav-item-drop1 {
    position: relative;
    display: inline-block;
    margin: 0 auto;
}
.nav-item-drop-content{
    display: none;
    position: absolute;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
	font-family: 'Jua', sans-serif;
	background-color: white;
}
.nav-item-drop-content a {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
}
.nav-item-drop-content a:hover {
    background-color: #f1f1f1
}
.nav-item-drop1:hover .nav-item-drop-content {
    display: block;
}
.nav-item-drop1:hover .nav-item-dropbtn1 {
    background-color: white;
    color: skyblue;
}

img{
	width: 173px;
	height: 100px;
}
</style>
</head>
<body>
<div class="container">
	<div class="header">
			<div class="header-box1" ></div>
			<div class="header-box2" >
				<img alt="로고이미지" src="${pageContext.request.contextPath}/resource/img/logo3.png" >
			</div>
			<div class="header-box3" > 로그인 | 회원가입</div>
	</div>
	
	
	<div class="nav">
	<div class="nav-item-drop1">
		<button class="nav-item-dropbtn1">자유게시판</button>
		<div class="nav-item-drop-content">
			<a href="#">자유게시판</a>
			<a href="#">추천게시판</a>
			<a href="#">사진게시판</a>
		</div>
	</div>
	
		<div class="nav-item-drop1">
		<button class="nav-item-dropbtn1">소스코드 게시판</button>
		<div class="nav-item-drop-content">
			<a href="#">C언어</a>
			<a href="#">JAVA</a>
			<a href="#">자바스크립트</a>
			<a href="#">C++</a>
		</div>
	</div>
		<div class="nav-item-drop1">
		<button class="nav-item-dropbtn1">지식인 게시판</button>
		<div class="nav-item-drop-content">
			<a href="#">책추천</a>
			<a href="#">강의 공유 게시판</a>
		</div>
	</div>
		<div class="nav-item-drop1">
		<button class="nav-item-dropbtn1">에러떠요</button>
		<div class="nav-item-drop-content">
			<a href="#">404/500에러</a>
			<a href="#">NULLPOINT</a>
			<a href="#">EXCEPTION</a>
		</div>
	</div>
		<div class="nav-item-drop1">
		<button class="nav-item-dropbtn1">공지사항</button>
		<div class="nav-item-drop-content">
			<a href="#">공지사항</a>
			<a href="#">채용공고</a>
			<a href="#">출석체크</a>
		</div>
	</div>

	</div>
</div>




</body>
</html>