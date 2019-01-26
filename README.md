## Movie Movies
This is a full-stack project of applying what I have learned and to build a Web Application.

**Skills**: HTML5, CSS, JavaScript, Bootstraps, jQuery, SQL(MySql), Apache Tomcat, JDBC, Maven

This is a simple version of movie website which it can search any movie and display it's information. The website is makeup of two parts - the login page and the main page. 

The default username and password is "iamusername", "iampassword".

### How to run this project:
1.	Checkout your desire directory and clone this repository use this command `git clone https://github.com/xinkailin95/first-web-project.git`
2. Create a database and tables use createtale.sql. 
3. Import datas use movie-data.sql or movie\_data\_light.sql (less data). 
4. open Eclipse -> File -> import -> under "Maven" -> "Existing Maven Projects" -> Click "Next". For "Root Directory", click "Browse" and select this repository's folder. Click "Finish".
5. In `WebContent/META-INF/context.xml`, make sure to change the username and password to your sql username and password.
7. Right click on the project and "Run As" -> "Run on Server".
8. **(Optional)** Export project as WAR file and deploy to Tomcat, you can run this way as well.
    