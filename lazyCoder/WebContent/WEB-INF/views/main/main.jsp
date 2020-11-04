<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<%-- <link
	href="${pageContext.request.contextPath}/resource/styles/layout.css"
	rel="stylesheet" type="text/css" media="all"> --%>
<style type="text/css">
.container {
	width: 1080px;
	margin: 0 auto;/* 중앙정렬 */
	
}
.bbs{
height: 200px;
background-color: silver;
margin-bottom: 10px;
margin-top: 20px;

}



<!-- 상단 광고 이미지 슬라이드 style 코드 -->
ul,li{list-style:none;}
#slide{height:400px;position:relative;overflow:hidden;}
#slide ul{width:400%;height:100%;transition:1s; list-style: none; padding: 0;}
#slide ul:after{content:"";display:block;clear:both;}
#slide li{float:left;width:25%;height:100%;}
#slide li:nth-child(1){
	background:url("${pageContext.request.contextPath}/resource/img/main1.jpg");
	background-size: cover;
	}
#slide li:nth-child(2){		
	background:url("${pageContext.request.contextPath}/resource/img/main2.jpg");
	background-size: cover;
	}
#slide li:nth-child(3){		
	background:url("${pageContext.request.contextPath}/resource/img/main1.jpg");
	background-size: cover;
	}
#slide li:nth-child(4){		
	background:url("${pageContext.request.contextPath}/resource/img/main2.jpg");
	background-size: cover;
	}
#slide input{display:none;}
#slide label{display:inline-block;vertical-align:middle;width:10px;height:10px;border:2px solid #666;background:#fff;transition:0.3s;border-radius:50%;cursor:pointer;}
#slide .pos{text-align:center;position:absolute;bottom:10px;left:0;width:100%;text-align:center;}
#pos1:checked~ul{margin-left:0%;}
#pos2:checked~ul{margin-left:-100%;}
#pos3:checked~ul{margin-left:-200%;}
#pos4:checked~ul{margin-left:-300%;}
#pos1:checked~.pos>label:nth-child(1){background:#666;}
#pos2:checked~.pos>label:nth-child(2){background:#666;}
#pos3:checked~.pos>label:nth-child(3){background:#666;}
#pos4:checked~.pos>label:nth-child(4){background:#666;}

#main-test{
padding-top: 150px;
padding-left: 100px;
color: white;
font-size: 20px;
}

.main-button{
	font-size: 14px;
	font-weight: bold;
	line-height: 2;
	border:none;
	border-radius: 10px;
	text-align: center;
	cursor: pointer;
	background:white;
	color: black;
    -webkit-transition-duration: 0.4s;
    transition-duration: 0.4s;
}
.main-button:hover{color:#fff;}
.hover1:hover{
	box-shadow:200px 0 0 0 rgba(0,0,0,0,5) inset;
	background-color: skyblue;
	border: none;
}
.menu {
display: flex;
margin: 0 auto;
width: 100%;

}
.menu-item{
width: 18%;
height: 150px;
background: yellow;
margin: 1%;
}
</style>
</head>
<body>
<div class="container">
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<div class="main">
	
<div id="slide">
  <input type="radio" name="pos" id="pos1" checked>
  <input type="radio" name="pos" id="pos2">
  <input type="radio" name="pos" id="pos3">
  <input type="radio" name="pos" id="pos4">
  <ul>
    <li>
    <p id="main-test">안녕하세요 <br>뺀질코딩입니다.<br><br>
    <button class="main-button hover1">바로가기</button>
    </p>
    </li>
    <li></li>
    <li></li>
    <li></li>
  </ul>
  <p class="pos">
    <label for="pos1"></label>
    <label for="pos2"></label>
    <label for="pos3"></label>
    <label for="pos4"></label>
  </p>
</div>

	</div>
	
	<div class="bbs">
		<div class="menu">
			<div class="menu-item">
				<img src="${pageContect.request.contextPath}/resource/img/idea.png" style="b">
			</div>
			<div class="menu-item">2</div>
			<div class="menu-item">3</div>
			<div class="menu-item">4</div>
			<div class="menu-item">5</div>
		</div>
	</div>
	
	<div class="bbs">
	공지 게시판
	</div>
	
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>




</body>
</html>