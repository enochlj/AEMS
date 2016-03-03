package cn.hlj.ems.actions;

import java.io.IOException;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import cn.hlj.ems.entities.Role;
import cn.hlj.ems.services.AuthorityService;
import cn.hlj.ems.services.RoleService;

@Controller
@Scope("prototype")
public class RoleAction extends ActionSupport implements RequestAware, Preparable, ModelDriven<Role> {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AuthorityService authorityService;

	private Map<String, Object> request;

	private Role model;

	private Integer roleId;

	@Autowired
	private RoleService roleService;

	public void prepareInput() {
		if (this.roleId != null) {
			// 跳转到修改页面回显信息
			this.model = roleService.getBy("roleId",this.roleId);
		}
	}

	public String input() {
		this.request.put("parentAuthorities", authorityService.getByIsNull("parentAuthority"));
		this.request.put("subAuthorities", authorityService.getByIsNotNull("parentAuthority"));

		return "input";
	}

	public void prepareSave() {

		if (this.roleId != null) {
			// 修改角色
			this.model = roleService.getBy("roleId",roleId);
			// 若出现 Duplicate... 异常，重写Authority类的hashcode和equals方法解决
			// 修改权限后再查看数据库，发现角色对应的权限只增不减，可通过clear解决
			this.model.getAuthorities().clear();
		} else {
			// 添加角色
			this.model = new Role();
		}

	}

	public String save() {
		roleService.save(model);

		// 根据操作是添加还是修改来设置提示消息
		if (this.roleId != null) {
			addActionError(getText("message.role.update"));
		} else {
			addActionError(getText("message.role.add"));
		}

		return input();
	}

	public String list() {
		this.request.put("roles", roleService.getAll());
		return "list";
	}

	public String details() {
		String roleName = roleService.getBy("roleId",this.roleId).getRoleName();
		this.request.put("roleName", roleName);
		this.request.put("details", roleService.getDetails(this.roleId));
		return "details";
	}

	public void delete() throws IOException {
		int result = roleService.delete(this.roleId);
		ServletActionContext.getResponse().getWriter().print(result);
	}

	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;
	}

	@Override
	public Role getModel() {
		return this.model;
	}

	@Override
	public void prepare() throws Exception {

	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
