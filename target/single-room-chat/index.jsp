<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% String WsUrl = getServletContext().getInitParameter("WsUrl"); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name='viewport' content='minimum-scale=1.0, initial-scale=1.0,
	width=device-width, maximum-scale=1.0, user-scalable=no'/>
<title>Diffle helman -chat</title>
<link rel="stylesheet" type="text/css" href="content/styles/site.css">
<script type="text/javascript" src="scripts/chatroom.js"></script>




    <!-- Bootstrap core CSS -->
    <link href="content/styles/bootstrap.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="content/styles/main.css" rel="stylesheet">
    <link href="content/styles/font-awesome.min.css" rel="stylesheet">


<script type="text/javascript">
var wsUri = '<%=WsUrl%>';
var proxy = CreateProxy(wsUri);

document.addEventListener("DOMContentLoaded", function(event) {
	console.log(document.getElementById('loginPanel'));
	proxy.initiate({
		loginPanel: document.getElementById('loginPanel'),
		msgPanel: document.getElementById('msgPanel'),
		txtMsg: document.getElementById('txtMsg'),
		txtLogin: document.getElementById('txtLogin'),
		msgContainer: document.getElementById('msgContainer')
	});
});

</script>
</head>
<body>
    
    <%
    if (request.getParameter("login") == null) {
        response.sendRedirect("http://localhost:8080/account/");
    } else {
        out.println("Hello <b>"+request. getParameter("name")+"</b>!");
    }
%>
<div id="container">
	
