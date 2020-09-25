<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Страница подтверждения успешной регистрации посетителя</title>
</head>
<body>
<h1>Регистрация посетителя успешно завершена</h1>
<jsp:useBean id="client" class="ua.epam.finalproject.repairagency.model.Client" scope="application"/>
<p>
    Пользователь <%= client.getClientName()%> <br>
    Email: <%= client.getEmail()%> <br>
    Зарегистрирован.
</p>
</body>
</html>
