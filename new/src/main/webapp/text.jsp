<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>各种测试</title>
<!-- 引入样式 -->

	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.7.0/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.7.0/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.7.0/demo.css">
	<script type="text/javascript" src="js/jquery-easyui-1.7.0/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script src="js/index1.js"></script>


<script type="text/javascript">
/////////////////////假进度条 开始/////////////////////////////////
		const p = new FakeProgress({
			timeConstant: 10000,
			autoStart: true
		});//设置假进度值,通过p.progress调用
		
		 var i = 0;
		 var timer = setInterval(function () {//设置时间器
			 
			 var value = $('#p').progressbar('getValue');//获取id为p的标签的value
				if (value < 100){//只要进度条小于100%
				    value = p.progress*100;//给进度条赋假值
				    $('#p').progressbar('setValue', value);
				}
		     //console.log(i++);
		     if (i>1000) {//触发1000次
	        clearInterval(timer);
		     }
		 },100)//100代表每毫秒触发一次,1000代表每秒触发一次
/////////////////////假进度条  结束/////////////////////////////////
		
		
		function fackprogress(){
			 var value = $('#p').progressbar('getValue');
			
			 $('#p').progressbar('setValue', 100);
		}
		
		

		
		
		
</script>
</head>
<body>
<div id="p" class="easyui-progressbar" data-options="value:0" style="width:400px;"></div>
		<div>
		
			<button onclick="fackprogress()">结束进度条</button>
		</div>
</body>
</html>