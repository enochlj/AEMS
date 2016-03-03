<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${ctp }/css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }/css/input.css">
</head>
<body>
	<br>

	<s:form cssClass="employeeForm" id="employeeForm">
		<fieldset>
			<p>
				<label for="message"><font color="red">角色的详细信息</font></label>
			</p>
			<p>
				<label for="loginname">角色名</label>${roleName }
			</p>

			<s:iterator value="#request.details">
				<p>
					<label for="message"><s:property value="key"/></label>
				</p>
				
				<s:iterator value="value">
					<p><label for="message">&nbsp;</label><s:property/></p>
				</s:iterator>
				
			</s:iterator>

		</fieldset>

	</s:form>
</body>
</html>