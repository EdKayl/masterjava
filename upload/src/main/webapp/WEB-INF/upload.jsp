<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload</title>
</head>
<body>
<form action="/upload/" method="POST" enctype="multipart/form-data">
    <input name="description" type="text"><br>
    <input name="data" type="file"><br>
    <input type="submit"><br>
</form>
</body>
</html>