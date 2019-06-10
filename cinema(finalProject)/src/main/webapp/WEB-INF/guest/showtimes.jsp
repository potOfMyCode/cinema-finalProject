<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 31.05.2019
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>
<html>
<head lang="${sessionScope.curLang}">
    <title>Showtimes</title>
    <link  href="${pageContext.request.contextPath}/css/styles.css" type="text/css" rel="stylesheet">
    <link  href="${pageContext.request.contextPath}/css/all.css" type="text/css" rel="stylesheet">
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
    <script src="${pageContext.request.contextPath}/js/post.js"></script>
</head>
<body>
    <jsp:include page="/WEB-INF/guest/header.jsp"/>
    <jsp:useBean id="day" scope="request" type="model.entity.Day"/>

    <section class="pricing py-5">
        <div class="wrap-cont">
            <div class="card-bg-color  mb-5 mb-lg-0">
                <div class="card-body inner-bg">
                    <br>
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
                        var now = ${day.id};




                        var newDate = new Date;
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
                        <div class="own-custom-btn btn-group btn-group-toggle" data-toggle="buttons">
                            <label class="btn btn-dark ${day.id == 1 ? "focus active" : ""}" onclick="post('/showtimes', {day : 1})">
                                <input type="radio" name="monday" id="monday" autocomplete="off" checked><fmt:message key="monday"/>
                            </label>
                            <label class="btn btn-dark ${day.id == 2 ? "focus active" : ""}" onclick="post('/showtimes', {day : 2})">
                                <input type="radio" name="tuesday" id="tuesday" autocomplete="off"><fmt:message key="tuesday"/>
                            </label>
                            <label class="btn btn-dark ${day.id == 3 ? "focus active" : ""}" onclick="post('/showtimes', {day : 3})">
                                <input type="radio" name="wednesday" id="wednesday" autocomplete="off"><fmt:message key="wednesday"/>
                            </label>
                            <label class="btn btn-dark ${day.id == 4 ? "focus active" : ""}" onclick="post('/showtimes', {day : 4})">
                                <input type="radio" name="thursday" id="thursday" autocomplete="off"><fmt:message key="thursday"/>
                            </label>
                            <label class="btn btn-dark ${day.id == 5 ? "focus active" : ""}" onclick="post('/showtimes', {day : 5})">
                                <input type="radio" name="friday" id="friday" autocomplete="off"><fmt:message key="friday"/>
                            </label>
                            <label class="btn btn-dark ${day.id == 6 ? "focus active" : ""}" onclick="post('/showtimes', {day : 6})">
                                <input type="radio" name="saturday" id="saturday" autocomplete="off"><fmt:message key="saturday"/>
                            </label>
                            <label class="btn btn-dark ${day.id == 7 ? "focus active" : ""}" onclick="post('/showtimes', {day : 7})">
                                <input type="radio" name="sunday" id="sunday" autocomplete="off"><fmt:message key="sunday"/>
                            </label>
                        </div>

                        <c:forEach items="${day.sessions}" var="session">
                            <div class="movie-row">
                                <div class="mov-name-entry">
                                    ${session.movie.name}
                                </div>
                                <div class="mov-time">
                                    ${session.timeHoursMins}
                                    <button type="button" class="btn btn-info purch-btn" onclick="post('/room', {sessionId : ${session.id}}, 'get')">
                                        <fmt:message key="room"/>
                                    </button>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </section>

</body>
</html>