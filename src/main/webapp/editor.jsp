<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link rel='stylesheet' href='${pageContext.request.contextPath}/webjars/bootstrap/4.1.0/dist/css/bootstrap.min.css'/>
    <link rel='stylesheet' href='${pageContext.request.contextPath}/css/tutorial.css'/>
    <script src='${pageContext.request.contextPath}/webjars/jquery/3.3.1/jquery.min.js'></script>
    <script src='${pageContext.request.contextPath}/webjars/bootstrap/4.1.0/dist/js/bootstrap.bundle.min.js'></script>
    <script id="keikai" async defer src="${keikaiJs}"></script>
    <script src='editor.js'></script>
    <title>Keikai Editor</title>
</head>
<body >
    <div class="container-fluid">
        <div class="header">
            <image src="images/keikai-logo.png" style="height: 45px; display: inline-block" />
            <%@ include file="server-address.jsp" %>
            <div class="alert" role="alert" style="display:none">
            </div>
        </div>
        <div id="spreadsheet" style="height: 90%" >
        </div>
    </div>
</body>
</html>
