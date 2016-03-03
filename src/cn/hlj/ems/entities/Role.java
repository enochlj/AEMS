package cn.hlj.ems.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Role {

	// id
	private Integer roleId;
	// 角色名
	private String roleName;

	// 角色拥有的权限集合
	private Set<Authority> authorities = new HashSet<>();

	public Role() {
	}

	public Role(Integer roleId) {
		super();
		this.roleId = roleId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void setAuthorities2(String[] authorities) {
		for (String authId : authorities) {
			this.authorities.add(new Authority(Integer.parseInt(authId)));
		}
	}

	public List<String> getAuthorities2() {
		List<String> ids = new ArrayList<>();

		for (Authority authority : authorities) {
			ids.add("" + authority.getId());
		}

		return ids;
	}

	/*
	 * 辅助方法:获取角色的所有父权限名称
	 */
	public List<String> getSuperAuthorityNames() {
		HashSet<String> set = new HashSet<>();
		for (Authority authority : this.getAuthorities()) {
			set.add(authority.getParentAuthority().getDisplayName());
		}
		return new ArrayList<>(set);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}

}
