<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>登录页面</title>
<link rel="stylesheet" type="text/css" href="js/login.css">

<style type="text/css">

.form .inputBox input {
width: 100%;
background: rgba(255，255，255， .2);
border: none;
outline: none;
padding: 10px 20px ;
border-radius: 35px;
box-shadow:  25px 45px rgba(0,0,0,.5);
border-right: 1px solid rgba(255，255，255，.2);
border-bottom: 1px solid rgba(255，255，255， .2);
letter-spacing: 1px;

color :black;
box-shadow: 0 5px 15px rgba(0,0,0,.05);

}
</style>


</link>


<script type="text/javascript" src="js/config.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript">

	function login(){
		
		var username = $("#username").val();
		var password = $("#password").val();
		//alert(username);
		if(username == null ){
			alert("请填写用户名");
		}
		if(password == null ){
			alert("请填写密码名");
		}
		
		
		var user = {
				
				"username":username,
				"password":password
		};
		var jsonuser =JSON.stringify(user)
		$.ajax({
			
			type:"POST",
			url:url+"loginByUsernameAndPassword",
			contentType:"application/json",
			xhrFields: {
		        withCredentials: true
		    },
		    crossDomain: true,
			dataType:"json",
			jsonp: 'callback',
			data:JSON.stringify(user),
			success:function(data){
				
				if(data.code=="200"){
					//alert("登陆成功");
					window.open(url+"jumptofunction","_self")
				}else{
					alert(data.message);
				}
			},
			error:function(data){
				alert(data.message);
			}
		})
		
		   
	}

</script>


</head>
<body>
		<section>
			<div class="color"></div>
			<div class="color"></div>
			<div class="color"></div>
			
			
			
			<div class="box">
			
				<div class="container">
				
					<div class ="form">
					
						<h2>仪轨1.0登陆界面</h2>
						<form >
							<div class="inputBox" >
								<h5>用户名</h5><input type="text" color ="black" id="username">
							</div>
							<div class="inputBox" >
								<h5>密码</h5><input type="password" id="password">
							</div>
							<div class="inputBox">
								<input type="button" value="登录" onclick="login()">
							</div>
							<p class="forget"> 忘记密码?
								<a href ="">点击此处</a>
							</p>
							<p class="forget">还没有账户?
								<a href ="jumptosignup">注册</a>
							</p>
						</form>
					</div>
					
				</div>




			<div class="square"style="--i:0"></div>
				<div class="square"style="--i:1"></div>
				<div class="square"style="--i:2"></div>
				<div class="square"style="--i:3"></div>
				<div class="square"style="--i:4"></div>
			</div>
			
			
			
			
			
			
			
			
			
			
		</section>
</body>
</html>