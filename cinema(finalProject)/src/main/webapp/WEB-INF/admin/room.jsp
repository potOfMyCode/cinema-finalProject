<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 31.05.2019
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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
<jsp:include page="/WEB-INF/admin/header.jsp"/>
<jsp:useBean id="showSession"  scope="request" class="model.entity.Session"/>
<jsp:useBean id="curDate" scope="request" type="java.time.LocalDate"/>
<section class="pricing py-5">
    <div class="wrap-cont">
        <!-- Free Tier -->
        <div class="card-bg-color  mb-5 mb-lg-0">
            <div class="card-body inner-bg">
                <div class="container">
                    <div class='col-md-5'>
                        <div class="form-group">
                            <div class='input-group date' id='datepicker' data-provide="datepicker">
                                <input type='text' class="form-control" disabled/>
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


                    var newDate = new Date();
                    if(now  != dayOfWeek){
                        if(now>dayOfWeek){
                            newDate.setDate(newDate.getDate()+now-dayOfWeek);
                        }else{
                            newDate.setDate(newDate.getDate()+7+now-dayOfWeek);
                        }
                    }
                    <c:choose>
                    <c:when test="${sessionScope.curLang.equals('en') }">
                    $('#datepicker').datepicker('setDate', newDate.toLocaleDateString("en"));
                    </c:when>
                    <c:otherwise>
                    $('#datepicker').datepicker('setDate', newDate.toLocaleDateString());
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
                                        <div class="place-gray"><p class="place-nb">${i}</p></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="place"><p class="place-nb">${i}</p></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="6" end="11">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"><p class="place-nb">${i}</p></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="place"><p class="place-nb">${i}</p></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="12" end="19">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"><p class="place-nb">${i}</p></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="place"><p class="place-nb">${i}</p></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="20" end="27">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"><p class="place-nb">${i}</p></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="place"><p class="place-nb">${i}</p></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="row">
                            <c:forEach var="i" begin="28" end="35">
                                <c:choose>
                                    <c:when test="${showSession.isEngagedPlace(i)}">
                                        <div class="place-gray"><p class="place-nb">${i}</p></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="place"><p class="place-nb">${i}</p></div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="visitors-list">
                            <div class="ticket-row mov-head">
                                <div>
                                    <fmt:message key="movie.name"/>
                                </div>
                                <div>
                                    <fmt:message key="day"/>
                                </div>
                                <div>
                                    <fmt:message key="time"/>
                                </div>
                            </div>
                            <div class="ticket-row">
                                <div>
                                    ${showSession.movie.name}
                                </div>
                                <div>
                                    ${showSession.day.name}
                                </div>
                                <div>
                                    ${showSession.timeHoursMins}
                                </div>
                            </div>

                            <c:if test="${!showSession.ticketList.isEmpty()}">
                                <div class="ticket-row user-head">
                                    <div>
                                        <fmt:message key="place"/>
                                    </div>
                                    <div>
                                        <fmt:message key="user"/>
                                    </div>
                                    <div>
                                        <fmt:message key="movie.date"/>
                                    </div>
                                </div>
                            </c:if>
                            <c:forEach items="${showSession.ticketList}" var="ticket">
                                <div class="ticket-row user-entr">
                                    <div>
                                            ${ticket.place}
                                    </div>
                                    <div>
                                            ${ticket.owner.username}
                                    </div>
                                    <div>
                                        <c:choose>
                                            <c:when test="${sessionScope.curLang.equals('en') }">
                                                <tags:localDate date="${ticket.date}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <tags:localDate date="${ticket.date}" pattern="dd/MM/yyyy"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>
