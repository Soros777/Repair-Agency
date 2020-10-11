<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<head>

    <title>Ремонтное агенство "MAX Service"</title>
    <%@ include file="jspf/head.jspf" %>

</head>
<body>
<div class="wrapper">

    <%@ include file="jspf/navbar.jspf" %>

    <%@ include file="jspf/baner.jspf" %>

    <div class="container w">

        <%@ include file="jspf/firstWorkArea.jspf" %>

        <%@ include file="jspf/secondWorkArea.jspf" %>

    </div>

    <%@ include file="jspf/weAre.jspf" %>

    <%@ include file="jspf/partners.jspf" %>

    <%@ include file="jspf/footer.jspf" %>

</div>

<%@ include file="/jspf/loginWindow.jspf" %>

<%@ include file="jspf/registerWindow.jspf" %>


<%@ include file="jspf/scripts.jspf" %>

</body>
</html>