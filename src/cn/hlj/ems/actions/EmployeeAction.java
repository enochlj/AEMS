package cn.hlj.ems.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.WebUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import cn.hlj.ems.entities.Authority;
import cn.hlj.ems.entities.Employee;
import cn.hlj.ems.entities.Resource;
import cn.hlj.ems.entities.Role;
import cn.hlj.ems.orm.Page;
import cn.hlj.ems.security.EmsUserDetailsService.SecurityUser;
import cn.hlj.ems.services.DepartmentService;
import cn.hlj.ems.services.EmployeeService;
import cn.hlj.ems.services.RoleService;
import cn.hlj.ems.util.EmployeeCriteriaFormBean;
import cn.hlj.ems.util.Navigation;

@Controller
@Scope("prototype")
public class EmployeeAction extends ActionSupport
		implements ModelDriven<Employee>, Preparable, SessionAware, RequestAware {

	private static final long serialVersionUID = 1L;

	private Employee model;

	@Autowired
	private EmployeeService employeeService;

	private Map<String, Object> session;

	private int pageNo;

	private Page<Employee> page;

	// 每一个 Action 实际上都会有接受 id 的任务. 所以 id 作为成员变量
	private Integer id;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private RoleService roleService;

	private Map<String, Object> request;

	// 导航菜单相关的属性
	private List<Navigation> navigations = null;

	// ----------------------------------------文件下载-------------------------------------------------

	// 输入流
	private InputStream inputStream;

	// 文件下载的类型
	private String contentType;

	// 下载的文件的长度
	private long contentLength;

	// 下载的文件的文件名
	private String fileName;

	// ----------------------------------------文件下载-------------------------------------------------

	// ----------------------------------------文件上传-------------------------------------------------
	private File file;
	// ----------------------------------------文件上传-------------------------------------------------

	// ==============================================================================================

	public void prepareLogin() {
		this.model = new Employee();
	}

	/*
	 * public String login() { String loginName = model.getLoginName(); String
	 * password = model.getPassword(); // 根据用户填写的用户名和密码获取Employee对象 Employee
	 * employee = employeeService.login(loginName, password);
	 * session.put("employee", employee);
	 * 
	 * return SUCCESS; }
	 */

	public String login() throws Exception {

		Object exception = this.request.get("exception");
		if (exception != null && exception instanceof Exception) {
			throw (Exception) exception;
		}

		// String loginName = model.getLoginName();
		// Employee employee = employeeService.getByLoginName(loginName);

		/*
		 * 另一种获取Employee对象的思路: > 在 EmsUserDetailsService#loadUserByUsername
		 * 方法中已经获取过 Employee 对象了. 可以使 EmsUserDetailsService#loadUserByUsername
		 * 方法返回 User 的一个子类, 其中包含 Employee. > 在 EmployeeAction 中可以通过
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		 * ; 方法得到 EmsUserDetailsService#loadUserByUsername 方法的返回值, 进而可以得到
		 * Employee
		 */

		// 在做 动态返回 JSON 数据, 以生成导航菜单时，发现在 EmsUserDetailsService 里对所有的 Role,
		// Role 的所有 Authority, Authority 的parentAuthority 和 mainResource
		// 初始化的操作并不能对通过employeeService.getByLoginName(loginName)
		// 获取到的Employee对象生效;因此改用第二种思路获取Employee对象
		SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Employee employee = securityUser.getEmployee();
		this.session.put("employee", employee);

		employeeService.updateVisitedTimes(employee.getEmployeeId());

		return SUCCESS;
	}

	public String list() {
		page = employeeService.getPage(pageNo);
		return "list";
	}

	public String list2() {
		page = employeeService.getPage(pageNo);
		return "list2";
	}

	public String delete() {
		int result = employeeService.delete(id);
		if (result == 0) {
			return null;
		} else {
			return list2();
		}
	}

	public void prepareInput() {
		if (id != null) {
			this.model = employeeService.get(id);
		}
	}

	public String input() {
		request.put("roles", roleService.getAll(true));
		request.put("departments", departmentService.getAll(true));
		return "emp-input";
	}

	public void prepareSave() {
		if (id == null) {
			this.model = new Employee();
		} else {
			this.model = employeeService.get(id);
			this.model.getRoles().clear();
		}
	}

	public String save() {
		employeeService.save(this.model);
		return "success";
	}

	public void validateLoginName() throws IOException {
		String result = "1";

		String loginName = ServletActionContext.getRequest().getParameter("loginName");
		Employee employee = employeeService.getBy("loginName", loginName);
		if (employee != null) {
			result = "0";
		}

		ServletActionContext.getResponse().getWriter().print(result);
	}

	public String download() throws IOException {
		// 指定文件下载相关的成员变量的值
		String tempFileName = System.currentTimeMillis() + ".xls";
		tempFileName = ServletActionContext.getServletContext().getRealPath("/files/" + tempFileName);

		// 将tempFileName放到请求域中，方便删除临时文件
		request.put("tempFileName", tempFileName);

		employeeService.downloadFile(tempFileName);

		inputStream = new FileInputStream(tempFileName);
		contentType = "application/vnd.ms-excel";
		contentLength = inputStream.available();
		fileName = "employees.xls";

		return "download-success";
	}

	public String downloadExcelTemplate() throws IOException {
		inputStream = ServletActionContext.getServletContext().getResourceAsStream("/files/template.xls");
		contentType = "application/vnd.ms-excel";
		contentLength = inputStream.available();
		fileName = "template.xls";

		return "download-success";
	}

	public String upload() throws Exception {
		// 返回的结果类似于如下: [{"2","4"},{"5","3"}]，指的是出错的位置，第几行第几列
		List<String[]> positions = employeeService.upload(file);

		if (positions != null && positions.size() > 0) {
			// 有错误
			addActionError(getText("errors.emp.uploadTop"));
			for (String[] position : positions) {
				String error = getText("errors.emp.upload", position);
				addActionError(error);
			}
		} else {
			// 没有错误，提示上传成功
			addActionError(getText("message.emp.upload"));
		}

		return "input";
	}

	public String navigate() {
		String contextPath = ServletActionContext.getServletContext().getContextPath();

		navigations = new ArrayList<>();

		// 创建一级菜单
		Navigation top = new Navigation(Integer.MAX_VALUE, "WSY教育办公管理系统", null);
		navigations.add(top);

		// 从session域中获取Employee对象，根据Employee对象的权限动态的创建菜单
		Employee employee = (Employee) session.get("employee");
		Map<Integer, Navigation> parentNavigations = new HashMap<>();

		// 1.获取用户所有Role
		for (Role role : employee.getRoles()) {
			// 2. 再获取用户的所有的 Authority. 注意: mainResource 属性不为 null 的, 才需要作为导航菜单
			for (Authority authority : role.getAuthorities()) {
				Resource resource = authority.getMainResource();
				if (resource == null) {
					continue;
				}

				// 3. 还需要获取权限的父权限, 来生成 Navigation. 再把具体的子权限作为父权限的 children
				Authority parentAuthority = authority.getParentAuthority();
				// 二级菜单，根据父权限创建
				Navigation parentNavigation = parentNavigations.get(parentAuthority.getId());
				if (parentNavigation == null) {
					parentNavigation = new Navigation(parentAuthority.getId(), parentAuthority.getDisplayName(), null);
					parentNavigation.setState("closed");

					// 将二级菜单加入到一级菜单下
					top.getChildren().add(parentNavigation);
					parentNavigations.put(parentAuthority.getId(), parentNavigation);
				}

				// 二级菜单下的选项
				Navigation navigation = new Navigation(authority.getId(), authority.getDisplayName(),
						contextPath + resource.getUrl());
				// 将选项添加到二级菜单下
				parentNavigation.getChildren().add(navigation);
			}
		}

		// 创建"登出"选项，它在一级菜单下，与二级菜单平行
		Navigation logout = new Navigation(Integer.MAX_VALUE - 1, "登出", contextPath + "/security-logout");
		// 将"登出"添加到一级菜单下
		top.getChildren().add(logout);

		return "navigate-success";
	}

	public void prepareCriteriaInput() {
		ActionContext.getContext().getValueStack().push(new EmployeeCriteriaFormBean());
	}

	public String criteriaInput() {
		// request.put("roles", roleService.getAll(true));
		request.put("departments", departmentService.getAll(true));

		return "criteriaInput";
	}

	public String list3() {
		// 获取查询条件对应的请求参数: 他们都是以 filter_ 开头的.
		// Spring 的 WebUtils 的 getParametersStartingWith 方法可以获取以指定前缀开头的请求参数
		// 结果的 key 为去了前缀的请求参数的参数名, value 为参数值
		HttpServletRequest req = ServletActionContext.getRequest();
		Map<String, Object> params = WebUtils.getParametersStartingWith(req, "filter_");

		// 将请求参数反序列化为一个查询字符串
		String queryString = encodeParamsToQueryString(params);
		// 将查询字符串传回到到页面上
		request.put("queryString", queryString);

		page = employeeService.getPage(pageNo, params);
		return "list3";
	}

	private String encodeParamsToQueryString(Map<String, Object> params) {
		StringBuilder queryString = new StringBuilder();

		for (Map.Entry<String, Object> me : params.entrySet()) {
			String key = me.getKey();
			Object value = me.getValue();

			if ("".equals(value)) {
				continue;
			}

			queryString.append("filter_").append(key).append("=").append(value).append("&");
		}

		if (queryString.length() > 0) {
			queryString.replace(queryString.length() - 1, queryString.length(), "");
		}

		return queryString.toString();
	}

	// ==============================================================================================

	@Override
	public Employee getModel() {
		return this.model;
	}

	@Override
	public void prepare() throws Exception {

	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = 1;
		try {
			this.pageNo = Integer.parseInt(pageNo);
		} catch (NumberFormatException e) {
		}
	}

	public Page<Employee> getPage() {
		return page;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	// ------------------------------------------文件下载-----------------------------------------------

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getContentType() {
		return contentType;
	}

	public long getContentLength() {
		return contentLength;
	}

	public String getContentDisposition() {
		return "attachment;filename=" + fileName;
	}

	// ------------------------------------------文件下载-----------------------------------------------

	// ------------------------------------------文件上传-----------------------------------------------

	public void setFile(File file) {
		this.file = file;
	}

	// ------------------------------------------文件上传-----------------------------------------------

	// ------------------------------------------导航菜单-----------------------------------------------

	public List<Navigation> getNavigations() {
		return navigations;
	}

	// ------------------------------------------导航菜单-----------------------------------------------
}
