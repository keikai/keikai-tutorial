<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link rel='stylesheet' href='${pageContext.request.contextPath}/css/tutorial.css'/>
    <script id="keikai" async defer src="${keikaiJs}"></script>
    <title>Keikai App</title>
</head>
<body>
    <div class="myapp">
        <div class="header">
            <image src="images/keikai-logo.png" style="height: 45px; display: inline-block" />
            <%@ include file="server-address.jsp" %>
        </div>
        <div id="spreadsheet" data-show-toolbar="false" style="height: 90%" >
        </div>
    </div>
</body>
</html>