</div>
    
    
    
    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#"> Secure crypto chatroom</a>
        </div>
        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
            <li class="active"><a href="#contact"> <% out.println("Hello <b>"+request. getParameter("name")+"</b>!"); %>Chat safely !</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </div>


	<div id="hello">
	    <div class="container">
                
                
	    	<div class="row">
	    		<div class="col-lg-8 col-lg-offset-2 centered">
                            
                            <div id="loginPanel">
		<div id="infoLabel">Click login to join the chat room</div>
		<div style="padding: 10px;">
			<input value="<%= request.getParameter("name") %>" id="txtLogin" type="text" class="loginInput"
				onkeyup="proxy.login_keyup(event)" />
			<button type="button" class="btn btn-theme btn-lg" onclick="proxy.login()" >Login</button>
		</div>
	</div>
	
                            
                            <div id="msgPanel" style="display: none">
                                <h1>Chat Room</h1>
	    			<h2>Get started</h2>
		<div id="msgContainer" style="overflow: auto;"></div>
		<div id="msgController">
			<textarea id="txtMsg" 
				title="Enter to send message"
				onkeyup="proxy.sendMessage_keyup(event)"
				style="height: 50px; width: 100%"></textarea>
			<button  class="btn btn-danger" type="button" style="height: 50px;"
				onclick="proxy.logout()">Logout</button>
		</div>
	</div>
                            
	    			<h1>Chat Room</h1>
	    			<h2>Using Cryptography Technology</h2>
	    		</div><!-- /col-lg-8 -->
	    	</div><!-- /row -->
	    </div> <!-- /container -->
	</div><!-- /hello -->
	
	<div id="green">
		<div class="container">
			<div class="row">
				<div class="col-lg-5 centered">
					<img src="content/img/iphone.png" alt="">
				</div>
				
				<div class="col-lg-7 centered">
					<h3>WHAT WE DO?</h3>
					<p> Symmetric Key Encryption Algorithms.</p>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="row centered mt grid">
			<h3>HOW YOUR MESSAGES ARE SECURED</h3>
			<div class="mt"></div>
			<div class="col-lg-4">
                            <a href="#"><img src="content/img/KeyAgreement.gif" alt=""></a>
			</div>
			<div class="col-lg-4">
                            <a href="#"><img src="content/img/digsgn.gif" alt=""></a>
			</div>
			<div class="col-lg-4">
				<a href="#"><img src="content/img/03.jpg" alt=""></a>
			</div>
		</div>
		
		<div class="row centered mt grid">
			<div class="mt"></div>		
			<div class="col-lg-4">
				<a href="#"><img src="<%=request.getContextPath()%>/content/img/04.jpg" alt=""></a>
			</div>
			<div class="col-lg-4">
				<a href="#"><img src="content/img/05.jpg" alt=""></a>
			</div>
			<div class="col-lg-4">
				<a href="#"><img src="content/img/06.jpg" alt=""></a>
			</div>
		</div>
		
		<div class="row mt centered">
			<div class="col-lg-7 col-lg-offset-1 mt">
					<p class="lead">By using this method, you can double ensure that your secret message is sent secretly without outside interference of hackers or crackers. .</p>
			</div>
			
			<div class="col-lg-3 mt">
				<p><button type="button" class="btn btn-theme btn-lg">Register now!</button></p>
			</div>
		</div>
	</div>


	<div id="skills">
		<div class="container">
			<div class="row centered">
				<h3>The Algorithms Used </h3>
				<div class="col-lg-3 mt">
					<canvas id="javascript" height="130" width="130"></canvas>
					<p>Diffie Hellman Key Exchange</p>
					<br>
					<script>
						var doughnutData = [
								{
									value: 70,
									color:"#74cfae"
								},
								{
									value : 30,
									color : "#3c3c3c"
								}
							];
							var myDoughnut = new Chart(document.getElementById("javascript").getContext("2d")).Doughnut(doughnutData);
					</script>
				</div>
				<div class="col-lg-3 mt">
					<canvas id="bootstrap" height="130" width="130"></canvas>
					<p>AES </p>
					<br>
					<script>
						var doughnutData = [
								{
									value: 90,
									color:"#74cfae"
								},
								{
									value : 10,
									color : "#3c3c3c"
								}
							];
							var myDoughnut = new Chart(document.getElementById("bootstrap").getContext("2d")).Doughnut(doughnutData);
					</script>
				</div>
				<div class="col-lg-3 mt">
					<canvas id="wordpress" height="130" width="130"></canvas>
					<p>Hashing Algo</p>
					<br>
					<script>
						var doughnutData = [
								{
									value: 65,
									color:"#74cfae"
								},
								{
									value : 35,
									color : "#3c3c3c"
								}
							];
							var myDoughnut = new Chart(document.getElementById("wordpress").getContext("2d")).Doughnut(doughnutData);
					</script>
				</div>
				<div class="col-lg-3 mt">
					<canvas id="photoshop" height="130" width="130"></canvas>
					<p>Key Decryption</p>
					<br>
					<script>
						var doughnutData = [
								{
									value: 50,
									color:"#74cfae"
								},
								{
									value : 50,
									color : "#3c3c3c"
								}
							];
							var myDoughnut = new Chart(document.getElementById("photoshop").getContext("2d")).Doughnut(doughnutData);
					</script>
				</div>
			</div><!-- /row -->
		</div><!-- /container -->
	</div><!-- /skills -->
	
	<section id="contact"></section>
	<div id="social">
		<div class="container">
			<div class="row centered">
				<div class="col-lg-8 col-lg-offset-2">
					<div class="col-md-3">
						<a href="#"><i class="fa fa-facebook"></i></a>
					</div>
					<div class="col-md-3">
						<a href="#"><i class="fa fa-dribbble"></i></a>
					</div>
					<div class="col-md-3">
						<a href="#"><i class="fa fa-twitter"></i></a>
					</div>
					<div class="col-md-3">
						<a href="#"><i class="fa fa-envelope"></i></a>
					</div>
				</div>
			</div>
		</div><!-- /container -->
	</div><!-- /social -->


	<div id="f">
		<div class="container">
			<div class="row">
				<p>Powered by <i class="fa fa-heart"></i> AES and Diffie Hellman Key Exchange</p>
			</div>
		</div>
	</div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="content/scripts/bootstrap.js"></script>
    
</body>
</html>