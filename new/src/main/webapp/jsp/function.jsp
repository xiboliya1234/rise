<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>登录页面</title>
<link rel="stylesheet" type="text/css" href="js/login.css">
<script type="text/javascript" src="js/config.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.7.0/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/xlsx.full.min.js"></script>
<script type="text/javascript">

function jump(val){
	
	 window.open(url+val,"_self");
	
}


function upload(){
	const fileReader = new FileReader();	
	 // 3. 获取到用户选择的文件列表
    const fileField = document.querySelector('input[type="file"]');
    //alert(files);
	//alert(files.length);
    if (fileField.length <= 0) {
      return alert('请选择要上传的文件！');
    }else{
    	
    
    

    	const formData = new FormData();
    	

    	
    	formData.append('file', fileField.files[0]);

    	fetch(url+'updateExcel', {
    	  method: 'POST',
    	//   headers: {
//    	     "Content-Type": "multipart/form-data"	// 我们手动加这个header会导致错误，让浏览器自己去设置这个header即可！
    	//   },
    	  body: formData
    	})
    	  .then((response) => response.json())
    	  .then((result) => {
    	    alert('Success:', "上传成功");
    	  })
    	  .catch((error) => {
    		  alert('Error:', error);
    	  });

    }
		
		
		
		
		
		   
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
					
						<h2>黄粱美梦</h2>
						<form >
							
							<div class="inputBox">
							 	
								<input type="button" id="jumptoupdate"  value="上传" onclick="jump(this.id)">
								<input type="button" id="jumptoMap" value="地铁图" onclick="jump()">
								<input type="button" value="视频" onclick="jump()">
							</div>
							
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