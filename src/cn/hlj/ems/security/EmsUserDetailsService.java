package cn.hlj.ems.security;

import java.util.Collection;
import java.util.HashSet;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.hlj.ems.entities.Authority;
import cn.hlj.ems.entities.Employee;
import cn.hlj.ems.entities.Role;
import cn.hlj.ems.services.EmployeeService;

@Component
public class EmsUserDetailsService implements UserDetailsService {

	@Autowired
	private EmployeeService employeeService;

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeService.getBy("loginName",username);
		if (employee == null) {
			throw new UsernameNotFoundException(username);
		}

		// 初始化Employee的roles，以及roles的authorities
		// Hibernate.initialize(employee.getRoles());
		for (Role role : employee.getRoles()) {
			// Hibernate.initialize(role.getAuthorities());
			for (Authority authority : role.getAuthorities()) {
				Hibernate.initialize(authority.getMainResource());
				Hibernate.initialize(authority.getParentAuthority());
			}
		}

		String password = employee.getPassword();
		boolean enabled = (employee.getEnabled() == 1);
		boolean accountNonExpired = (employee.getIsDeleted() == 0);
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;

		Collection<GrantedAuthorityImpl> authorities = new HashSet<>();
		for (Role role : employee.getRoles()) {
			for (Authority authority : role.getAuthorities()) {
				GrantedAuthorityImpl authorityImpl = new GrantedAuthorityImpl(authority.getName());
				authorities.add(authorityImpl);
			}
		}

//		User user = new User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
//				authorities);
		User user = new SecurityUser(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities, employee);
		return user;
	}

	public class SecurityUser extends User {
		private static final long serialVersionUID = 1L;

		private Employee employee;

		public SecurityUser(String username, String password, boolean enabled, boolean accountNonExpired,
				boolean credentialsNonExpired, boolean accountNonLocked,
				Collection<? extends GrantedAuthority> authorities, Employee employee) {
			super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

			this.employee = employee;
		}

		public Employee getEmployee() {
			return employee;
		}
	}
}
