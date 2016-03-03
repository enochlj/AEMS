<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>  
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${ctp}/css/content.css">
<link rel="stylesheet" type="text/css" href="${ctp}/css/input.css">
<link rel="stylesheet" type="text/css" href="${ctp}/css/weebox.css">
 
<link rel="stylesheet" type="text/css" href="${ctp}/script/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctp}/script/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctp}/css/default.css">

<script type="text/javascript" src="${ctp}/script/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="${ctp}/script/jquery.validate.js"></script>

<script type="text/javascript" src="${ctp}/script/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctp}/script/locale/easyui-lang-zh_CN.js"></script>

<script type="text/javascript" src="${ctp}/script/messages_cn.js"></script>
<script type="text/javascript" src="${ctp}/script/bgiframe.js"></script>
<script type="text/javascript" src="${ctp}/script/weebox.js"></script>
<script type="text/javascript">

	function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}
	
	function myparser(s){
		if (!s) return new Date();
		var ss = (s.split('-'));
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	}
	
	$(function() {
		$("#role_a_id").click(function(){
			//可以使用jQuery的选择器为弹窗提供内容
			$.weeboxs.open('#rolebox', {
				title:'分配角色',
				//有name属性的checkbox，用来保存选中的状态;
				//没有name属性的checkbox，仅用来显示选中的状态
				//weebox窗口打开时触发的事件
				onopen:function() {
					//读取选中的状态
					$(":checkbox[name='roles2']").each(function(index) {
						var checked=$(this).is(":checked");
						$($(":checkbox[name!='roles2']")[index]).attr("checked",checked);
					});
				},
				//点击确定按钮时触发的事件
				onok:function(box){
					//保存选中的状态
					//jQuery 的 each 函数中如果加入 index, 则 index 表示索引
					$(":checkbox[name!='roles2']").each(function(index) {
						var checked=$(this).is(":checked");
						$($(":checkbox[name='roles2']")[index]).attr("checked",checked);
					});
					box.close();
				}
			});
			
			return false;
		});
		
		//针对loginName完成jQuery的validate验证
		$("#employeeForm").validate();
		
		//loginName的Ajax校验
		$("#loginName").change(function() {
			var val=this.value;
			val=$.trim(val);
			this.value=val;
			
			if(val=="") {
				alert("loginName不能为空!");
				return;
			}
			
			if(val.length<6) {
				alert("loginName的长度不能小于6!");
				return;
			}
			
			var reg=/^[a-zA-Z]\w+\w$/g;
			if(!reg.test(val)) {
				alert("loginName不合法!");
				return;
			}
			
			var oldLoginName=$("#oldLoginName").val();
			if(val==oldLoginName) {
				return;
			}
			
			var url="${ctp}/emp-validateLoginName";
			var args={"loginName":val,"time":new Date()};
			$.post(url,args,function(data) {
				if(data=="1") {
					alert("loginName可用!");
				} else if(data=="0") {
					alert("loginName不可用!");
				} else {
					alert("请重试!");
				}
			});
		});
		
	});
</script>
</head>

<body>

	<br>
	<s:form id="employeeForm" cssClass="employeeForm" action="/emp-save">
		<s:if test="employeeId!=null"><!-- 修改操作 -->
			<input type="hidden" name="oldLoginName" id="oldLoginName" 
				value="${param.oldLoginName==null?requestScope.loginName:param.oldLoginName }" />
			<input type="hidden" name="id" value="${employeeId }"/>
		</s:if>
	
		<fieldset>
		
			<p>
				<label for="message">
						<font color="red">添加员工信息</font>
				</label> 
			</p>
			
			<p>
				<label for="loginName">登录名(必填)</label>
				<s:textfield id="loginName" name="loginName" cssClass="required" minlength="6" />
				<label id="loginlabel" class="error" for="loginname" generated="true">
					<!-- 显示服务器端简单验证的信息 -->
				</label>
			</p>
			
			<p>
				<label for="employeeName">姓名 (必填)</label>
				<s:textfield name="employeeName" />
			</p>
			
			<p>
				<label for="logingallow">登录许可 (必填)</label>
				<s:radio list="#{'1':'允许','0':'禁止'}" name="enabled" cssStyle="border:none" />
			</p>

			<p>
				<label for="gender">性别 (必填)</label>
				<s:radio list="#{'1':'男','0':'女' }" name="gender" cssStyle="border:none" />
			</p>
			
			<p>
				<label for="dept">部门 (必填)</label>
				<s:select list="#request.departments" listKey="departmentId" listValue="departmentName" name="department.departmentId" />

				<label class="error" for="dept" generated="true">
					<font color="red">
					<!-- 显示服务器端简单验证的信息 -->
					</font>
				</label>
			</p>
			
			<p>
				<label for="birth">生日 (必填)</label>
				<s:textfield name="birth" cssClass="easyui-datebox" data-options="formatter:myformatter,parser:myparser" />
			</p>
			
			<p>
				<label for="email">Email (必填)</label>
				<s:textfield name="email" />
				<label class="error" for="email" generated="true">
					<!-- 显示服务器端简单验证的信息 -->
					<s:fielderror fieldName="email" />
				</label>
			</p>
			
			<p>
				<label for="mobilePhone">电话 (必填)</label>
				<s:textfield name="mobilePhone" />
			</p>

			<p>
				<label for="role"><a id="role_a_id" href="">分配角色(必选)</a></label>
			</p>
			
			<div style="display:none">
				<!-- 有name属性的checkbox，用来保存选中的状态 -->
				<s:checkboxlist list="#request.roles" listKey="roleId" listValue="roleName" name="roles2" />
			</div>
			
			<div id="rolebox" style="display:none">
				<!-- 没有name属性的checkbox，仅用来显示选中的状态 -->
				<s:iterator value="#request.roles">
					<input type="checkbox" value="${roleId }" />${roleName }<br>
				</s:iterator>
			</div>
			
			<p>
				<label for="mobilePhone">创建人</label>
				<s:if test="employeeId==null"><!-- 添加操作 -->
					${employee.loginName }				
					<input type="hidden" value="${sessionScope.employee.employeeId }" name="editor.employeeId"/>
				</s:if>
				<s:else><!-- 修改操作 -->
					${editor.loginName }
					<input type="hidden" value="${editor.employeeId }" name="editor.employeeId"/>
				</s:else>
			</p>

			<p>
				<input class="submit" type="submit" value="提交"/>
			</p>
				
		</fieldset>
	</s:form>

</body>
</html>