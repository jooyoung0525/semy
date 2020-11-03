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
.bbs{
height: 200px;
background-color: silver;
margin-bottom: 10px;

}
.imgbox1{
display: flex;
justify-content: center;

}

.imgbox {
width: 200px;
height: 150px;
background-color: red;
margin: 20px;
}

.bbs2-table{
width: 800px;
text-align: center;
border-spacing: 0; border-collapse:collapse;
margin-left: 100px;
}

.bbs2-table tr{
	border-bottom: 3px solid blue;

}
.bbs2-table th{
	height: 40px;
	border-bottom: 3px solid blue;
	width: 100px;
}

.bbs2-table td{
width: 100px;
	border-bottom: 3px solid blue;
}
</style>
</head>
<body>
<div class="container">
	<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
	
	<div class="main">
		<div class="bbs" id="bbs1">
			<p>사진게시판</p>
		</div>
		
		<div class="bbs" id="bbs2">
			추천게시판
			<table class="bbs2-table">
				<tr>
					<td width="20%">작성자</td>
					<td width="60%">제목</td>
					<td width="40%">작성일</td>
				</tr>
				<tr>
					<td >이영헌</td>
					<td >소스코드 알려주세요</td>
					<td >2020</td>
				</tr>

			</table>
			
		</div>	
		<div class="bbs" id="bbs3">
			공지사항
		</div>	
	</div>
	
	
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>




</body>
</html>