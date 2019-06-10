<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 01.06.2019
  Time: 13:48
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>
<html>
<head>
    <title>Title</title>
    <link  href="${pageContext.request.contextPath}/css/forb.css" type="text/css" rel="stylesheet">
    <link  href="${pageContext.request.contextPath}/<%--webjars/bootstrap/3.2.0/--%>css/bootstrap.css" rel="stylesheet"/>
    <link  href="${pageContext.request.contextPath}/css/all.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/post.js"></script>
</head>
<body>
<div class="main-card">
    <img src="${pageContext.request.contextPath}/picture/icons/success.png"/>
    <div class="sorry-msg">
        <h2 class="font-weight-normal">Operation success!</h2>
        <h2 class="font-weight-normal">${msg}</h2>
        <h4 class="font-weight-normal" style="margin-top: 35px;">
            <a class="non" href="#" onclick="go_back()">${msg2}</a><br>
            <a class="" href="${pageContext.request.contextPath}/films/"><fmt:message key="back.home"/></a>
        </h4>
    </div>
</div>

</body>
</html>

