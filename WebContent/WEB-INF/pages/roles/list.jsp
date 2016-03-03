<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${ctp }/css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }/css/list.css">
<script type="text/javascript" src="${ctp }/script/jquery-1.3.1.js"></script>
<script type="text/javascript">
	$(function() {
		$(".role-delete").click(function() {
			var url=this.href;
			
			var roleId=this.id;
			var args={
				"date":new Date(),
			};
			
			$.post(url,args,function(data) {
				if("1"==data) {
					//删除成功
					$("tr[id='"+roleId+"']").remove();
					alert("删除成功!");
				} else {
					//删除失败
					alert("角色被引用,删除失败!");
				}
			});
			
			return false;
		});
	});
</script>
</head>
<body>

	<br><br>
	<center>
		<table>
			<thead>
				<tr>
					<th>角色名称</th>
					<th>授予的权限</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				
				<s:iterator value="#request.roles">
					<tr id="${roleId }">
						<td>
							<a href="role-details?roleId=${roleId }">${roleName }</a>						
						</td>
						<td>
							<s:iterator value="superAuthorityNames" >
								<s:property />
							</s:iterator>
						</td>
						
						<td>
						<a href="role-delete?roleId=${roleId }" id="${roleId }" name="${roleName }" class="role-delete">删除</a>
						<a href="role-input?roleId=${roleId }">修改</a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</center>
</body>
</html>