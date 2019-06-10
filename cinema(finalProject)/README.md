Cinema
==========================
18. Система Кинотеатр. Вы пишете интернет витрину маленького
Кинотеатра с одним залом. В нем есть Расписание показа фильмов на все 7
дней недели с 9:00 до 22:00 (начало последнего фильма).
Незарегистрированный пользователь может видеть: расписание,
свободные места в зале, возможность зарегистрироваться.
Зарегистрированный пользователь должен быть в состоянии выкупить
билет на выбранное место. Администратор может: внести в расписание
новый фильм, отменить фильм, просматривать посещаемость зала.
### Setup
<li>
     JDK 1.8 or higher
<li>
     Apache Maven 3.6.1 or higher
<li>
     MySQL 8.0.15 or higher
<li>
     Apache Tomcat 8.5.39 or higher
    
### Installation
* Clone project from GitHub (git clone https://github.com/potOfMyCode/cinema-finalProject)
* Specify values of "mysql.user" and "mysql.password" keys in *../src/main/resources/db.property*
* Execute script _../db/cinema_final.sql_ to create database
* cd to root project folder and execute command *mvn clean install*
* copy artifact cinema(finalProject).war from target folder to %TOMCAT%\webapps
    
### Running
* Start Tomcat server by running the script %TOMCAT%\bin\startup .bat for Windows or .sh for Unix based OS.
* After server start, application will be available by URL http://localhost:8080/cinema/films  
* Use login "**admin**" and password "**admin**" to log in with administrator rights.
* To stop server run script %TOMCAT%\bin\shutdown .bat for Windows or .sh for Unix based OS.
