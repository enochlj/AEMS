<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" type="text/css" media="screen"
	href="${ctp}/css/global.css" />
<link rel="stylesheet" type="text/css"
	href="${ctp}/css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }/script/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctp }/script/themes/icon.css">
<script type="text/javascript" src="${ctp }/script/jquery.min.js"></script>
<script type="text/javascript" src="${ctp }/script/jquery.easyui.min.js"></script>
<script type="text/javascript">
	$(function(){
		$('#navigate').tree({
			onClick: function(node){
				if(node.url!="${ctp}/security-logout"){
					//避免点击父菜单出现异常页面(因为父菜单的url为null)
					if(node.url==null) {
						return false;
					}
					window.parent.document.getElementById("content").src = node.url;
				} else {
					window.parent.parent.location.href=node.url;
				}
			}
		});
	})
</script>

</head>
<body>
	
	<br>
	<table cellpadding=0 cellspacing=0>
      <tr>
      	<td>
      		&nbsp;&nbsp;&nbsp;
      	</td>
        <td>
        	<!--  
        	<ul id="navigate" class="easyui-tree" data-options="url:'${ctp }/commons/tree_data1.json?abc=1',method:'get',animate:true" />
        	-->
        	<!-- 动态的获取导航菜单 -->
        	<ul id="navigate" class="easyui-tree" 
        		data-options="url:'${ctp }/emp-navigate',method:'get',animate:true" />
        </td>
      </tr>
    </table>
	
</body>
</html>