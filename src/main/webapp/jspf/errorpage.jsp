<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="code" value="${requestScope['javax.servlet.error.status_code']}"/>
<c:set var="message" value="${requestScope['javax.servlet.error.message']}"/>
<c:set var="exception" value="${requestScope['javax.servlet.error.exception']}"/>

<!DOCTYPE html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error page ${code}</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body class="err-page">
<header class="err-container">
    <section class="err-content">
        <h2>OOPS, something wrong</h2>
        <h1><strong>ERROR ${code}</strong></h1>
        <c:if test="${not empty message}">
            <h4>
                Message:
                ${message}
            </h4>
        </c:if>
        <c:if test="${not empty exception}">
            <h4>${exception}</h4>
        </c:if>
        <c:if test="${not empty requestScope.errorMessage}">
            <h4>${requestScope.errorMessage}</h4>
        </c:if>
    </section>
</header>

</body>
</html>