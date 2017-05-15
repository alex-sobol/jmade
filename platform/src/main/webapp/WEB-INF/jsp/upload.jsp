<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload agent</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</head>
<body>
<h2 style="text-align: center; width: 100%; padding: 20px">Upload agent</h2>

<div>
    <form action="\agent\upload" method="post" enctype="multipart/form-data">
        <div>
            <input id="file" name="file" type="file"/>
        </div>
        <input type="submit" value="Upload"/>
    </form>
</div>
</body>
</html>
