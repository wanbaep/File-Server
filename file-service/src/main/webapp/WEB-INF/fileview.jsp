<%--
  Created by IntelliJ IDEA.
  User: wanbaep
  Date: 2017. 9. 17.
  Time: PM 4:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>파일 서비스</title>
    <link href="/resources/css/bootstrap.css" rel="stylesheet">
</head>
<body>

<br>
<h2 style="text-align:center">파일리스트</h2>
<br>

<div class="container">
  <div class="file-list row">
    <div>
      <span class="col-sm-2">파일ID</span>
      <span class="col-sm-3">파일 크기</span>
      <span class="col-sm-3">파일 이름</span>
      <span class="col-sm-3">날짜</span>
      <span class="col-sm-1">미리보기</span>
    </div><br><hr>
  </div>
</div>
<hr>
<h2 style="text-align:center">파일업로드</h2>
<div class="container">
  <form method="post" action="/files" enctype="multipart/form-data">
    <input type="file" name="file">
    <input type="submit" value="등록">
  </form>
</div>
<br>
<br>
<br>
<br>
<br>
<br>

</body>
<script src="/resources/node_modules/jquery/dist/jquery.min.js"></script>
<script src="/resources/node_modules/moment/moment.js"></script>
<script src="/resources/js/fileview.js"></script>
</html>
