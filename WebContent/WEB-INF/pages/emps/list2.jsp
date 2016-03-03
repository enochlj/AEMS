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
<script type="text/javascript">
	$(function() {
		//若当前页面是第一页，当隐藏prevPageLinks
		if ("$(page.pageNo)" == "1") {
			$("#prevPageLinks").hide();
		}

		//若当前页面是最后一页, 则隐藏 nextPageLinks
		if ("$(page.pageNo)" == "$(page.totalPages)") {
			$("#nextPageLinks").hide();
		}

		function updatePage(data) {
			//更新#pageNo
			$("#pageNo").val(data.pageNo);

			//3. 使用 jQuery 和 javascript 更新页面的内容
			//3.1 更新: .pagebanner
			$(".pagebanner").html(
					"共" + data.totalElements + "条记录" + "&nbsp;&nbsp;" + "共"
							+ data.totalPages + "页" + "&nbsp;&nbsp;" + "当前第"
							+ data.pageNo + "页");

			//3.2 更新: .pagelinks
			if (data.pageNo == 1) {
				$("#prevPageLinks").hide();
			} else {
				$("#prevPageLinks").show();
			}

			if (data.pageNo == data.totalPages) {
				$("#nextPageLinks").hide();
			} else {
				$("#nextPageLinks").show();
			}

			//3.3 更新隐藏域中的 value 值
			$("#prevPageNoVal").val(data.prev);
			$("#nextPageNoVal").val(data.next);

			//3.4 更新 table 中 tbody 的内容. 
			var $tbody = $("table tbody");
			$tbody.empty();

			for (var i = 0; i < data.content.length; i++) {
				var item = data.content[i];

				var $tr = $("<tr></tr>");

				$tr.append("<td><a href=''>" + item.loginName + "</a></td>");
				$tr.append("<td>" + item.employeeName + "</td>");

				$tr.append("<td>" + (item.enabled == 1 ? '允许' : '禁止')+ "</td>");
				$tr.append("<td>" + item.departmentName + "</td>");

				$tr.append("<td>" + item.displayBirth + "</td>");
				$tr.append("<td>" + (item.gender == 1 ? '男' : '女') + "</td>");

				$tr.append("<td><a href=''>" + item.email + "</a></td>");
				$tr.append("<td>" + item.mobilePhone + "</td>");

				$tr.append("<td>" + item.visitedTimes + "</td>");
				$tr.append("<td>" + (item.isDeleted == 1 ? '删除' : '正常')
						+ "</td>");

				$tr.append("<td>" + item.roleNames + "</td>");

				<security:authorize ifAnyGranted="ROLE_EMP_DELETE,ROLE_EMP_UPDATE">
				
				var $td = $("<td></td>");
				
				<security:authorize ifAllGranted="ROLE_EMP_UPDATE">
				
				$td.append("<a href='emp-input?id=" + item.employeeId
						+ "'>修改</a>");
				
				</security:authorize>
				
				<security:authorize ifAllGranted="ROLE_EMP_DELETE">
				
				if (item.isDeleted == 1) {
					$td.append("&nbsp;&nbsp;删除");
				} else {
					$td.append("&nbsp;&nbsp;");
					var $deleteANode = $("<a href='emp-delete?id="
							+ item.employeeId + "'>删除</a>");
					$deleteANode.click(function() {
						deleteTr(this);
						return false;
					});
					$td.append($deleteANode);
					$td.append("<input type='hidden' value='"+ item.loginName +"' />");
				}
				
				</security:authorize>
				
				</security:authorize>
				
				$tr.append($td);
				$tbody.append($tr);
			}
		}

		function turnPage(url, args) {
			$.post(url, args, function(data) {
				updatePage(data);
			}, "json");
		}

		$(".pagelinks a").click(function() {
			//1.准备发送Ajax请求需要的信息
			var url = "${ctp}/emp-list2";
			var pageNo = $(this).next(":hidden").val();
			var args = {
				"pageNo" : pageNo,
				"time" : new Date()
			};

			//2.发送Ajax请求
			turnPage(url, args);

			return false;
		});

		//输入页码之后直接分页
		$(".logintxt").change(function() {
			//1.得到value值
			var val = this.value;
			val = $.trim(val);

			//2.对val值进行校验
			var regex = /^\d+$/;
			if (!regex.test(val)) {
				alert("输入的页码不合法!");
				this.value = "";
				return;
			}

			var pageNo = parseInt(val);
			if (pageNo<1 || pageNo>parseInt("$(page.totalPages)")) {
				alert("输入的页码不合法!");
				this.value = "";
				return;
			}

			//3.Ajax分页
			var url = "${ctp}/emp-list2";
			var args = {
				"pageNo" : pageNo,
				"time" : new Date()
			};
			turnPage(url, args);
		});

		function deleteTr(aNode) {
			var loginName = $(aNode).next(":hidden").val();
			var flag = confirm("确定要删除 " + loginName + " 的信息吗?");

			if (!flag) {
				return false;
			}

			var url = aNode.href;
			var pageNo = $("#pageNo").val();
			var args = {
				"date" : new Date(),
				"pageNo" : pageNo
			};
			$.post(url, args, function(data) {
				//如何来判断删除是否成功呢 ? 若成功则返回 page 对应的 JSON, 若失败, 则什么都没有返回
				if (data.pageNo) {
					alert("删除成功!");
					//更新页面
					updatePage(data);
				} else {
					alert("Manager不能被删除!");
				}
			});
		}

		$(".delete").click(function() {
			deleteTr(this);
			return false;
		});

	});
</script>
</head>
<body>
	<input type="hidden" id="pageNo" value="${page.pageNo}" />
	<br>
	<br>
	<center>
		<br> <br> 
		<a id="criteria" href="${ctp}/emp-criteriaInput?height=300&width=320&time=new Date()"
			class="thickbox"> 增加(显示当前)查询条件 </a> 
		<a href="" id="delete-query-condition"> 删除查询条件 </a> 
		
		<span class="pagebanner">
			共 ${page.totalElements } 条记录 &nbsp;&nbsp; 共 ${page.totalPages } 页
			&nbsp;&nbsp; 当前第 ${page.pageNo } 页 
		</span> 
		
		<span class="pagelinks"> 
		
			<span id="prevPageLinks"> [ <a href="emp-list?pageNo=1">首页</a> 
				<input type="hidden" value="1" /> / 
					
				<a href="emp-list?pageNo=${page.prev }">上一页</a> 
				<input type="hidden" value="${page.prev}" id="prevPageNoVal" /> ]
			</span> 
			
			<span id="pagelist"> 
				转到 <input type="text" name="pageNo" size="1" height="1" class="logintxt" /> 页
			</span> 
			
			<span id="nextPageLinks"> 
				[ <a href="emp-list?pageNo=${page.next }">下一页</a> 
				<input type="hidden" value="${page.next}" id="nextPageNoVal" /> / 
				
				<a href="emp-list?pageNo=${page.totalPages }">末页</a> 
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