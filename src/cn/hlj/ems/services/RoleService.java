package cn.hlj.ems.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hlj.ems.daos.EmployeeDao;
import cn.hlj.ems.entities.Authority;
import cn.hlj.ems.entities.Employee;
import cn.hlj.ems.entities.Role;

@Service
public class RoleService extends BaseService<Role> {
	/*@Autowired
	private RoleDao roleDao;*/

	@Autowired
	private EmployeeDao employeeDao;

	/*@Transactional(readOnly = true)
	public List<Role> getAll() {
		return roleDao.getAll(true);
	}

	@Transactional
	public void save(Role role) {
		roleDao.saveOrUpdate(role);
	}

	@Transactional(readOnly = true)
	public Role getBy(Integer roleId) {
		return roleDao.getBy("roleId", roleId);
	}*/

	@Transactional
	public Map<String, List<String>> getDetails(Integer roleId) {
		// 父权限作键，子权限作值
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		// 根据roleId获取相应Role对象
		Role role = baseDao.getBy("roleId", roleId);
		// 获取role的所有权限
		Set<Authority> authorities = role.getAuthorities();

		// 将所有的键添加到map里
		for (Authority authority : authorities) {
			String key = authority.getParentAuthority().getDisplayName();
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<String>());
			}
		}

		// 给map添加值
		for (Authority authority : authorities) {
			String listVal = authority.getDisplayName();
			map.get(authority.getParentAuthority().getDisplayName()).add(listVal);
		}
		return map;
	}

	@Transactional
	public int delete(Integer roleId) {
		Role role = baseDao.getBy("roleId", roleId);

		/*
		 * 先获取所有的Employee : List<Employee>
		 * 遍历List<Employee>-->获取Employee对象的Role集合 : Set<Role>
		 * 判断Set<Role>是否包含指定的Role，如果包含则不能删除
		 */

		List<Employee> empList = employeeDao.getAll();
		for (Employee employee : empList) {
			Set<Role> rolesSet = employee.getRoles();

			if (rolesSet.contains(role)) {
				return 0;
			}
		}

		baseDao.delete(role);
		return 1;
	}
}
