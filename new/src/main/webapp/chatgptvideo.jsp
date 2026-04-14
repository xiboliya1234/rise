<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="video-manager.css">
  <title>本地视频管理页面</title>
</head>
<body>
  <h1>本地视频管理</h1>
  <input type="file" id="video-input" multiple accept="video/*">
  <div id="video-list"></div>
  <script src="video-manager.js"></script>
</body>
</html>
