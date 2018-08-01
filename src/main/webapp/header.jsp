<%@ page import="io.keikai.tutorial.Configuration" %>
<div class="header">
    <image src="images/keikai-logo.png" style="height: 45px; display: inline-block" />
    <span>${empty param.server? Configuration.DEFAULT_KEIKAI_SERVER : 'http://'.concat(param.server)}</span>
</div>
