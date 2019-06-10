<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 31.05.2019
  Time: 18:51
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
    <title>Now_playing</title>

    <link  href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/now_playing.css" rel="stylesheet" type="text/css" >
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/ticket_scr.js"></script>
    <script src="${pageContext.request.contextPath}/js/now_playing.js"></script>


</head>

<body>
<div class="header">
    <link  href="${pageContext.request.contextPath}/css/styles.css" type="text/css" rel="stylesheet">
    <jsp:include page="/WEB-INF/admin/header.jsp"/>
</div>

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
<div class="modal fade" id="modal-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="img-thumbnail">
                    <button class="close" type="button" data-dismiss="modal">&times;</button>
                    <img src="${pageContext.request.contextPath}/picture/backgrounds/new_film.jpg" alt="">
                    <div class="caption align-content-center">
                        <h2 class="font-weight-normal"><fmt:message key="add.movie"/></h2>
                        <p class="font-weight-light"><fmt:message key="aspect.ratio"/></p>
                        <div class="nm-form">
                            <form action="${pageContext.request.contextPath}/films/addmovie?${pageContext.request.queryString}" method="post" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label for="engName"><fmt:message key="eng.name"/></label>
                                    <input name="engName" type="text" class="form-control" id="engName" aria-describedby="engNameHint" required>
                                    <small id="engNameHint" class="form-text text-muted"><fmt:message key="eng.name.small"/></small>
                                </div>
                                <div class="form-group">
                                    <label for="ukrName"><fmt:message key="ukr.name"/></label>
                                    <input name="ukrName" type="text" class="form-control" id="ukrName" aria-describedby="ukrNameHint" required>
                                    <small id="ukrNameHint" class="form-text text-muted"><fmt:message key="ukr.name.small"/></small>
                                </div>
                                <label for="inputGroupFile01"><fmt:message key="pic.file"/></label>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="inputGroupFileAddon01"><fmt:message key="upload"/></span>
                                    </div>
                                    <div class="custom-file">
                                        <input name="pic" type="file" class="custom-file-input" id="inputGroupFile01" accept="image/*" required>
                                        <label class="custom-file-label" for="inputGroupFile01"></label>
                                    </div>
                                </div>

                                <button type="submit" class="nm-btn btn btn-primary mb-2 btn-block"><fmt:message key="add.movie"/></button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-danger" type="button" data-dismiss="modal"><fmt:message key="cancel"/></button>
            </div>
        </div>

    </div>
</div>


<div class="container">
    <c:forEach var="film" items="${requestScope.moviesForShow}">
    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">

            <button type="button" class="btn btn-success" data-toggle="modal" data-target="#modal-1"><fmt:message key="create.movie"/></button>

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

        <div class="col-xs-12 col-sm-12 col-md-6 col-lg-4">

            <div class="delete-btn">
                <button type="button" class="btn btn-danger" onclick="notify_del('${film.name}', 'place-holder');notify_del('${film.id}', 'id-movie-holder');appearDivById('sure-span')">
                    <img src="${pageContext.request.contextPath}/picture/icons/trash30.png"/>
                </button>
            </div>

            <div id="sure-span" class="sure-span" style="background-color: white; border-radius: 10px; display:none">
                <div class="order-top">
                    <h3 class="font-weight-normal"><fmt:message key="delete.movie"/></h3>
                    <p class="font-weight-normal disp"><fmt:message key="movie.name"/></p>
                    <p id="place-holder" class="disp"></p>
                    <p id="id-movie-holder" style="display: none"></p>
                </div>

                <div class="order-bottom">
                    <button class="btn btn-danger" onclick="post('/remmovie', {movieId : document.getElementById('id-movie-holder').innerText}, 'post')">
                        <fmt:message key="remove"/>
                    </button>
                    <button class="btn btn-outline-secondary" onclick="cls_span('sure-span')">
                        <fmt:message key="cancel"/>
                    </button>
                </div>
            </div>

        </div>


    </div>
    </c:forEach>
</div>


</body>
</html>

