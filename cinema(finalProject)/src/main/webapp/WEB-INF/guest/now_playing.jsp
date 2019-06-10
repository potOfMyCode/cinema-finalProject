<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 31.05.2019
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>
<jsp:useBean id="moviesBean" scope="request" type="java.util.List"/>

<html>
<head lang="${sessionScope.curLang}">
    <title>now_playing</title>

    <link  href="${pageContext.request.contextPath}/css/styles.css" type="text/css" rel="stylesheet">
    <link  href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/now_playing.css" type="text/css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
</head>

<body>

<jsp:include page="/WEB-INF/guest/header.jsp"/>


<br>

<nav aria-label="Page navigation example" >
    <ul class="pagination justify-content-center" style="font-size: 36px">
        <li class="page-item" >
            <c:if test="${requestScope.currentPage != 1}">
                <td><a href="#" onclick="{return renewPage(setGetParam('page', ${requestScope.currentPage - 1}))}">&laquo;   </a></td>
            </c:if>
        </li>

        <li class="page-item active">
            <c:forEach begin="1" end="${requestScope.noOfPages}" var="i">
                <c:choose>
                    <c:when test="${requestScope.currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="#" onclick="{return renewPage(setGetParam('page', ${i}))}">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </li>
        <li class="page-item" >
            <c:if test="${requestScope.currentPage lt requestScope.noOfPages}">
                <td><a href="#" onclick="{return renewPage(setGetParam('page', ${requestScope.currentPage + 1}))}">   &raquo;</a></td>
            </c:if>
        </li>
    </ul>
</nav>


<div class="container">
    <c:forEach var="film" items="${requestScope.moviesForShow}">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">

        </div>
            <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">
                <div class="img-thumbnail" style="background-color: #DAF7A6">
                    <img src="${pageContext.request.contextPath}/picture/filmPic/${film.picUrl}" alt="">
                    <div class="caption align-content-center">
                        <h3><a href="#">${film.name}</a></h3>
                        <p><fmt:message key="film.description.${film.id}"/></p>
                        <a href="${pageContext.request.contextPath}/films/showtimes" class="btn btn-success btn-block"><fmt:message key="details"/> <i class="fa fa-arrow-right"></i></a>
                    </div>
                </div>
            </div>

        </div>
    </div>
    </c:forEach>

</body>
</html>
