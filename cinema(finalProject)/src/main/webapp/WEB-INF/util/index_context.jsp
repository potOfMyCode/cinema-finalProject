<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 27.05.2019
  Time: 21:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<fmt:setLocale value="${sessionScope.curLang}"/>
<fmt:setBundle basename="lang"/>

<script src="${pageContext.request.contextPath}/js/carousel.js"></script>

<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel" >
    <ol class="carousel-indicators">
        <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="3"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="4"></li>
    </ol>
    <div class="carousel-inner" role="listbox">
        <!-- Slide One - Set the background image for this slide in the line below -->
        <div class="carousel-item active" style="background-image: url(${pageContext.request.contextPath}/picture/posters/TheGodfather.jpg)">
            <div class="carousel-caption d-none d-md-block">
                <h3 class="display-4 sl-txt-big"><fmt:message key="slider1.name"/></h3>
                <p class="lead sl-txt-lit"><fmt:message key="slider1.ds"/></p>
            </div>
        </div>
        <!-- Slide Two - Set the background image for this slide in the line below -->
        <div class="carousel-item" style="background-image: url(${pageContext.request.contextPath}/picture/posters/TheShawshankRedemption.jpg)">
            <div class="carousel-caption d-none d-md-block">
                <h3 class="display-4 sl-txt-big"><fmt:message key="slider2.name"/></h3>
                <p class="lead sl-txt-lit"><fmt:message key="slider2.ds"/></p>
            </div>
        </div>
        <!-- Slide Three - Set the background image for this slide in the line below -->
        <div class="carousel-item" style="background-image: url(${pageContext.request.contextPath}/picture/posters/ForrestGump.jpg)">
            <div class="carousel-caption d-none d-md-block">
                <h3 class="display-4 sl-txt-big"><fmt:message key="slider3.name"/></h3>
                <p class="lead sl-txt-lit"><fmt:message key="slider3.ds"/></p>
            </div>
        </div>
        <!-- Slide Fourth - Set the background image for this slide in the line below -->
        <div class="carousel-item" style="background-image: url(${pageContext.request.contextPath}/picture/posters/Titanic.jpg)">
            <div class="carousel-caption d-none d-md-block">
                <h3 class="display-4 sl-txt-big"><fmt:message key="slider4.name"/></h3>
                <p class="lead sl-txt-lit"><fmt:message key="slider4.ds"/></p>
            </div>
        </div>
        <!-- Slide Fifth - Set the background image for this slide in the line below -->
        <div class="carousel-item" style="background-image: url(${pageContext.request.contextPath}/picture/posters/BraveHeart.jpg)">
            <div class="carousel-caption d-none d-md-block">
                <h3 class="display-4 sl-txt-big"><fmt:message key="slider5.name"/></h3>
                <p class="lead sl-txt-lit"><fmt:message key="slider5.ds"/></p>
            </div>
        </div>
    </div>
    <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="previous"/></span>
    </a>
    <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only"><fmt:message key="next"/></span>
    </a>
</div>

<!-- Page Content -->
<section class="py-5">
    <div class="container">
        <h1 class="font-weight-light"><fmt:message key="index.bottom.h"/></h1>
        <p class="lead"><fmt:message key="index.bottom.p"/></p>
    </div>
</section>

