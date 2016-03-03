<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to the Central Administration System</title>
<link rel="stylesheet" type="text/css" href="${ctp }/css/login.css">
<script type="text/javascript" src="${ctp }/script/jquery.min.js"></script>
<script type="text/javascript">
	/*
	* 前端基于 JS 的验证.
	* 
	* ①: loginName 和 password 字段除去前后空格不能为空
	* ②: loginName 和 password 字段除去前后空格, 不能少于 6 个字符
	* ③: loginName 中不能包含特殊字符, 即以字母开头, 后边还可以包含数字和_ 
	*
	* 其中前两个验证都通过, 再验证 ③. 
	*/
	/* $(function() {
		$(".submit").click(function() {
			var flag=true;
			
			$("input:not(.submit)").each(function() {
				var val=$(this).val();
				val=$.trim(val);
				$(this).val(val);
				
				//使密码不回显
				var name=$(this).attr("name");
				if(name=="password") {
					$(this).val("");
				}
				
				//获取label
				var label=$(this).prev("b").text();
				
				//先判断loginName 和 password 字段除去前后空格是否为空
				//再判断 loginName 和 password 字段除去前后空格的长度是否少于 6 个字符
				if(val=="") {
					alert(label+"不能为空!");
					flag=false;
				} else if(val.length<6) {
					alert(label+"不能少于6个字符");
					flag=false;
				}
			});
			
			//loginName 中不能包含特殊字符, 即以字母开头, 后边还可以包含数字和_ 
			if(flag) {
				var loginName=$("input[name='loginName']").val();
				
				var regex=/^[a-zA-Z]\w*\w$/g;
				if(!reg.test(loginName)) {
					alert("用户名不合法!");
					flag=false;
				}
			}
			//取消点击的默认行为
			return flag;
		}); 
	}); */
</script>
</head>
<body>

	<div align="center">
		<s:form action="/security-login">
			<div class="login_div" align="center">
				<font color="red">
					<s:text name="%{exception.class.name}" />
				</font>
				
				<b>用户名</b>
				<s:textfield name="loginName" />
				<font color="red">
					<s:fielderror fieldName="loginName" />
				</font>

				<b>密码</b>
				<s:password name="password" />
				<font color="red">
					<s:fielderror fieldName="password" />
				</font>
				
				<input type="submit" class="submit" value="登录" />
				
			</div>		
		</s:form>
	</div>
</body>
</html>