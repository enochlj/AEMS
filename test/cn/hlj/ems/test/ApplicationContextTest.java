package cn.hlj.ems.test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import cn.hlj.ems.entities.Authority;
import cn.hlj.ems.entities.Employee;
import cn.hlj.ems.entities.Resource;
import cn.hlj.ems.entities.Role;
import cn.hlj.ems.security.FilterInvocationSecurityMetadataSourceRequestMapBuilderImpl;
import cn.hlj.ems.services.DepartmentService;
import cn.hlj.ems.services.EmployeeService;
import cn.hlj.ems.services.ResouceService;
import cn.hlj.ems.services.RoleService;

public class ApplicationContextTest {
	private ApplicationContext ioc = null;
	private EmployeeService employeeService = null;
	private DepartmentService departmentService = null;
	private RoleService roleService;
	private ResouceService resouceService;

	{
		ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		employeeService = ioc.getBean(EmployeeService.class);
		departmentService = ioc.getBean(DepartmentService.class);
		roleService = ioc.getBean(RoleService.class);
		resouceService = ioc.getBean(ResouceService.class);
	}

	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource = ioc.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}

	@Test
	public void testLogin() {
		Employee employee = employeeService.login("testName", "123456");
		System.out.println(employee.getVisitedTimes());
	}

	@Test
	public void testSecondFactory() {
		departmentService.getAll();
		System.out.println("---------------------------------------------------------");
		departmentService.getAll();

		System.out.println("---------------------------------------------------------");
		roleService.getAll();
		System.out.println("---------------------------------------------------------");
		roleService.getAll();
	}

	@Test
	public void testGet() {
		Employee employee = employeeService.get(4);
		System.out.println(employee.getDepartment().getDepartmentId());
	}

	@Test
	public void testRoleServiceGetAll() {
		List<Role> roles = roleService.getAll();
		for (Role role : roles) {
			HashSet<String> set = new HashSet<>();
			System.out.print(role.getRoleId() + "---" + role.getRoleName() + "--- [ ");
			for (Authority authority : role.getAuthorities()) {
				set.add(authority.getParentAuthority().getDisplayName());
			}
			for (String roleName : set) {
				System.out.print(roleName + ",");
			}
			System.out.println("]");
		}
	}

	@Test
	public void testRoleServiceGetDetails() {
		Map<String, List<String>> map = roleService.getDetails(1);
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			System.out.println(key);
			List<String> val = map.get(key);
			for (String listVal : val) {
				System.out.println("\t\t" + listVal);
			}
		}

	}

	@Test
	public void testRoleServiceDelete() {
		int delete = roleService.delete(35);
		System.out.println(delete);
	}

	@Test
	public void testResourceServiceGetAll() {
		List<Resource> resList = resouceService.getAll();
		for (Resource resource : resList) {
			System.out.println(resource.getResourceId() + "--" + resource.getUrl() + "--" + resource.getAuthorities());
		}
	}

	@Test
	public void testRequestMap() {
		FilterInvocationSecurityMetadataSourceRequestMapBuilderImpl bean = ioc
				.getBean(FilterInvocationSecurityMetadataSourceRequestMapBuilderImpl.class);

		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> map = bean.buildRequestMap();
		Set<RequestMatcher> keySet = map.keySet();
		for (RequestMatcher requestMatcher : keySet) {
			AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
			System.out.println(antPathRequestMatcher.getPattern());

			Collection<ConfigAttribute> collection = map.get(requestMatcher);
			for (ConfigAttribute configAttribute : collection) {
				SecurityConfig securityConfig = (SecurityConfig) configAttribute;
				System.out.println("\t\t" + securityConfig.getAttribute());
			}

			System.out.println("---------------------------------------------------");
		}
	}

}
