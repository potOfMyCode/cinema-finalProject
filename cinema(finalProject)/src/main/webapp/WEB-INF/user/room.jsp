<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 01.06.2019
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>
<html>
<head>
    <title>Room</title>
    <link  href="${pageContext.request.contextPath}/css/styles.css" type="text/css" rel="stylesheet">
    <link  href="${pageContext.request.contextPath}/css/room.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/jquery.datetimepicker.min.css" rel="stylesheet" type="text/css"/>
    <link  href="${pageContext.request.contextPath}/css/showtimes.css" type="text/css" rel="stylesheet">
    <link  href="${pageContext.request.contextPath}/css/font-glyphicons.css" type="text/css" rel="stylesheet">

    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.datetimepicker.full.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/moment.js"></script>
    <script src="${pageContext.request.contextPath}/js/ticket_scr.js"></script>
    <script src="${pageContext.request.contextPath}/js/post.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/user/header.jsp"/>
<jsp:useBean id="showSession"  scope="request" class="model.entity.Session"/>
<section class="pricing py-5">
    <div class="wrap-cont">
        <!-- Free Tier -->
        <div class="card-bg-color  mb-5 mb-lg-0">
            <div class="card-body inner-bg">
                <div class="container">
                    <div class='col-md-5'>
                        <div class="form-group">
                            <div class='input-group date' id='datepicker' data-provide="datepicker">
                                <input type='text' class="form-control" id="extractText" disabled/>
                                <span class="input-group-addon input-group-append" role="right-icon">
                            <button class="btn btn-outline-secondary border-left-0" type="button"><i class="glyphicon glyphicon-calendar"></i></button>
                        </span>
                            </div>
                        </div>
                    </div>
                </div>

                <script type="text/javascript">
                    $(function () {
                        $('#datepicker').datepicker();
                    });
                    var dayOfWeek = new Date().getDay();
                    if(dayOfWeek == 0){
                        dayOfWeek = 7;
                    }
                    var now = ${requestScope.showSession.day.id};


                    var newDate = new Date;
                    if(now  != dayOfWeek){
                        if(now>dayOfWeek){
                            newDate.setDate(newDate.getDate()+now-dayOfWeek);
                        }else{
                            newDate.setDate(newDate.getDate()+7+now-dayOfWeek);
                        }
                    }else{
                        newDate = new Date();
                    }
                    <c:choose>
                    <c:when test="${sessionScope.curLang.equals('en') }">
                    $('#datepicker').datepicker('setDate', newDate.toLocaleDateString("en"));
                    </c:when>
                    <c:otherwise>
                    $('#datepicker').datepicker('setDate', newDate.toLocaleDateString("uk"));
                    </c:otherwise>
                    </c:choose>

                </script>
                <div class="table-container">
                    <div class="screen"><h2 class="font-weight-light"><fmt:message key="screen"/></h2></div>
                    <div class="sits">
                        <div class="row">
                            <c:forEach var="i" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-info place"
                                                onclick="notify_tick('${showSession.movie.name}','${showSession.day.name}', newDate,'${showSession.timeHoursMins}','${i}');">
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="6" end="11">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-info place"
                                                onclick="notify_tick('${showSession.movie.name}','${showSession.day.name}', newDate,'${showSession.timeHoursMins}','${i}');">
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="12" end="19">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-info place"
                                                onclick="notify_tick('${showSession.movie.name}','${showSession.day.name}', newDate,'${showSession.timeHoursMins}','${i}');">
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="20" end="27">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-info place"
                                                onclick="notify_tick('${showSession.movie.name}','${showSession.day.name}', newDate,'${showSession.timeHoursMins}','${i}');">
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="28" end="35">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-info place"
                                                onclick="notify_tick('${showSession.movie.name}','${showSession.day.name}', newDate,'${showSession.timeHoursMins}','${i}');">
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <h4 class="font-weight-light" style="margin-top: 50px"><fmt:message key="presstobuy"/></h4>
                        </div>
                        <div id="order-form" class="order-form" style="display:none">
                            <div class="order-top">
                                <button type="button" class="btn-close btn btn-labeled btn-danger" onclick="cls_span('order-form')">
                                    <span class="btn-label"><i class="glyphicon glyphicon-remove"></i></span></button>
                                <h3 class="font-weight-normal"><fmt:message key="ticket"/></h3>
                            </div>
                            <div class="tickets-list">
                                <div class="ticket">
                                    <div class="t-row">
                                        <h6 class="font-weight-norman"><fmt:message key="movie.name"/>:</h6>
                                        <h6 class="font-weight-norman"><fmt:message key="day"/>:</h6>
                                        <h6 class="font-weight-norman"><fmt:message key="date"/>:</h6>
                                        <h6 class="font-weight-norman"><fmt:message key="time"/>:</h6>
                                        <h6 class="font-weight-norman"><fmt:message key="place"/>:</h6>
                                    </div>
                                    <div class="t-row">
                                        <h6 id="mov" class="font-weight-norman"></h6>
                                        <h6 id="day" class="font-weight-norman"></h6>
                                        <h6 id="date" class="font-weight-norman"></h6>
                                        <h6 id="time" class="font-weight-norman"></h6>
                                        <h6 id="place" class="font-weight-norman" style="font-size: 125%"></h6>
                                    </div>
                                </div>
                            </div>
                            <div class="order-bottom">
                                <button class="btn btn-success btn-block"
                                onclick="post('/order', {place : document.getElementById('place').innerText, session : ${showSession.id}, curDay: newDate.getDate(), curMonth : (newDate.getMonth()+1), curYear : newDate.getFullYear()}, 'post')">
                                    <fmt:message key="purchase"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>
