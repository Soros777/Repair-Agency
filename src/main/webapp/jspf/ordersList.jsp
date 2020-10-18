<%@ page contentType="text/html; charset=UTF-8"%>

<ul class="nav nav-pills" role="tablist">
    <li class="nav-item">
        <a href="#all" class="nav-link active" role="tab" data-toggle="pill">
            Всего заказов<br/><ctg:orders-info needInfo="all"/>
        </a>
    </li>
    <li class="nav-item">
        <a href="#new" class="nav-link" role="tab" data-toggle="pill">
            Новых<br/><ctg:orders-info needInfo="new"/>
        </a>
    </li>
    <li class="nav-item">
        <a href="#wait" class="nav-link" role="tab" data-toggle="pill">
            Ожидают оплату<br/><ctg:orders-info needInfo="Wait_For_Pay"/>
        </a>
    </li>
    <li class="nav-item">
        <a href="#payed" role="tab" class="nav-link" data-toggle="pill">
            Оплаченные<br/><ctg:orders-info needInfo="Payed"/>
        </a>
    </li>
    <li class="nav-item">
        <a href="#work" class="nav-link" role="tab" data-toggle="pill">
            В работе<br/><ctg:orders-info needInfo="Working"/>
        </a>
    </li>
    <li class="nav-item">
        <a href="#done" class="nav-link" role="tab" data-toggle="pill">
            Готовые<br/><ctg:orders-info needInfo="Made"/>
        </a>
    </li>
    <li class="nav-item">
        <a href="#canceled" class="nav-link" role="tab" data-toggle="pill">
            Отменённые<br/><ctg:orders-info needInfo="Canceled"/>
        </a>
    </li>
</ul>
<div class="tab-content p-3">
    <div role="tabpanel" class="tab-pane active fade show in" id="all">
        <c:set var="orders" value="${allOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="new">
        <c:set var="orders" value="${newOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="wait">
        <c:set var="orders" value="${Wait_For_PayOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="payed">
        <c:set var="orders" value="${PayedOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="work">
        <c:set var="orders" value="${WorkingOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="done">
        <c:set var="orders" value="${MadeOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
    <div role="tabpanel" class="tab-pane fade" id="canceled">
        <c:set var="orders" value="${CanceledOrders}"/>
        <%@ include file="/jspf/ordersTable.jspf" %>
    </div>
</div>
