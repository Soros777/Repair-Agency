<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error page ${pageContext.errorData.statusCode}</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css">
</head>
<body class="err-page">
<header class="err-container">
    <section class="err-content">
        <h2>OOPS, something wrong</h2>
        <h1><strong>ERROR ${pageContext.errorData.statusCode}</strong></h1>
        <h4>
            Exception: ${pageContext.exception}
        </h4>
    </section>
</header>

</body>
</html>