<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel='stylesheet' href='${pageContext.request.contextPath}/css/tutorial.css'/>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Keikai Editor</title>
</head>
<body style="height: 100%">
    <div class="myapp">
        <%@ include file="../header.jsp" %>
        <div id="spreadsheet">
        </div>
    </div>
    <script src="${keikaiJs}"></script>
</body>
</html>
