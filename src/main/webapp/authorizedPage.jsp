<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

      <c:if test="${param.tab ne 'order' && param.tab ne 'clientOrders' && param.tab ne 'userProfile'}">
        <%@ include file="/jspf/searchAuth.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'main'}">
        <%@ include file="/jspf/grafics.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'create'}">
        <%@ include file="/jspf/createOrderCard.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'orders'}">
        <%@ include file="/jspf/orders.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'order'}">
        <%@ include file="/jspf/order.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'clientOrders'}">
        <%@ include file="/jspf/clientOrders.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'userProfile'}">
        <%@ include file="/jspf/userProfile.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'topUpClientWallet'}">
        <%@ include file="/jspf/topUpClientWallet.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'notEnoughMoney'}">
        <%@ include file="/jspf/notEnoughMoney.jspf"%>
      </c:if>

      <c:if test="${param.tab eq 'successTopUpWallet'}">
        <%@ include file="/jspf/successTopUpWallet.jspf"%>
      </c:if>

    </div>
  </main>
  <%@ include file="/jspf/footerAuth.jspf"%>

  <%@ include file="/jspf/scriptsAuth.jspf"%>
</body>
</html>
