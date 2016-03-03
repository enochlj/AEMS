<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>       
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${ctp }/css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp }/css/input.css">
<script type="text/javascript" src="${ctp }/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp }/script/jquery.validate.js"></script>
<script type="text/javascript" src="${ctp }/script/messages_cn.js" ></script>
<script type="text/javascript" src="${ctp }/script/jquery.blockUI.js" ></script>
<script type="text/javascript">
	$(function() {
		$("select[name='parentAuthroities']").change(function() {
			$("p[class^='authority-']").hide();
			
			//获取权限的id值
			var val=this.value;
			//显示该父权限的所有子权限（每个子权限复选框的父标签p都有一个class属性:"authority-${parentAuthority.id }"）
			$(".authority-"+val).show();
		});
		
		$(":checkbox").click(function() {
			/* alert($(this).attr("class"));//,3, */
			
			//获取当前复选框被选中的状态
			var flag=$(this).is(":checked");
			if(flag) {
				//选中某权限，其关联的权限也被选中
				//如"员工删除"权限依赖于"员工查询"权限，若选中"员工删除"，则"员工查询"也应该被选中
				
				var relatedAuthorites=$(this).attr("class");
				var ras=relatedAuthorites.split(",");
				for(var i=0;i<ras.length;i++) {
					var ra=ras[i];
					ra=$.trim(ra);
					
					if(ra!="") {
						//使关联的权限也被选中
						$(":checkbox[value='" + ra +"']").attr("checked",true);
					}
				}
			} else {
				//取消某权限的选中状态，则其他依赖于该权限的权限也应该被取消选中状态
				//如"员工删除"、"员工修改"都依赖于"员工查询"，若取消选中"员工查询"，则"员工删除"、"员工修改"也应该被取消选中
				
				//使 class 值中包含当前 value 值的 checkbox 取消选中.
				//若当前的 val 值为 2, 则 class 属性值包含 2 的或许有如下情况:
				//class=',2,3,4,' , class=',1,12,31,' 类似于 12 的不该被取消. 所以仅包含 2 不行. 
				//应该不包含产生歧义的部分，若class 包含 ,val, (如class属性包含 ,2, ) 就可以消除歧义. 
				var val=this.value;
				$(":checkbox[class*='," + val +",']").attr("checked",false);
			}
			
		});
	});
</script>
</head>
<body>

	<br>
	
	<s:form id="employeeForm" name="employeeForm" action="/role-save" cssClass="employeeForm">
		<s:hidden name="roleId" />
		<fieldset>
			<p>
				<label for="name">角色名(必填*)</label>
				<s:textfield name="roleName" cssClass="required" />
			</p>
			
			<p>
				<label for="authority">授予权限(必选)</label>
			</p>
			
			<p>
				<label>权限名称(必填)</label>
				<!-- 父权限 -->
				<s:select list="#request.parentAuthorities" listKey="id" listValue="displayName" 
						  headerKey="" headerValue="请选择" name="parentAuthroities" />
			</p>
				
			<!-- 子权限以隐藏的 checkbox 的形式显示出来 -->
			<!--  
				1.使用 s:checkboxlist 标签, 修改模版文件. 
				2.使用 s:checkbox 标签, 使用 JS 进行回显
				3.其他办法. 
			-->
			<!-- 
				<s:iterator value="#request.subAuthorities">
					<p style="display:none" class="authority-${parentAuthority.id }">    
						<label>&nbsp;</label>    
						<input type="checkbox" name="authorities2" value="${id }"
							style="border:none" class="${relatedAuthorites }" />
						${displayName }
					</p>
				</s:iterator>
			 -->
			
			<!-- 下面的s:checkboxlist标签的模板文件在ems.template.simple包下，对checkboxlist.ftl文件做了修改，
				 从而达到上面注释中的效果 -->
			<s:checkboxlist list="#request.subAuthorities" listKey="id" listValue="displayName" 
							name="authorities2" templateDir="ems/template" listCssClass="relatedAuthorites" />
								<!-- Authority 有一个字段: relatedAuthorites. 用来存储关联的权限. -->
				
			<p align="center">
				<!-- 显示操作后的提示消息 -->
				<font color="red"><s:actionerror escape="false" /></font>&nbsp;&nbsp;
			</p>
								
			<p>
				<input class="submit" type="submit" value="Submit"/>
			</p>
			
		</fieldset>
		
	</s:form>


</body>
</html>