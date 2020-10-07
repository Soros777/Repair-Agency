<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="p" value="${param.p}"/>

<!DOCTYPE html>

<head>
  <%@ include file="/WEB-INF/jspf/headAuthorized.jspf"%>
</head>
<body class="grey lighten-3">
  <header>
    <%@ include file="/WEB-INF/jspf/headerAuth.jspf"%>

    <%@ include file="/WEB-INF/jspf/leftBarAuth.jspf"%>

  </header>
  <main class="pt-5 max-lg-5">
    <div class="container-fluid mt-5">
      <!--    Heading-->
      <%@ include file="/WEB-INF/jspf/searchAuth.jspf"%>
      <%@ include file="/WEB-INF/jspf/grafics.jspf"%>
      <%@ include file="/WEB-INF/jspf/card.jspf"%>

    </div>
  </main>
  <%@ include file="/WEB-INF/jspf/footerAuth.jspf"%>

  <%@ include file="/WEB-INF/jspf/scriptsAuth.jspf"%>
</body>
</html>
