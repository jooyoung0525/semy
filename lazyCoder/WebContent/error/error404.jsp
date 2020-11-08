<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%response.setStatus(HttpServletResponse.SC_OK);%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>404에러 페이지</title>
</head>
<style type="text/css">
.container {
	width: 1080px;
	margin: 0 auto;/* 중앙정렬 */
	
}

.intro{
width:100%;
	display:flex;
	float: left;
	margin-left: 40%;
}
</style>
</head>
<body>

<div class="container">
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>

	<div class="intro">
		<img alt="error404" src="${pageContext.request.contextPath}/resource/img/errorpage.png" style="width: 20%; height: 20%; ">
	</div>
	<br><br><br><br><br><br><br><br><br>
<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>

</body>
</html>