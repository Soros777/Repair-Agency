<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="p" value="${param.p}"/>

<!DOCTYPE html>

<head>
  <%@ include file="/jspf/headAuthorized.jspf"%>
</head>
<body class="grey lighten-3">
  <header>
    <%@ include file="/jspf/headerAuth.jspf"%>

    <%@ include file="/jspf/leftBarAuth.jspf"%>

  </header>
  <main class="pt-5 max-lg-5">
    <div class="container-fluid mt-5">

      <%@ include file="/jspf/searchAuth.jspf"%>

      <c:if test="${p eq 'clientMain'}">
        <%@ include file="/jspf/grafics.jspf"%>
      </c:if>

      <c:if test="${p eq 'create'}">
        <%@ include file="/jspf/createOrderCard.jspf"%>
      </c:if>

    </div>
  </main>
  <%@ include file="/jspf/footerAuth.jspf"%>

  <%@ include file="/jspf/scriptsAuth.jspf"%>
</body>
</html>
