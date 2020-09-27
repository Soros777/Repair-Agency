<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Страница успешного входа в систему</title>
</head>
<body>
<br>
<h1>Вход посетителя в систему прошел успешно</h1>
<jsp:useBean id="client" class="ua.epam.finalproject.repairagency.model.Client" scope="session"/>
<p>
    Клиент: <%= client.getClientName()%>
</p>
<p>
    Email: <%= client.getEmail()%>
</p>
</body>
</html>
