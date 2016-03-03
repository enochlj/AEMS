package cn.hlj.ems.daos;

import org.springframework.stereotype.Repository;

import cn.hlj.ems.entities.Department;

@Repository
public class DepartmentDao extends BaseDao<Department> {
/*	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Department getByManager(Employee manager) {
		String hql = "FROM Department d WHERE d.manager=:manager";
		Department department = (Department) getSession().createQuery(hql).setParameter("manager", manager)
				.uniqueResult();
		return department;
	}

	public Criteria getCriteria() {
		return getSession().createCriteria(Department.class);
	}

	@SuppressWarnings("unchecked")
	public List<Department> getAll() {
		return getCriteria().setCacheable(true).list();
	}

	public Department getBy(String propertyName, String propertyVal) {
		Criterion criterion = Restrictions.eq(propertyName, propertyVal);
		Criteria criteria = getCriteria().add(criterion);
		return (Department) criteria.uniqueResult();
	}*/
}
