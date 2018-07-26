<%@ page import="io.keikai.tutorial.Configuration" %>
<span>${empty param.server? Configuration.DEFAULT_KEIKAI_SERVER : 'http://'.concat(param.server)}</span>
