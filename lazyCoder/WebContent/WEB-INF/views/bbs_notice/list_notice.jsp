<!--content 부분   -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
.container {
	width: 1080px;
	margin: 0 auto;/* 중앙정렬 */
	
}

.content{
	overflow:hidden;
	/* display: flex; */
}
.aside {
	height: 200px;
	background-color: skyblue;
	width: 25%;
	float :left;
}
.section {
	float :right;
	height: 200px;
	background-color: yellow;
	width: 65%;
	
}


</style>
</head>
<body>
<div class="container">

	<div class="content">
	<!--aside 왼쪽 메뉴바 -->
		<div class="aside"></div>
	<!--본문 내용 게시판 -->
		<div class="section">

		</div>
	</div>

</div>




</body>
</html>