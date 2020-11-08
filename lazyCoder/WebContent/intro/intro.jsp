<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/resource/img/titlelogo.png">
<title>뺀질코딩</title>
<style type="text/css">
.container {
	width: 1080px;
	margin: 0 auto;/* 중앙정렬 */
	
}

.intro{
	
}
</style>
</head>
<body>

<div class="container">
<jsp:include page="/WEB-INF/views/layout/header.jsp"/>
<br>
<div class="intro">
<img alt="intro1" src="${pageContext.request.contextPath}/resource/img/intro2.png" style="width: 100%; height: 600px;">
<img alt="intro2" src="${pageContext.request.contextPath}/resource/img/intro3.png" style="width: 100%; height: 600px;">
<img alt="intro3" src="${pageContext.request.contextPath}/resource/img/intro5.png" style="width: 100%; height: 600px;">
<img alt="intro4" src="${pageContext.request.contextPath}/resource/img/intro4.png" style="width: 100%; height: 600px;">
</div>
<br>
<jsp:include page="/WEB-INF/views/layout/footer.jsp"/>
</div>

</body>
</html>