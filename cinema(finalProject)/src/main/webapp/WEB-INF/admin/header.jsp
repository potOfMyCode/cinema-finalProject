<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 31.05.2019
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>
<c:set var="user" value="${sessionScope.sessionUser}"/>
<jsp:useBean id="user" class="model.entity.User"/>
<jsp:setProperty name="user" property="username" value="${sessionScope.sessionUser.username}"/>
<jsp:setProperty name="user" property="password" value="${sessionScope.sessionUser.password}"/>
<jsp:setProperty name="user" property="email" value="${sessionScope.sessionUser.email}"/>
<jsp:setProperty name="user" property="role" value="${sessionScope.sessionUser.role}"/>

<script src="${pageContext.request.contextPath}/js/signAjax.js"></script>
<script src="${pageContext.request.contextPath}/js/post.js"></script>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark mynavbar header shadow-c">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/films/">Cinema</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item mybutton">
                    <a class="nav-link" href="${pageContext.request.contextPath}/films/"><fmt:message key="header.home"/>
                        <span class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item mybutton">
                    <a class="nav-link" href="${pageContext.request.contextPath}/films/now_playing">
                        <fmt:message key="header.now.plays"/>
                    </a>
                </li>
                <li class="nav-item mybutton">
                    <a class="nav-link" href="${pageContext.request.contextPath}/films/showtimes"><fmt:message key="header.showings"/></a>
                </li>
                <li class="nav-item dropdown mybutton">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <fmt:message key="header.language"/>
                    </a>
                    <!-- Here's the magic. Add the .animate and .slide-in classes to your .dropdown-menu and you're all set! -->
                    <div class="crystal shadow-c dropdown-menu dropdown-menu-right animate slideIn" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item"  onclick="{return renewPage(deleteGetParam('curLang'))}" href="#"
                           style="padding-left: 12px">
                            <img src="${pageContext.request.contextPath}/picture/langs/United-Kingdom.png"
                                 style="margin-right: 24px"/><fmt:message key="header.lang.eng"/>
                        </a>
                        <a class="dropdown-item" onclick="{return renewPage(setGetParam('curLang', 'uk'))}" href="#"
                           style="padding-left: 12px;">
                            <img src="${pageContext.request.contextPath}/picture/langs/Ukraine.png"
                                 style="margin-right: 24px"/><fmt:message key="header.lang.ukr"/>
                        </a>
                    </div>
                </li>
                <li class="nav-item mybutton">
                    <a class="btn nav-link rounded singup" style="background: #ffc107; color: black;" href="${pageContext.request.contextPath}/films/logout"><fmt:message key="header.logout"/></a>
                </li>
                <li class="nav-item mybutton">
                    <span class="btn nav-link rounded singup" style="background: #ffc107; color: black;"><fmt:message key="admin"/></span>
                </li>
            </ul>
        </div>
    </div>
</nav>
