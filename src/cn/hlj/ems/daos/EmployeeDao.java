package cn.hlj.ems.daos;

import org.springframework.stereotype.Repository;

import cn.hlj.ems.entities.Employee;

@Repository
public class EmployeeDao extends BaseDao<Employee> {
	/*@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Employee getByLoginName(String loginName) {
		String hql = "FROM Employee e WHERE e.loginName=:loginName";
		Employee employee = (Employee) getSession().createQuery(hql).setParameter("loginName", loginName)
				.uniqueResult();
		return employee;
	}

	public long getTotalElements() {
		String hql = "SELECT count(e.id) FROM Employee e";
		return (long) getSession().createQuery(hql).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getContent(Page<Employee> page) {
		int firstResult = (page.getPageNo() - 1) * page.getPageSize();
		int maxResults = page.getPageSize();

		String hql = "FROM Employee e " + "LEFT OUTER JOIN FETCH e.department " + "LEFT OUTER JOIN FETCH e.roles";

		return getSession().createQuery(hql).setFirstResult(firstResult).setMaxResults(maxResults).list();
	}

	@SuppressWarnings("rawtypes")
	public Page getPage(int pageNo) {
		Page<Employee> page = new Page<>();
		page.setPageNo(pageNo);

		long totalElements = getTotalElements();
		page.setTotalElements(totalElements);

		List<Employee> content = getContent(page);
		page.setContent(content);
		return page;
	}

	public Employee get(Integer id) {
		return (Employee) getSession().get(Employee.class, id);
	}

	public void save(Employee employee) {
		getSession().saveOrUpdate(employee);
	}

	@SuppressWarnings("unchecked")
	public List<Employee> getAll() {
		String hql = "FROM Employee e " + "LEFT OUTER JOIN FETCH e.department " + "LEFT OUTER JOIN FETCH e.roles";
		return getSession().createQuery(hql).list();
	}

	public void batchSave(List<Employee> emps) {
		for (int i = 0; i < emps.size(); i++) {
			getSession().save(emps.get(i));

			if ((i + 1) % 50 == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	public void updateVisitedTimes(Integer id) {
		String hql = "UPDATE Employee e " + "SET e.visitedTimes=e.visitedTimes+1 " + "WHERE e.employeeId=:id";
		getSession().createQuery(hql).setParameter("id", id).executeUpdate();
	}
	
	*/
}
