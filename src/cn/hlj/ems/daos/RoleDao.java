package cn.hlj.ems.daos;

import org.springframework.stereotype.Repository;

import cn.hlj.ems.entities.Role;

@Repository
public class RoleDao extends BaseDao<Role> {
	/*@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Criteria getCriteria() {
		return getSession().createCriteria(Role.class);
	}

	@SuppressWarnings("unchecked")
	public List<Role> getAll() {
		return getCriteria().setCacheable(true).list();
	}

	@SuppressWarnings("unchecked")
	public List<Role> getByIn(String propertyName, List<? extends Object> propertyVals) {
		Criterion criterion = Restrictions.in(propertyName, propertyVals);
		Criteria criteria = getCriteria().add(criterion);
		return criteria.list();
	}

	public void save(Role role) {
		getSession().saveOrUpdate(role);
	}

	public Role getBy(String propertyName, Integer propertyVal) {
		Criterion criterion = Restrictions.eq(propertyName, propertyVal);
		Criteria criteria = getCriteria().add(criterion);
		return (Role) criteria.uniqueResult();
	}

	public void delete(Role role) {
		getSession().delete(role);
	}*/
}
