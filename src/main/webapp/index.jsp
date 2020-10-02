<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<head>

    <title>Ремонтное агенство "MAX Service"</title>
    <%@ include file="/WEB-INF/jspf/head.jspf" %>

</head>
<body>

<div class="wrapper">

    <jsp:include page="/WEB-INF/jspf/navbar.jspf" >

    <%@ include file="/WEB-INF/jspf/baner.jspf" %>

    <div class="container w">

        <%@ include file="/WEB-INF/jspf/firstWorkArea.jspf" %>

        <%@ include file="/WEB-INF/jspf/secondWorkArea.jspf" %>

    </div>

    <%@ include file="/WEB-INF/jspf/weAre.jspf" %>

    <%@ include file="/WEB-INF/jspf/partners.jspf" %>

    <%@ include file="/WEB-INF/jspf/footer.jspf" %>

</div>

<%@ include file="/WEB-INF/jspf/loginWindow.jspf" %>

<%@ include file="/WEB-INF/jspf/registerWindow.jspf" %>


<%@ include file="/WEB-INF/jspf/scripts.jspf" %>

</body>
</html>