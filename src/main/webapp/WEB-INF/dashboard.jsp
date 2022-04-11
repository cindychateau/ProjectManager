<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Gerente de Proyecto</title>
</head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
<body>

	<div class="container">
		<nav class="d-flex justify-content-between align-items-center">
			<h1>Bienvenid@ <c:out value="${user_session.getFirst_name() }"/></h1>
			<a href="/projects/new" class="btn btn-primary">+ Nuevo Proyecto</a>
			<a href="/logout" class="btn btn-danger">Cerrar Sesión</a>
		</nav>
		
		<div class="row">
			<h2>Todos los Proyectos</h2>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Proyecto</th>
						<th>Líder de Proyecto</th>
						<th>Fecha Límite</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="p_o" items="${resto_proyectos}">
						<tr>
							<td><a href="/projects/${p_o.getId()}">${p_o.getTitle()}</a></td>
							<td>${p_o.getUser().getFirst_name()}</td>
							<td>${p_o.getDue_date() }</td>
							<td>
								<a href="/projects/join/${p_o.getId()}" class="btn btn-warning">Unirse</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="row">
			<h2>Mis Proyectos</h2>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Proyecto</th>
						<th>Líder de Proyecto</th>
						<th>Fecha Límite</th>
						<th>Acciones</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="p_m" items="${mis_proyectos}">
						<tr>
							<td><a href="/projects/${p_m.getId()}">${p_m.getTitle()}</a></td>
							<td>${p_m.getUser().getFirst_name()}</td>
							<td>${p_m.getDue_date()}</td>
							<td>
								
								<c:if test="${p_m.getUser().getId() == user_session.getId()}">
									<a href="/projects/edit/${p_m.getId()}" class="btn btn-warning">Editar</a>
								</c:if>
								
								<c:if test="${p_m.getUser().getId() != user_session.getId()}">
									<a href="/projects/leave/${p_m.getId()}" class="btn btn-danger">Salir</a>
								</c:if>
								
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
	</div>

</body>
</html>