package cn.hlj.ems.daos;

import org.springframework.stereotype.Repository;

import cn.hlj.ems.entities.Resource;

@Repository
public class ResourceDao extends BaseDao<Resource> {
	/*@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Criteria getCriteria() {
		return getSession().createCriteria(Resource.class);
	}

	@SuppressWarnings("unchecked")
	public List<Resource> getAll() {
		return getCriteria().list();
	}*/
}
