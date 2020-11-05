<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link
	href="https://fonts.googleapis.com/css2?family=Jua&family=Pathway+Gothic+One&family=Roboto+Condensed&display=swap"
	rel="stylesheet">
<%-- <link
	href="${pageContext.request.contextPath}/resource/styles/layout.css"
	rel="stylesheet" type="text/css" media="all"> --%>
<style type="text/css">
.container {
	width: 1080px;
	margin: 0 auto; /* 중앙정렬 */
	font-family: 'Jua', sans-serif;
}

.bbs {
	height: 200px;
	background-color: none;
	margin-bottom: 10px;
	margin-top: 20px;
}

<!--
상단 광고 이미지 슬라이드 style 코드 -->ul, li {
	list-style: none;
}

#slide {
	height: 400px;
	position: relative;
	overflow: hidden;
}

#slide ul {
	width: 400%;
	height: 100%;
	transition: 1s;
	list-style: none;
	padding: 0;
}

#slide ul:after {
	content: "";
	display: block;
	clear: both;
}

#slide li {
	float: left;
	width: 25%;
	height: 100%;
}

#slide li:nth-child(1) {
	background:
		url("${pageContext.request.contextPath}/resource/img/main1.jpg");
	background-size: cover;
}

#slide li:nth-child(2) {
	background:
		url("${pageContext.request.contextPath}/resource/img/main2.jpg");
	background-size: cover;
}

#slide li:nth-child(3) {
	background:
		url("${pageContext.request.contextPath}/resource/img/main1.jpg");
	background-size: cover;
}

#slide li:nth-child(4) {
	background:
		url("${pageContext.request.contextPath}/resource/img/main2.jpg");
	background-size: cover;
}

#slide input {
	display: none;
}

#slide label {
	display: inline-block;
	vertical-align: middle;
	width: 10px;
	height: 10px;
	border: 2px solid #666;
	background: #fff;
	transition: 0.3s;
	border-radius: 50%;
	cursor: pointer;
}

#slide .pos {
	text-align: center;
	position: absolute;
	bottom: 10px;
	left: 0;
	width: 100%;
	text-align: center;
}

#pos1:checked ~ul{
	margin-left: 0%;
}

#pos2:checked ~ul{
	margin-left: -100%;
}

#pos3:checked ~ul{
	margin-left: -200%;
}

#pos4:checked ~ul{
	margin-left: -300%;
}

#pos1:checked ~.pos>label:nth-child(1) {
	background: #666;
}

#pos2:checked ~.pos>label:nth-child(2) {
	background: #666;
}

#pos3:checked ~.pos>label:nth-child(3) {
	background: #666;
}

#pos4:checked ~.pos>label:nth-child(4) {
	background: #666;
}

#main-test {
	padding-top: 100px;
	padding-left: 100px;
	color: white;
	font-size: 20px;
}

.main-button {
	font-family: 'Jua', sans-serif;
	font-size: 14px;
	font-weight: bold;
	line-height: 2;
	border: none;
	border-radius: 10px;
	text-align: center;
	cursor: pointer;
	background: white;
	color: black;
	-webkit-transition-duration: 0.4s;
	transition-duration: 0.4s;
}

.main-button:hover {
	color: #fff;
}

.hover1:hover {
	box-shadow: 200px 0 0 0 rgba(0, 0, 0, 0, 5) inset;
	background-color: skyblue;
	border: none;
}

.menu {
	display: flex;
	margin: 0 auto;
	width: 100%;
}

#main-test {
	font-family: 'Jua', sans-serif;
	font-size: 40px;
}

.menu-item {
	width: 18%;
	height: 150px;
	background: none;
	margin: 1%;
	text-align: center;
	font-family: 'Jua', sans-serif;
}

.menu-items {
	display: flex;
	margin: 0 auto;
	width: 100px;
	height: 100px;
}
#bbs-img{
	width:100%;
	display: flex;
	margin: 0 auto;
	text-align: center;
}
.main-frame{
	width: 30%;
	height:250px;
	margin:2.5%;
	background: none;
}
.img-box{
	width: 90%;
	height : 80%;
	margin: 5%;
	border-radius: 20px;
	border: 2px solid silver;
}
</style>
</head>
<body>
	<div class="container">
		<jsp:include page="/WEB-INF/views/layout/header.jsp" />

		<div class="main">

			<div id="slide">
				<input type="radio" name="pos" id="pos1" checked> <input
					type="radio" name="pos" id="pos2"> <input type="radio"
					name="pos" id="pos3"> <input type="radio" name="pos"
					id="pos4">
				<ul>
					<li>
						<p id="main-test">
							안녕하세요 <br>뺀질코딩입니다.<br>
							<button class="main-button hover1" onclick="location.href='${pageContext.request.contextPath}/intro/intro.jsp'">더 알아보기</button>
						</p>
					</li>
					<li></li>
					<li></li>
					<li></li>
				</ul>
				<p class="pos">
					<label for="pos1"></label> <label for="pos2"></label> <label
						for="pos3"></label> <label for="pos4"></label>
				</p>
			</div>

		</div>

		<div class="bbs">
			<div class="menu">
				<div class="menu-item">
					<img class="menu-items"
						src="${pageContext.request.contextPath}/resource/img/idea.png"
						onclick="">
					<p>커뮤니티</p>
				</div>
				<div class="menu-item">
					<img class="menu-items"
						src="${pageContext.request.contextPath}/resource/img/coding.png">
					<p>코드소스</p>
				</div>
				<div class="menu-item">

					<img class="menu-items"
						src="${pageContext.request.contextPath}/resource/img/book-stack.png">
					<p>지식인</p>
				</div>
				<div class="menu-item">
					<img class="menu-items"
						src="${pageContext.request.contextPath}/resource/img/404-error.png">
					<p>에러떠요!</p>
				</div>
				<div class="menu-item">
					<img class="menu-items"
						src="${pageContext.request.contextPath}/resource/img/noticeboard.png">
					<p>공지게시판</p>
				</div>
			</div>
		</div>
		<p style="font-size: 20px;">뺀질코더의 스토리</p>
		<div class="bbs" id="bbs-img">
		
		<div class="main-frame">
			<div class="img-box" style="background: url('${pageContext.request.contextPath}/resource/img/logo3.png');background-position: center;background-size:cover;">
			</div>
			
			<p>글 제목</p>
		</div>
		
		<div class="main-frame">
			<div class="img-box" style="background: url('${pageContext.request.contextPath}/resource/img/logo3.png');background-position: center;background-size:cover;">
			</div>
			<p>글 제목</p>
		</div>
		
		<div class="main-frame">
			<div class="img-box" style="background: url('${pageContext.request.contextPath}/resource/img/logo3.png');background-position: center;background-size:cover;">
			</div>
			<p>글 제목</p>
		</div>
		
		</div>

<br><br><br><br><br><br>
		<jsp:include page="/WEB-INF/views/layout/footer.jsp" />
	</div>




</body>
</html>