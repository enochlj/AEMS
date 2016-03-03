<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ include file="/commons/common.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" href="${ctp }/css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }/css/list.css">
<link rel="stylesheet" type="text/css"
	href="${ctp }/script/thickbox/thickbox.css">
<script type="text/javascript" src="${ctp }/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp }/script/thickbox/thickbox.js"></script>

</head>
<body>
	<input type="hidden" id="pageNo" value="${page.pageNo}" />
	<br>
	<br>
	<center>
		<br> <br> 
		<a id="criteria" href="${ctp}/emp-criteriaInput?height=300&width=320&time=new Date()&${queryString}"
			class="thickbox"> 增加(显示当前)查询条件 </a> 
		<a href="" id="delete-query-condition"> 删除查询条件 </a> 
		
		<span class="pagebanner">
			共 ${page.totalElements } 条记录 &nbsp;&nbsp; 共 ${page.totalPages } 页
			&nbsp;&nbsp; 当前第 ${page.pageNo } 页 
		</span> 
		
		<span class="pagelinks"> 
		
			<span id="prevPageLinks"> [ <a href="emp-list3?pageNo=1&${queryString}">首页</a> 
				<input type="hidden" value="1" /> / 
					
				<a href="emp-list3?pageNo=${page.prev }&${queryString}">上一页</a> 
				<input type="hidden" value="${page.prev}" id="prevPageNoVal" /> ]
			</span> 
			
			<span id="pagelist"> 
				转到 <input type="text" name="pageNo" size="1" height="1" class="logintxt" /> 页
			</span> 
			
			<span id="nextPageLinks"> 
				[ <a href="emp-list3?pageNo=${page.next }&${queryString}">下一页</a> 
				<input type="hidden" value="${page.next}" id="nextPageNoVal" /> / 
				
				<a href="emp-list3?pageNo=${page.totalPages }&${queryString}">末页</a> 
				<input type="hidden" value="${page.totalPages }" /> ]
			</span>
		
		</span>

		<table>
			<thead>
				<tr>
					<td><a id="loginname" href="">登录名</a></td>
					<td>姓名</td>

					<td>登录许可</td>
					<td>部门</td>

					<td>生日</td>
					<td>性别</td>

					<td><a id="email" href="">E-Mail</a></td>
					<td>手机</td>

					<td>登录次数</td>
					<td>删除</td>

					<td>角色</td>
					
					<security:authorize ifAnyGranted="ROLE_EMP_DELETE,ROLE_EMP_UPDATE">
					<td>操作</td>
					</security:authorize>
				</tr>
			</thead>

			<tbody>
				<s:iterator value="page.content">
					<tr>
						<td><a id="loginname" href="">${loginName }</a></td>
						<td>${employeeName }</td>

						<td>${enabled == 1 ? '允许':'禁止' }</td>
						<td>${department.departmentName }</td>

						<td><s:date name="birth" format="yyyy-MM-dd" /></td>
						<td>${gender == 1 ? '男':'女' }</td>

						<td><a id="email" href="">${email }</a></td>
						<td></td>

						<td>${visitedTimes }</td>
						<td>${isDeleted == 1 ? '删除':'正常' }</td>

						<td>${roleNames }</td>
						
						<security:authorize ifAnyGranted="ROLE_EMP_DELETE,ROLE_EMP_UPDATE">
						<td>
							<security:authorize ifAllGranted="ROLE_EMP_UPDATE">
							<a href="emp-input?id=${employeeId }">修改</a> &nbsp;
							</security:authorize>
							
							<security:authorize ifAllGranted="ROLE_EMP_DELETE">
							<s:if test="isDeleted == 1">删除</s:if> 
							<s:else>
								<a class="delete" href="emp-delete?id=${employeeId }">删除</a>
								<input type="hidden" value="${loginName}" />
							</s:else>
							</security:authorize> 
						</td>
						</security:authorize>
						
					</tr>
				</s:iterator>
			</tbody>
		</table>

		<a href="${ctp}/emp-download">下载到Excel 中</a> &nbsp;&nbsp;&nbsp;&nbsp;

	</center>

</body>
</html>